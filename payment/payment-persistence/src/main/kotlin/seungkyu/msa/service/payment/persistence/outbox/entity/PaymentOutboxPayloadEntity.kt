package seungkyu.msa.service.payment.persistence.outbox.entity

import org.bson.types.ObjectId
import seungkyu.msa.service.common.status.PaymentOrderStatus
import seungkyu.msa.service.common.status.PaymentStatus
import java.time.LocalDateTime

data class PaymentOutboxPayloadEntity(
    val orderId: ObjectId,
    val customerId: ObjectId,
    val price: Long,
    val createdAt: LocalDateTime,
    val paymentOrderStatus: PaymentOrderStatus,
    val paymentStatus: PaymentStatus,
)
