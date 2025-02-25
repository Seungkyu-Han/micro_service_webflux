package seungkyu.msa.service.order.persistence.outbox.payment.entity

import org.bson.types.ObjectId
import seungkyu.msa.service.common.status.PaymentOrderStatus
import java.time.LocalDateTime

data class PaymentEventPayloadEntity(
    val orderId: ObjectId,
    val customerId: ObjectId,
    val price: Long,
    val createdAt: LocalDateTime,
    val paymentOrderStatus: PaymentOrderStatus
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaymentEventPayloadEntity

        return orderId == other.orderId
    }

    override fun hashCode(): Int {
        return orderId.hashCode()
    }
}