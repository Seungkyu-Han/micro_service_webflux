package seungkyu.msa.service.order.service.ports.input.message.listener.restaurantApproval

import seungkyu.msa.service.order.service.dto.message.RestaurantApprovalResponse

interface RestaurantApprovalResponseMessageListener {

    fun orderApproved(restaurantApprovalResponse: RestaurantApprovalResponse)

    fun orderRejected(restaurantApprovalResponse: RestaurantApprovalResponse)
}