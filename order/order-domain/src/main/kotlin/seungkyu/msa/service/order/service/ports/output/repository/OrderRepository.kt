package seungkyu.msa.service.order.service.ports.output.repository

import org.bson.types.ObjectId
import reactor.core.publisher.Mono
import seungkyu.msa.service.order.domain.entity.Order

interface OrderRepository {

    fun save(order: Order): Mono<Order>

    fun findById(id: ObjectId): Mono<Order>
}