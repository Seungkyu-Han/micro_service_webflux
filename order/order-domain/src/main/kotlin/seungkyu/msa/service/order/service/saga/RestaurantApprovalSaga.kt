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
import seungkyu.msa.service.common.status.PaymentOrderStatus
import seungkyu.msa.service.order.domain.OrderDomainService
import seungkyu.msa.service.order.domain.entity.Order
import seungkyu.msa.service.order.domain.event.OrderCancelledEvent
import seungkyu.msa.service.order.service.dto.message.RestaurantApprovalResponse
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalOutboxMessage
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentEventPayload
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentOutboxMessage
import seungkyu.msa.service.order.service.outbox.scheduler.orderApproval.OrderApprovalOutboxHelper
import seungkyu.msa.service.order.service.outbox.scheduler.payment.PaymentOutboxHelper
import seungkyu.msa.service.order.service.ports.output.repository.OrderRepository
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.saga.SagaStep
import java.time.LocalDateTime

@Component
class RestaurantApprovalSaga(
    private val orderDomainService: OrderDomainService,
    private val paymentOutboxHelper: PaymentOutboxHelper,
    private val orderApprovalOutboxHelper: OrderApprovalOutboxHelper,
    private val orderRepository: OrderRepository
): SagaStep<RestaurantApprovalResponse> {

    private val logger = LoggerFactory.getLogger(RestaurantApprovalSaga::class.java)

    @Transactional
    override fun process(data: RestaurantApprovalResponse): Mono<Void> {
        return mono{

            val orderApprovalOutboxMessage = orderApprovalOutboxHelper.getApprovalOutboxMessageByIdAndOrderStatus(
                ObjectId(data.id),
                listOf(OrderStatus.PAID)
            ).awaitSingleOrNull()

            if (orderApprovalOutboxMessage == null){
                logger.info("{}의 주문은 이미 승인 요청 처리되었습니다.", data.id)
            }
            else{

                val order = approveOrder(data).awaitSingle()

                orderApprovalOutboxHelper.save(
                    getUpdatedOrderApprovalOutboxMessage(
                        orderApprovalOutboxMessage,
                        order.orderStatus
                    )
                ).awaitSingle()

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

            if (orderApprovalOutboxMessage == null){
                logger.info("{}의 주문은 이미 취소 요청 처리되었습니다.", data.id)
            }else{
                val orderCancelledEvent = rollbackOrder(data).awaitSingle()

                orderApprovalOutboxHelper.save(
                    getUpdatedOrderApprovalOutboxMessage(
                        orderApprovalOutboxMessage,
                        orderCancelledEvent.order.orderStatus
                    )
                ).awaitSingle()

                paymentOutboxHelper.deleteById(id = orderCancelledEvent.order.orderId.id)
                    .then(
                        paymentOutboxHelper.savePaymentOutboxMessage(
                            paymentOutboxPayload = orderCancelledEventToOrderPaymentEventPayload(orderCancelledEvent),
                            orderStatus = orderCancelledEvent.order.orderStatus,
                            outboxStatus = OutboxStatus.STARTED
                        )
                    ).awaitSingle()

                logger.info("주문 {}는 취소요청되었습니다", orderCancelledEvent.order.orderId.id)
            }
        }.then()
    }

    private fun orderCancelledEventToOrderPaymentEventPayload(orderCancelledEvent: OrderCancelledEvent): PaymentEventPayload {
        return PaymentEventPayload(
            customerId = orderCancelledEvent.order.customerId.id,
            orderId = orderCancelledEvent.order.orderId.id,
            price = orderCancelledEvent.order.price.amount,
            createdAt = orderCancelledEvent.createdAt,
            paymentOrderStatus = PaymentOrderStatus.PENDING,
            orderStatus = orderCancelledEvent.order.orderStatus,
        )
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
        return paymentOutboxHelper.getPaymentOutboxMessageByIdAndOrderStatus(
            id, listOf(OrderStatus.PAID)
        ).map {
            paymentOutboxMessage ->
            paymentOutboxMessage.processedAt = LocalDateTime.now()
            paymentOutboxMessage.payload.orderStatus = orderStatus
            paymentOutboxMessage
        }
    }
}