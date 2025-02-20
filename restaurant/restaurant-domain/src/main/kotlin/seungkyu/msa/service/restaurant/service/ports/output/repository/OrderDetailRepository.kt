package seungkyu.msa.service.restaurant.service.ports.output.repository

import reactor.core.publisher.Mono
import seungkyu.msa.service.restaurant.domain.entity.OrderDetail

interface OrderDetailRepository {

    fun save(orderDetail: OrderDetail): Mono<OrderDetail>
}