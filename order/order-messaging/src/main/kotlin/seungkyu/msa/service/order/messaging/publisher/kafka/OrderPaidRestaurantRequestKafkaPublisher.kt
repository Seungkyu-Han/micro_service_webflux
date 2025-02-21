package seungkyu.msa.service.order.messaging.publisher.kafka

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import seungkyu.msa.service.kafka.model.RestaurantApprovalRequestAvroModel
import seungkyu.msa.service.kafka.model.RestaurantOrderStatus
import seungkyu.msa.service.order.domain.event.OrderPaidEvent
import seungkyu.msa.service.order.service.ports.output.message.publisher.restaurantApproval.OrderPaidRestaurantRequestMessagePublisher
import java.time.ZoneOffset

@Component
class OrderPaidRestaurantRequestKafkaPublisher(
    @Value("\${kafka.topic.order-approval-request}")
    private val orderApprovalRequestTopic: String,
    private val reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, RestaurantApprovalRequestAvroModel>,
): OrderPaidRestaurantRequestMessagePublisher {

    private val logger = LoggerFactory.getLogger(OrderPaidRestaurantRequestKafkaPublisher::class.java)

    override fun publish(domainEvent: OrderPaidEvent) {
        val restaurantApprovalRequestAvroModel = orderPaidEventToRestaurantApprovalRequestAvroModel(orderPaidEvent = domainEvent)

        reactiveKafkaProducerTemplate.send(
            orderApprovalRequestTopic,
            domainEvent.order.orderId.id.toString(),
            restaurantApprovalRequestAvroModel
        ).doOnNext {
            logger.info("{} 주문의 승인 요청이 전송되었습니다.", domainEvent.order.orderId.id)
        }.subscribe()
    }

    private fun orderPaidEventToRestaurantApprovalRequestAvroModel(orderPaidEvent: OrderPaidEvent): RestaurantApprovalRequestAvroModel{
        return RestaurantApprovalRequestAvroModel.newBuilder()
            .setOrderId(orderPaidEvent.order.orderId.id.toString())
            .setRestaurantId(orderPaidEvent.order.restaurantId.id.toString())
            .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
            .setPrice(orderPaidEvent.order.price.amount)
            .setCreatedAt(orderPaidEvent.createdAt.toEpochSecond(ZoneOffset.UTC))
            .setProducts(
                orderPaidEvent.order.orderItems
                    .associate { it.productId.id.toString() to it.quantity }
            )
            .build()
    }
}