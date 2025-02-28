package seungkyu.msa.service.order.persistence.order.adapter

import org.bson.types.ObjectId
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.valueObject.*
import seungkyu.msa.service.order.domain.entity.Order
import seungkyu.msa.service.order.domain.entity.OrderItem
import seungkyu.msa.service.order.persistence.order.entity.OrderEntity
import seungkyu.msa.service.order.persistence.order.entity.OrderItemEntity
import seungkyu.msa.service.order.persistence.order.repository.OrderMongoRepository
import seungkyu.msa.service.order.service.ports.output.repository.OrderRepository

@Repository
class OrderRepositoryImpl(
    private val orderMongoRepository: OrderMongoRepository): OrderRepository {

    override fun save(order: Order): Mono<Order> {
        return orderMongoRepository.save(orderToOrderEntity(order))
            .map(::orderEntityToOrder)
    }

    override fun findById(id: ObjectId): Mono<Order> {
        return orderMongoRepository.findById(id)
            .map(::orderEntityToOrder)
    }

    private fun orderEntityToOrder(orderEntity: OrderEntity): Order {
        return Order(
            orderId = OrderId(orderEntity.id),
            customerId = CustomerId(orderEntity.customerId),
            restaurantId = RestaurantId(orderEntity.restaurantId),
            price = Money(orderEntity.price),
            orderItems = orderEntity.items.map{
                OrderItem(
                    productId = ProductId(it.id),
                    quantity = it.quantity,
                )
            },
            orderStatus = orderEntity.orderStatus
        )
    }

    private fun orderToOrderEntity(order: Order): OrderEntity {
        return OrderEntity(
            id = order.orderId.id,
            customerId = order.customerId.id,
            restaurantId = order.restaurantId.id,
            price = order.price.amount,
            items = order.orderItems.map{
                OrderItemEntity(
                    id = it.productId.id,
                    quantity = it.quantity,
                )
            },
            orderStatus = order.orderStatus
        )
    }
}