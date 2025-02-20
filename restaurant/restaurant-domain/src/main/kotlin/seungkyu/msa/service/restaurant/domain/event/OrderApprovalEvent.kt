package seungkyu.msa.service.restaurant.domain.event

import seungkyu.msa.service.common.event.DomainEvent
import seungkyu.msa.service.common.status.OrderApprovalStatus
import seungkyu.msa.service.restaurant.domain.entity.OrderDetail
import java.time.LocalDateTime

open class OrderApprovalEvent(
    open val orderDetail: OrderDetail,
    open val orderApprovalStatus: OrderApprovalStatus,
    open val createdAt: LocalDateTime
): DomainEvent<OrderDetail>