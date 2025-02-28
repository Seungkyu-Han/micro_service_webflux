package seungkyu.msa.service.order.service.saga.mapper

import org.springframework.stereotype.Component
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.common.status.PaymentStatus
import seungkyu.msa.service.order.domain.event.OrderPaidEvent
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalEventPayload
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalEventProduct
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalOutboxMessage
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.saga.SagaConstants.Companion.ORDER_SAGA_NAME
import java.time.LocalDateTime

@Component
class PaymentSagaMapper {

    fun createOrderApprovalOutboxMessageByOrderPaidEvent(orderPaidEvent: OrderPaidEvent): OrderApprovalOutboxMessage {
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

    fun getCurrentOrderStatus(paymentStatus: PaymentStatus): List<OrderStatus> {
        return when(paymentStatus){
            PaymentStatus.COMPLETED -> listOf(OrderStatus.PENDING)
            PaymentStatus.CANCELLED -> listOf(OrderStatus.PAID)
            PaymentStatus.FAILED -> listOf(OrderStatus.PENDING, OrderStatus.PAID)
            PaymentStatus.UNKNOWN -> throw RuntimeException("Unknown payment status: $paymentStatus")
        }
    }
}