package seungkyu.msa.service.order.domain

import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.common.valueObject.OrderId
import seungkyu.msa.service.common.valueObject.RestaurantId
import seungkyu.msa.service.order.domain.entity.Order
import seungkyu.msa.service.order.domain.entity.OrderItem
import seungkyu.msa.service.order.domain.event.OrderCancelledEvent
import seungkyu.msa.service.order.domain.event.OrderCreatedEvent
import seungkyu.msa.service.order.domain.event.OrderPaidEvent

@Service
class OrderDomainServiceImpl: OrderDomainService {

    private val logger = LoggerFactory.getLogger(OrderDomainServiceImpl::class.java)

    override fun validateAndInitiateOrder(
        restaurantId: RestaurantId,
        customerId: CustomerId,
        orderItems: List<OrderItem>,
        price: Money
    ): OrderCreatedEvent {
        val orderId = OrderId(ObjectId.get())

        logger.info("{}의 주문이 생성되었습니다", orderId)

        return OrderCreatedEvent(
            order = Order(
                orderId = orderId,
                customerId = customerId,
                restaurantId = restaurantId,
                orderItems = orderItems,
                price = price
            )
        )
    }

    override fun payOrder(order: Order): OrderPaidEvent {
        order.pay()
        logger.info("{} 주문이 결제 처리되었습니다.", order.orderId.id)
        return OrderPaidEvent(order)
    }

    override fun cancelOrderPayment(order: Order): OrderCancelledEvent {
        order.cancelPay()
        logger.info("{} 주문의 결제가 취소 처리되었습니다.", order.orderId.id)
        return OrderCancelledEvent(order)
    }

    override fun approveOrder(order: Order) {
        order.approve()
        logger.info("{} 주문이 승인되었습니다.", order.orderId.id)
    }

    override fun cancelOrder(order: Order) {
        order.cancel()
        logger.info("{} 주문이 취소되었습니다.", order.orderId.id)
    }
}