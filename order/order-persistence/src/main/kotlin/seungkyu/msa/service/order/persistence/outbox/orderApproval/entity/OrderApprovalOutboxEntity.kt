package seungkyu.msa.service.order.persistence.outbox.orderApproval.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.outbox.OutboxStatus
import java.time.LocalDateTime

@Document("order_approvals")
data class OrderApprovalOutboxEntity(
    val id: ObjectId,
    val createdAt: LocalDateTime,
    val processedAt: LocalDateTime,
    val type: String,
    val payload: OrderApprovalEventPayloadEntity,
    val orderStatus: OrderStatus,
    val outboxStatus: OutboxStatus,
    @Version
    val version: Int
)
