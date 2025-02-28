package seungkyu.msa.service.order.service.outbox.model.payment

import org.bson.types.ObjectId
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.common.status.PaymentOrderStatus
import java.time.LocalDateTime

data class PaymentEventPayload(
    val orderId: ObjectId,
    val customerId: ObjectId,
    val price: Long,
    val createdAt: LocalDateTime,
    var paymentOrderStatus: PaymentOrderStatus,
    var orderStatus: OrderStatus
)
