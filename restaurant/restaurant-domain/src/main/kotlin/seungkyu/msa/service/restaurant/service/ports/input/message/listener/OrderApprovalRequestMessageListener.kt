package seungkyu.msa.service.restaurant.service.ports.input.message.listener

import reactor.core.publisher.Mono
import seungkyu.msa.service.restaurant.service.dto.RestaurantApprovalRequestDto

interface OrderApprovalRequestMessageListener {

    fun approveOrder(restaurantApprovalRequestDto: RestaurantApprovalRequestDto): Mono<Void>
}