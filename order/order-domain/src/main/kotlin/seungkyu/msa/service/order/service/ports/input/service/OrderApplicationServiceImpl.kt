package seungkyu.msa.service.order.service.ports.input.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.common.valueObject.ProductId
import seungkyu.msa.service.common.valueObject.RestaurantId
import seungkyu.msa.service.order.domain.OrderDomainService
import seungkyu.msa.service.order.domain.entity.Order
import seungkyu.msa.service.order.domain.entity.OrderItem
import seungkyu.msa.service.order.service.dto.create.CreateOrderCommand
import seungkyu.msa.service.order.service.dto.create.CreateOrderResponse
import seungkyu.msa.service.order.service.ports.output.repository.OrderRepository

@Service
class OrderApplicationServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderDomainService: OrderDomainService
): OrderApplicationService {

    override fun createOrder(createOrderCommand: CreateOrderCommand): Mono<CreateOrderResponse> {
        val orderCreatedEvent = orderDomainService.validateAndInitiateOrder(
            restaurantId = RestaurantId(ObjectId(createOrderCommand.restaurantId)),
            customerId = CustomerId(ObjectId(createOrderCommand.customerId)),
            orderItems = createOrderCommand.items.map{
                OrderItem(
                    productId = ProductId(ObjectId(it.id)),
                    quantity = it.quantity,
                )
            },
            price = Money(createOrderCommand.price)
        )

        return orderRepository.save(orderCreatedEvent.order)
            .thenReturn(CreateOrderResponse(
                orderId = orderCreatedEvent.order.orderId.id.toString()
            ))

    }

    override fun trackOrder(id: String): Mono<Order> {
        TODO("Not yet implemented")
    }
}