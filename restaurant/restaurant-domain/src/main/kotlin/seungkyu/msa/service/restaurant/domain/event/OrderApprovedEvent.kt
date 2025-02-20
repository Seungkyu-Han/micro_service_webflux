package seungkyu.msa.service.restaurant.domain.event

import seungkyu.msa.service.common.status.OrderApprovalStatus
import seungkyu.msa.service.common.valueObject.OrderId
import seungkyu.msa.service.common.valueObject.RestaurantId
import java.time.LocalDateTime

class OrderApprovedEvent(
    override val restaurantId: RestaurantId,
    override val orderId: OrderId,
    override val orderApprovalStatus: OrderApprovalStatus,
    override val createdAt: LocalDateTime
): OrderApprovalEvent(
    restaurantId = restaurantId,
    orderId = orderId,
    orderApprovalStatus = orderApprovalStatus,
    createdAt = createdAt
)