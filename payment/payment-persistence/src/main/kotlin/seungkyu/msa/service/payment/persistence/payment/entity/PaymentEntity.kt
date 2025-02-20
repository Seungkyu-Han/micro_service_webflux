package seungkyu.msa.service.payment.persistence.payment.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import seungkyu.msa.service.common.status.PaymentStatus
import java.time.LocalDateTime

@Document(collection = "payments")
data class PaymentEntity(
    @Id
    val id: ObjectId = ObjectId.get(),
    val customerId: ObjectId,
    val orderId: ObjectId,
    val price: Long,
    val paymentStatus: PaymentStatus,
    val createdAt: LocalDateTime = LocalDateTime.now(),
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaymentEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}