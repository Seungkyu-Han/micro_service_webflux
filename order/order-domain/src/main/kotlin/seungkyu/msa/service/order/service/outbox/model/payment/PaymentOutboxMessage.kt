package seungkyu.msa.service.order.service.outbox.model.payment

import org.bson.types.ObjectId
import seungkyu.msa.service.outbox.OutboxStatus
import java.time.LocalDateTime

data class PaymentOutboxMessage(
    val id: ObjectId,
    val createdAt: LocalDateTime,
    var processedAt: LocalDateTime,
    val type: String,
    var payload: PaymentEventPayload,
    var outboxStatus: OutboxStatus,
    val version: Int
){
    override fun toString(): String {
        return "PaymentOutboxMessage(id=$id, createdAt=$createdAt, processedAt=$processedAt, type='$type', payload=$payload, outboxStatus=$outboxStatus, version=$version)"
    }
}