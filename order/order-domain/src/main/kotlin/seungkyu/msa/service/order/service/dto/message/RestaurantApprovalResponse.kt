package seungkyu.msa.service.order.service.dto.message

import seungkyu.msa.service.common.status.OrderApprovalStatus
import java.time.LocalDateTime

data class RestaurantApprovalResponse(
    val id: String,
    val createdAt: LocalDateTime,
    val orderApprovalStatus: OrderApprovalStatus,
)
