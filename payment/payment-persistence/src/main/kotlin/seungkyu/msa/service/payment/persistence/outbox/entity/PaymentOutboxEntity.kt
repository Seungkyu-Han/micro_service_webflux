package seungkyu.msa.service.payment.persistence.outbox.entity

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.outbox.OutboxStatus
import java.time.LocalDateTime

@Document(collection = "payment_outboxes")
data class PaymentOutboxEntity(
    val id: ObjectId,
    val createdAt: LocalDateTime,
    var processedAt: LocalDateTime,
    val type: String,
    val payload: PaymentOutboxPayloadEntity,
    var outboxStatus: OutboxStatus,
    var orderStatus: OrderStatus,
    val version: Int
)
