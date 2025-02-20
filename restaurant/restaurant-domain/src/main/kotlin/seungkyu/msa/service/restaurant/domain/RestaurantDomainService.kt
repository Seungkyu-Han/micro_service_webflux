package seungkyu.msa.service.restaurant.domain

import seungkyu.msa.service.restaurant.domain.entity.OrderDetail
import seungkyu.msa.service.restaurant.domain.entity.Product
import seungkyu.msa.service.restaurant.domain.entity.Restaurant
import seungkyu.msa.service.restaurant.domain.event.OrderApprovalEvent

interface RestaurantDomainService {

    fun validateOrder(
        orderDetail: OrderDetail,
        restaurant: Restaurant,
        failureMessages: MutableList<String>
    ): OrderApprovalEvent

    fun createRestaurant(): Restaurant

    fun addProduct(restaurant: Restaurant, product: Product): Restaurant
}