package seungkyu.msa.service.order.service.saga

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.common.status.PaymentStatus
import seungkyu.msa.service.order.domain.OrderDomainService
import seungkyu.msa.service.order.domain.entity.Order
import seungkyu.msa.service.order.domain.event.OrderPaidEvent
import seungkyu.msa.service.order.service.dto.message.PaymentResponse
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalEventPayload
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalEventProduct
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalOutboxMessage
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentOutboxMessage
import seungkyu.msa.service.order.service.outbox.scheduler.orderApproval.OrderApprovalOutboxHelper
import seungkyu.msa.service.order.service.outbox.scheduler.payment.PaymentOutboxHelper
import seungkyu.msa.service.order.service.ports.output.repository.OrderRepository
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.saga.SagaConstants.Companion.ORDER_SAGA_NAME
import seungkyu.msa.service.saga.SagaStep
import java.time.LocalDateTime

@Component
class OrderPaymentSaga(
    private val orderDomainService: OrderDomainService,
    private val orderRepository: OrderRepository,
    private val paymentOutboxHelper: PaymentOutboxHelper,
    private val orderApprovalOutboxHelper: OrderApprovalOutboxHelper
): SagaStep<PaymentResponse> {

    private val logger = LoggerFactory.getLogger(OrderPaymentSaga::class.java)

    override fun process(data: PaymentResponse): Mono<Void> {

        return mono{
            val paymentOutboxMessage = paymentOutboxHelper.getPaymentOutboxMessageByIdAndOrderStatus(
                id = ObjectId(data.id),
                orderStatuses = listOf(OrderStatus.PENDING)).awaitSingleOrNull()

            if (paymentOutboxMessage != null) {
                val orderPaidEvent = completePaymentForOrder(data).awaitSingle()

                paymentOutboxHelper.save(
                    getUpdatedPaymentOutboxMessage(
                        paymentOutboxMessage, orderPaidEvent.order.orderStatus)).awaitSingle()

                orderApprovalOutboxHelper.save(
                    createOrderApprovalOutboxMessageByOrderPaidEvent(orderPaidEvent)).awaitSingle()
            }
        }.then()
    }

    override fun rollback(data: PaymentResponse): Mono<Void> {
        return mono{
            val orderPaymentOutboxMessage = paymentOutboxHelper.getPaymentOutboxMessageByIdAndOrderStatus(
                ObjectId(data.id),
                getCurrentOrderStatus(data.paymentStatus)
            ).awaitSingleOrNull()

            if(orderPaymentOutboxMessage == null){
                logger.info("{}의 주문이 이미 취소 처리되었습니다", data.id)
            }else{
                val order = rollbackPaymentForOrder(data).awaitSingle()

                paymentOutboxHelper.save(
                    getUpdatedPaymentOutboxMessage(
                        orderPaymentOutboxMessage,
                        order.orderStatus
                    )
                ).awaitSingle()
            }
        }.then()
    }

    private fun rollbackPaymentForOrder(paymentResponse: PaymentResponse): Mono<Order> {
        logger.info("주문 {}을 취소하고 있습니다.", paymentResponse.id)
        return orderRepository.findById(ObjectId(paymentResponse.id))
            .flatMap{
                it.cancel()
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

    private fun getCurrentOrderStatus(paymentStatus: PaymentStatus): List<OrderStatus> {
        return when(paymentStatus){
            PaymentStatus.COMPLETED -> listOf(OrderStatus.PENDING)
            PaymentStatus.CANCELLED -> listOf(OrderStatus.PAID)
            PaymentStatus.FAILED -> listOf(OrderStatus.PENDING, OrderStatus.PAID)
            PaymentStatus.UNKNOWN -> throw RuntimeException("Unknown payment status: $paymentStatus")
        }
    }

    private fun createOrderApprovalOutboxMessageByOrderPaidEvent(orderPaidEvent: OrderPaidEvent): OrderApprovalOutboxMessage {
        return OrderApprovalOutboxMessage(
            id = orderPaidEvent.order.orderId.id,
            createdAt = LocalDateTime.now(),
            processedAt = LocalDateTime.now(),
            type = ORDER_SAGA_NAME,
            payload = OrderApprovalEventPayload(
                restaurantId = orderPaidEvent.order.restaurantId.id,
                price = orderPaidEvent.order.price.amount,
                products = orderPaidEvent.order.orderItems.map{
                    OrderApprovalEventProduct(
                        id = it.productId.id,
                        quantity = it.quantity,
                    )
                }
            ),
            orderStatus = orderPaidEvent.order.orderStatus,
            outboxStatus = OutboxStatus.STARTED,
            version = 0
        )
    }
}