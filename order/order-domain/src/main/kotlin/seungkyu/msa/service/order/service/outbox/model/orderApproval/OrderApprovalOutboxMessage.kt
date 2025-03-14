package seungkyu.msa.service.order.service.outbox.model.orderApproval

import org.bson.types.ObjectId
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.outbox.OutboxStatus
import java.time.LocalDateTime


data class OrderApprovalOutboxMessage(
    val id: ObjectId,
    val createdAt: LocalDateTime,
    var processedAt: LocalDateTime,
    val type: String,
    val payload: OrderApprovalEventPayload,
    var orderStatus: OrderStatus,
    var outboxStatus: OutboxStatus,
    val version: Int
)