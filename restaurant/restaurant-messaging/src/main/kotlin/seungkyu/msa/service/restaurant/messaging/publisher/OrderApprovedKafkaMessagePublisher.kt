package seungkyu.msa.service.restaurant.messaging.publisher

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import seungkyu.msa.service.kafka.model.OrderApprovalStatus
import seungkyu.msa.service.kafka.model.RestaurantApprovalResponseAvroModel
import seungkyu.msa.service.restaurant.domain.event.OrderApprovalEvent
import seungkyu.msa.service.restaurant.service.ports.output.message.publisher.OrderApprovedMessagePublisher
import java.time.ZoneOffset

@Component
class OrderApprovedKafkaMessagePublisher (
    private val reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, RestaurantApprovalResponseAvroModel>,
    @Value("\${kafka.topic.order-approval-response}")
    private val orderApprovalResponseTopic: String,
): OrderApprovedMessagePublisher {

    private val logger = LoggerFactory.getLogger(OrderApprovedKafkaMessagePublisher::class.java)

    override fun publish(domainEvent: OrderApprovalEvent) {

        logger.info("주문: {}의 승인 이벤트를 전송하겠습니다", domainEvent.orderDetail.id)

        reactiveKafkaProducerTemplate.send(
            orderApprovalResponseTopic,
            domainEvent.orderDetail.id.toString(),
            orderApprovalEventToRestaurantApprovalResponseAvroModel(domainEvent)
        ).subscribe()
    }

    private fun orderApprovalEventToRestaurantApprovalResponseAvroModel(orderApprovalEvent: OrderApprovalEvent): RestaurantApprovalResponseAvroModel {
        return RestaurantApprovalResponseAvroModel.newBuilder()
            .setOrderId(orderApprovalEvent.orderDetail.id.id.toString())
            .setRestaurantId(orderApprovalEvent.orderDetail.restaurantId.id.toString())
            .setOrderApprovalStatus(
                OrderApprovalStatus.valueOf(orderApprovalEvent.orderApprovalStatus.name)
            )
            .setCreatedAt(orderApprovalEvent.createdAt.toEpochSecond(ZoneOffset.UTC))
            .build()
    }
}