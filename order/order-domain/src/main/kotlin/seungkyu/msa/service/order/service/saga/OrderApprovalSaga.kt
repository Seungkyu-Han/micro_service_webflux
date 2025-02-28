package seungkyu.msa.service.order.service.saga

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.order.domain.OrderDomainService
import seungkyu.msa.service.order.domain.entity.Order
import seungkyu.msa.service.order.domain.event.OrderCancelledEvent
import seungkyu.msa.service.order.service.dto.message.RestaurantApprovalResponse
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalOutboxMessage
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentOutboxMessage
import seungkyu.msa.service.order.service.outbox.scheduler.orderApproval.OrderApprovalOutboxHelper
import seungkyu.msa.service.order.service.outbox.scheduler.payment.PaymentOutboxHelper
import seungkyu.msa.service.order.service.ports.output.repository.OrderRepository
import seungkyu.msa.service.order.service.saga.mapper.OrderApprovalSagaMapper
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.saga.SagaStep
import java.time.LocalDateTime

@Component
class OrderApprovalSaga(
    private val orderDomainService: OrderDomainService,
    private val paymentOutboxHelper: PaymentOutboxHelper,
    private val orderApprovalOutboxHelper: OrderApprovalOutboxHelper,
    private val orderRepository: OrderRepository,
    private val orderApprovalSagaMapper: OrderApprovalSagaMapper
): SagaStep<RestaurantApprovalResponse> {

    private val logger = LoggerFactory.getLogger(OrderApprovalSaga::class.java)

    @Transactional
    override fun process(data: RestaurantApprovalResponse): Mono<Void> {
        return mono{
            val orderApprovalOutboxMessage = orderApprovalOutboxHelper
                .getApprovalOutboxMessageByIdAndOrderStatus(ObjectId(data.id), listOf(OrderStatus.PAID))
                .awaitSingleOrNull()

            if (orderApprovalOutboxMessage != null){
                //주문을 승인 후 저장
                val order:Order = approveOrder(data).awaitSingle()

                //order outbox를 업데이트
                orderApprovalOutboxHelper.save(
                    getUpdatedOrderApprovalOutboxMessage(
                        orderApprovalOutboxMessage,
                        order.orderStatus)
                ).awaitSingle()

                //payment outbox를 업데이트
                getUpdatePaymentOutboxMessage(
                    ObjectId(data.id),
                    order.orderStatus
                ).flatMap {
                    paymentOutboxHelper.save(it)
                }.awaitSingle()

                logger.info("주문 {}는 승인되었습니다", order.orderId.id)
            }
        }.then()
    }

    @Transactional
    override fun rollback(data: RestaurantApprovalResponse): Mono<Void> {
        return mono{
            val orderApprovalOutboxMessage =
                orderApprovalOutboxHelper.getApprovalOutboxMessageByIdAndOrderStatus(
                    ObjectId(data.id),
                    listOf(OrderStatus.PAID)
                ).awaitSingleOrNull()

            if (orderApprovalOutboxMessage != null){
                //주문을 취소 후 저장
                val orderCancelledEvent = rollbackOrder(data).awaitSingle()

                //order outbox를 업데이트
                orderApprovalOutboxHelper.save(
                    getUpdatedOrderApprovalOutboxMessage(
                        orderApprovalOutboxMessage,
                        orderCancelledEvent.order.orderStatus
                    )
                ).awaitSingle()

                //payment outbox에서 조회 후 해당 이벤트를 취소 요청으로 변경 후 저장
                paymentOutboxHelper.getPaymentOutboxMessageByIdAndOrderStatus(
                    id = orderCancelledEvent.order.orderId.id,
                    orderStatuses = listOf(OrderStatus.PAID)
                ).flatMap {
                    paymentOutboxMessage ->
                    paymentOutboxHelper.save(updatePaymentOutboxMessageToRollback(paymentOutboxMessage, orderCancelledEvent))
                }.awaitSingle()
            }
        }.then()
    }

    private fun rollbackOrder(restaurantApprovalResponse: RestaurantApprovalResponse): Mono<OrderCancelledEvent> {
        return orderRepository.findById(ObjectId(restaurantApprovalResponse.id))
            .flatMap{
                val orderCancelledEvent = orderDomainService.cancelOrderPayment(it)
                orderRepository.save(it)
                    .thenReturn(orderCancelledEvent)
            }
    }

    private fun approveOrder(restaurantApprovalResponse: RestaurantApprovalResponse): Mono<Order>{
        return orderRepository.findById(ObjectId(restaurantApprovalResponse.id))
            .flatMap {
                order ->
                orderDomainService.approveOrder(order)
                orderRepository.save(order)
            }
    }

    private fun getUpdatedOrderApprovalOutboxMessage(
        orderApprovalOutboxMessage: OrderApprovalOutboxMessage,
        orderStatus: OrderStatus): OrderApprovalOutboxMessage {
        orderApprovalOutboxMessage.processedAt = LocalDateTime.now()
        orderApprovalOutboxMessage.orderStatus = orderStatus
        return orderApprovalOutboxMessage
    }

    private fun getUpdatePaymentOutboxMessage(
        id: ObjectId, orderStatus: OrderStatus): Mono<PaymentOutboxMessage> {
        return paymentOutboxHelper.getPaymentOutboxMessageByIdAndOrderStatus(id, listOf(OrderStatus.PAID))
            .map {
                paymentOutboxMessage ->
                paymentOutboxMessage.processedAt = LocalDateTime.now()
                paymentOutboxMessage.payload.orderStatus = orderStatus
                paymentOutboxMessage
        }
    }

    private fun updatePaymentOutboxMessageToRollback(
        paymentOutboxMessage: PaymentOutboxMessage,
        orderCancelledEvent: OrderCancelledEvent): PaymentOutboxMessage {
        paymentOutboxMessage.payload = orderApprovalSagaMapper.orderCancelledEventToOrderPaymentEventPayload(orderCancelledEvent)
        paymentOutboxMessage.outboxStatus = OutboxStatus.STARTED
        paymentOutboxMessage.payload.orderStatus = orderCancelledEvent.order.orderStatus
        return paymentOutboxMessage
    }
}