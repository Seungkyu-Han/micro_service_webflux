package seungkyu.msa.service.order.persistence.outbox.orderApproval.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Version
import seungkyu.msa.service.common.status.OrderStatus
import java.time.LocalDateTime

data class OrderApprovalOutboxEntity(
    val id: ObjectId,
    val createdAt: LocalDateTime,
    val processedAt: LocalDateTime,
    val type: String,
    val payload: OrderApprovalEventPayloadEntity,
    val orderStatus: OrderStatus,
    val outboxStatus: OrderStatus,
    @Version
    val version: Int
)
