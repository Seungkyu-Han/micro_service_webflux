package seungkyu.msa.service.order.service.outbox.model.payment

import org.bson.types.ObjectId
import seungkyu.msa.service.outbox.OutboxStatus
import java.time.LocalDateTime

data class OrderPaymentOutboxMessage(
    val id: ObjectId,
    val createdAt: LocalDateTime,
    val processedAt: LocalDateTime,
    val type: String,
    val payload: Any,
    val outboxStatus: OutboxStatus,
    val version: Int)