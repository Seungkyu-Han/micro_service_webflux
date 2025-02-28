package seungkyu.msa.service.order.service.ports.input.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.PaymentOrderStatus
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.common.valueObject.ProductId
import seungkyu.msa.service.common.valueObject.RestaurantId
import seungkyu.msa.service.order.domain.OrderDomainService
import seungkyu.msa.service.order.domain.entity.Order
import seungkyu.msa.service.order.domain.entity.OrderItem
import seungkyu.msa.service.order.domain.event.OrderCreatedEvent
import seungkyu.msa.service.order.service.dto.create.CreateOrderCommand
import seungkyu.msa.service.order.service.dto.create.CreateOrderResponse
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentEventPayload
import seungkyu.msa.service.order.service.outbox.scheduler.payment.PaymentOutboxHelper
import seungkyu.msa.service.order.service.ports.output.repository.OrderRepository
import seungkyu.msa.service.outbox.OutboxStatus

@Service
class OrderApplicationServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderDomainService: OrderDomainService,
    private val paymentOutboxHelper: PaymentOutboxHelper
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

        return paymentOutboxHelper.savePaymentOutboxMessage(
            orderCreatedEventToPaymentEventPayload(orderCreatedEvent),
            orderStatus = orderCreatedEvent.order.orderStatus,
            outboxStatus = OutboxStatus.STARTED
        ).then(
            orderRepository.save(orderCreatedEvent.order)
                .thenReturn(CreateOrderResponse(
                    orderId = orderCreatedEvent.order.orderId.id.toString()
                ))
        )
    }

    override fun trackOrder(id: String): Mono<Order> {
        TODO("Not yet implemented")
    }

    private fun orderCreatedEventToPaymentEventPayload(
        orderCreatedEvent: OrderCreatedEvent): PaymentEventPayload =
        PaymentEventPayload(
            customerId = orderCreatedEvent.order.customerId.id,
            orderId = orderCreatedEvent.order.orderId.id,
            price = orderCreatedEvent.order.price.amount,
            createdAt = orderCreatedEvent.createdAt,
            paymentOrderStatus = PaymentOrderStatus.PENDING,
            orderStatus = orderCreatedEvent.order.orderStatus,
        )
}