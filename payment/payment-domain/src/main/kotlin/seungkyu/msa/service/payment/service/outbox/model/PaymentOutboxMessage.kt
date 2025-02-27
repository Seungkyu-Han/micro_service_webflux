package seungkyu.msa.service.payment.service.outbox.model

import org.bson.types.ObjectId
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.saga.SagaConstants.Companion.ORDER_SAGA_NAME
import java.time.LocalDateTime

data class PaymentOutboxMessage(
    val id: ObjectId,
    val createdAt: LocalDateTime,
    var processedAt: LocalDateTime,
    val type: String = ORDER_SAGA_NAME,
    val payload: PaymentEventPayload,
    var outboxStatus: OutboxStatus,
    val version: Int
)