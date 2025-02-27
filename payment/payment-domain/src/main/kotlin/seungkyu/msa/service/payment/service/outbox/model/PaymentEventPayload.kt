package seungkyu.msa.service.payment.service.outbox.model

import org.bson.types.ObjectId
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.common.status.PaymentOrderStatus
import seungkyu.msa.service.common.status.PaymentStatus
import java.time.LocalDateTime

data class PaymentEventPayload(
    val orderId: ObjectId,
    val customerId: ObjectId,
    val price: Long,
    val createdAt: LocalDateTime,
    val paymentOrderStatus: PaymentOrderStatus,
    var orderStatus: OrderStatus,
    val paymentStatus: PaymentStatus,
)
