package seungkyu.msa.service.restaurant.domain.event

import seungkyu.msa.service.common.status.OrderApprovalStatus
import seungkyu.msa.service.restaurant.domain.entity.OrderDetail
import java.time.LocalDateTime

class OrderRejectedEvent(
    override val orderDetail: OrderDetail,
    override val orderApprovalStatus: OrderApprovalStatus,
    override val createdAt: LocalDateTime
): OrderApprovalEvent(
    orderDetail = orderDetail,
    orderApprovalStatus = orderApprovalStatus,
    createdAt = createdAt
)