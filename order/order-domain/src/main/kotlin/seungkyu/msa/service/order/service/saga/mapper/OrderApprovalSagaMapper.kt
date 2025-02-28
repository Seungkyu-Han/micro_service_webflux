package seungkyu.msa.service.order.service.saga.mapper

import org.springframework.stereotype.Component
import seungkyu.msa.service.common.status.PaymentOrderStatus
import seungkyu.msa.service.order.domain.event.OrderCancelledEvent
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentEventPayload

@Component
class OrderApprovalSagaMapper {

    fun orderCancelledEventToOrderPaymentEventPayload(orderCancelledEvent: OrderCancelledEvent): PaymentEventPayload {
        return PaymentEventPayload(
            customerId = orderCancelledEvent.order.customerId.id,
            orderId = orderCancelledEvent.order.orderId.id,
            price = orderCancelledEvent.order.price.amount,
            createdAt = orderCancelledEvent.createdAt,
            paymentOrderStatus = PaymentOrderStatus.CANCELLING,
            orderStatus = orderCancelledEvent.order.orderStatus,
        )
    }
}