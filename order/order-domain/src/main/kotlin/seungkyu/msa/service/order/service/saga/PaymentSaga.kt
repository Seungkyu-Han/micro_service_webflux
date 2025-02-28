package seungkyu.msa.service.order.service.saga

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.order.domain.OrderDomainService
import seungkyu.msa.service.order.domain.entity.Order
import seungkyu.msa.service.order.domain.event.OrderPaidEvent
import seungkyu.msa.service.order.service.dto.message.PaymentResponse
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentOutboxMessage
import seungkyu.msa.service.order.service.outbox.scheduler.orderApproval.OrderApprovalOutboxHelper
import seungkyu.msa.service.order.service.outbox.scheduler.payment.PaymentOutboxHelper
import seungkyu.msa.service.order.service.ports.output.repository.OrderRepository
import seungkyu.msa.service.order.service.saga.mapper.PaymentSagaMapper
import seungkyu.msa.service.saga.SagaStep
import java.time.LocalDateTime

@Component
class PaymentSaga(
    private val orderDomainService: OrderDomainService,
    private val orderRepository: OrderRepository,
    private val paymentOutboxHelper: PaymentOutboxHelper,
    private val orderApprovalOutboxHelper: OrderApprovalOutboxHelper,
    private val paymentSagaMapper: PaymentSagaMapper,
): SagaStep<PaymentResponse> {

    override fun process(data: PaymentResponse): Mono<Void> {
        return mono{
            //해당 이벤트가 이미 처리되었는지 확인
            val paymentOutboxMessage = paymentOutboxHelper.getPaymentOutboxMessageByIdAndOrderStatus(
                id = ObjectId(data.id), orderStatuses = listOf(OrderStatus.PENDING)).awaitSingleOrNull()

            if (paymentOutboxMessage != null) {

                //결제를 진행 후 저장하고, 결제 완료 이벤트를 생성
                val orderPaidEvent = completePaymentForOrder(data).awaitSingle()

                //결제 outbox를 업데이트
                paymentOutboxHelper.save(
                    getUpdatedPaymentOutboxMessage(paymentOutboxMessage, orderPaidEvent.order.orderStatus)
                ).awaitSingle()

                //주문 outbox에 새로운 데이터 생성
                orderApprovalOutboxHelper.save(
                    paymentSagaMapper.createOrderApprovalOutboxMessageByOrderPaidEvent(orderPaidEvent)).awaitSingle()
            }
        }.then()
    }

    override fun rollback(data: PaymentResponse): Mono<Void> {
        return mono{
            //해당 이벤트가 이미 처리되었는지 확인
            val orderPaymentOutboxMessage:PaymentOutboxMessage? = paymentOutboxHelper.getPaymentOutboxMessageByIdAndOrderStatus(
                id = ObjectId(data.id),
                orderStatuses = listOf(OrderStatus.CANCELLING)
            ).awaitSingleOrNull()

            if(orderPaymentOutboxMessage != null){
                //결제를 복구 후 저장
                val order:Order = rollbackPaymentForOrder(data).awaitSingle()

                //결제 outbox를 업데이트
                paymentOutboxHelper.save(
                    getUpdatedPaymentOutboxMessage(orderPaymentOutboxMessage, order.orderStatus)
                ).awaitSingle()
            }
        }.then()
    }

    private fun rollbackPaymentForOrder(paymentResponse: PaymentResponse): Mono<Order> {
        return orderRepository.findById(ObjectId(paymentResponse.id))
            .flatMap{
                orderDomainService.cancelOrder(it)
                orderRepository.save(it)
            }
    }

    private fun completePaymentForOrder(paymentResponse: PaymentResponse): Mono<OrderPaidEvent> {
        return orderRepository.findById(ObjectId(paymentResponse.id))
            .flatMap{
                val orderPaidEvent = orderDomainService.payOrder(order = it)
                orderRepository.save(it)
                    .thenReturn(orderPaidEvent)
            }
    }


    private fun getUpdatedPaymentOutboxMessage(
        paymentOutboxMessage: PaymentOutboxMessage,
        orderStatus: OrderStatus
    ): PaymentOutboxMessage {
        paymentOutboxMessage.processedAt = LocalDateTime.now()
        paymentOutboxMessage.payload.orderStatus = orderStatus
        return paymentOutboxMessage
    }
}