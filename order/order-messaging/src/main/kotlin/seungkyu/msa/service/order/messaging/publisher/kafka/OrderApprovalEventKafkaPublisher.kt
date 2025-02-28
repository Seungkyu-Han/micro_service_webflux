package seungkyu.msa.service.order.messaging.publisher.kafka

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import seungkyu.msa.service.kafka.model.Product
import seungkyu.msa.service.kafka.model.RestaurantApprovalRequestAvroModel
import seungkyu.msa.service.kafka.model.RestaurantOrderStatus
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalOutboxMessage
import seungkyu.msa.service.order.service.ports.output.message.publisher.restaurantApproval.OrderApprovalRequestMessagePublisher
import seungkyu.msa.service.outbox.OutboxStatus
import java.time.ZoneOffset

@Component
class OrderApprovalEventKafkaPublisher(
    private val reactiveKafkaProducer: ReactiveKafkaProducerTemplate<String, RestaurantApprovalRequestAvroModel>,
    @Value("\${kafka.topic.order-approval-request}")
    private val orderApprovalRequestTopic: String
): OrderApprovalRequestMessagePublisher {

    private val logger = LoggerFactory.getLogger(OrderApprovalEventKafkaPublisher::class.java)

    override fun publish(
        orderApprovalOutboxMessage: OrderApprovalOutboxMessage,
        callback: (OrderApprovalOutboxMessage, OutboxStatus) -> Mono<Void>
    ): Mono<Void> {

        logger.info("주문 {}의 승인 요청을 전송하려고 합니다", orderApprovalOutboxMessage.id)

        val restaurantApprovalRequestAvroModel = orderApprovalEventToRestaurantApprovalRequestAvroModel(orderApprovalOutboxMessage)

        return reactiveKafkaProducer.send(
            orderApprovalRequestTopic,
            orderApprovalOutboxMessage.id.toString(),
            restaurantApprovalRequestAvroModel
        ).publishOn(Schedulers.boundedElastic()).doOnNext {
            callback(orderApprovalOutboxMessage, OutboxStatus.COMPLETED).subscribe()
        }.doOnError {
            callback(orderApprovalOutboxMessage, OutboxStatus.FAILED).subscribe()
        }.doFinally {
            logger.info("주문 {}의 승인 요청을 전송했습니다", orderApprovalOutboxMessage.id)
        }.then()
    }

    private fun orderApprovalEventToRestaurantApprovalRequestAvroModel(
        orderApprovalOutboxMessage: OrderApprovalOutboxMessage
    ): RestaurantApprovalRequestAvroModel {
        return RestaurantApprovalRequestAvroModel.newBuilder()
            .setOrderId(orderApprovalOutboxMessage.id.toString())
            .setRestaurantId(orderApprovalOutboxMessage.payload.restaurantId.toString())
            .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
            .setPrice(orderApprovalOutboxMessage.payload.price)
            .setCreatedAt(orderApprovalOutboxMessage.createdAt.toEpochSecond(ZoneOffset.UTC))
            .setProducts(
                orderApprovalOutboxMessage.payload.products.map{
                    Product.newBuilder()
                        .setProductId(it.id.toString())
                        .setQuantity(it.quantity)
                        .build()
                }
            )
            .build()
    }
}