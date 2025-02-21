package seungkyu.msa.service.order.service.ports.input.service

import seungkyu.msa.service.order.domain.entity.Order
import seungkyu.msa.service.order.service.dto.create.CreateOrderCommand
import seungkyu.msa.service.order.service.dto.create.CreateOrderResponse

interface OrderApplicationService {

    fun createOrder(createOrderCommand: CreateOrderCommand): CreateOrderResponse

    fun trackOrder(id: String): Order
}