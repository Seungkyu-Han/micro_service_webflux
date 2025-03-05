package seungkyu.msa.service.order.persistence.outbox.payment.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.outbox.OutboxStatus
import java.time.LocalDateTime

@Document("payment_outboxes")
data class PaymentOutboxEntity(
    @Id
    val id: ObjectId,
    val createdAt: LocalDateTime,
    val processedAt: LocalDateTime,
    val type: String,
    val payload: PaymentEventPayloadEntity,
    var orderStatus: OrderStatus,
    var outboxStatus: OutboxStatus,
    @Version
    val version: Int
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaymentOutboxEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}