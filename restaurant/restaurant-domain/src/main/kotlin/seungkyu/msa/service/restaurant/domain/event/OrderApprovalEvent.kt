package seungkyu.msa.service.restaurant.domain.event

import seungkyu.msa.service.common.status.OrderApprovalStatus
import seungkyu.msa.service.common.valueObject.OrderId
import seungkyu.msa.service.common.valueObject.RestaurantId
import java.time.LocalDateTime

open class OrderApprovalEvent(
    open val restaurantId: RestaurantId,
    open val orderId: OrderId,
    open val orderApprovalStatus: OrderApprovalStatus,
    open val createdAt: LocalDateTime
)
