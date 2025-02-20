package seungkyu.msa.service.restaurant.service.dto

import seungkyu.msa.service.common.status.RestaurantOrderStatus
import java.time.LocalDateTime

data class RestaurantApprovalRequestDto(
    val restaurantId: String,
    val orderId: String,
    val restaurantOrderStatus: RestaurantOrderStatus,
    val products: HashMap<String, Int>,
    val price: Long,
    val createdAt: LocalDateTime
)