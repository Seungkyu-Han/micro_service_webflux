package seungkyu.msa.service.restaurant.domain

import seungkyu.msa.service.restaurant.domain.entity.OrderDetail
import seungkyu.msa.service.restaurant.domain.event.OrderApprovalEvent

interface RestaurantDomainService {

    fun validateOrder(
        orderDetail: OrderDetail,
        failureMessages: MutableList<String>
    ): OrderApprovalEvent
}