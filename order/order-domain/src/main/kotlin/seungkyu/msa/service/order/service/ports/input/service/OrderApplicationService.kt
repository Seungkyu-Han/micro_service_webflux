package seungkyu.msa.service.order.service.ports.input.service

import reactor.core.publisher.Mono
import seungkyu.msa.service.order.domain.entity.Order
import seungkyu.msa.service.order.service.dto.create.CreateOrderCommand
import seungkyu.msa.service.order.service.dto.create.CreateOrderResponse

interface OrderApplicationService {

    fun createOrder(createOrderCommand: CreateOrderCommand): Mono<CreateOrderResponse>

    fun trackOrder(id: String): Mono<Order>
}