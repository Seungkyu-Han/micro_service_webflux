package seungkyu.msa.service.restaurant.domain.event

import seungkyu.msa.service.common.status.OrderApprovalStatus
import seungkyu.msa.service.restaurant.domain.entity.OrderDetail
import java.time.LocalDateTime

class OrderApprovedEvent(
    override val orderDetail: OrderDetail,
    override val orderApprovalStatus: OrderApprovalStatus,
    override val createdAt: LocalDateTime
): OrderApprovalEvent(
    orderDetail = orderDetail,
    orderApprovalStatus = orderApprovalStatus,
    createdAt = createdAt
)