package seungkyu.msa.service.order.domain

import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.common.valueObject.RestaurantId
import seungkyu.msa.service.order.domain.entity.Order
import seungkyu.msa.service.order.domain.entity.OrderItem
import seungkyu.msa.service.order.domain.event.OrderCancelledEvent
import seungkyu.msa.service.order.domain.event.OrderCreatedEvent
import seungkyu.msa.service.order.domain.event.OrderPaidEvent

interface OrderDomainService {

    fun validateAndInitiateOrder(
        restaurantId: RestaurantId,
        customerId: CustomerId,
        orderItems: List<OrderItem>,
        price: Money
    ): OrderCreatedEvent

    fun payOrder(order: Order): OrderPaidEvent

    fun cancelOrderPayment(order: Order): OrderCancelledEvent

    fun approveOrder(order: Order)

    fun cancelOrder(order: Order)
}