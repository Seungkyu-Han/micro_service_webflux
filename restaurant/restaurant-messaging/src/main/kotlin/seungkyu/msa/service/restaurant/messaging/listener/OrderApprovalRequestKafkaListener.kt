package seungkyu.msa.service.restaurant.messaging.listener

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import seungkyu.msa.service.common.status.RestaurantOrderStatus
import seungkyu.msa.service.kafka.consumer.KafkaConsumer
import seungkyu.msa.service.kafka.model.RestaurantApprovalRequestAvroModel
import seungkyu.msa.service.restaurant.service.dto.RestaurantApprovalRequestDto
import seungkyu.msa.service.restaurant.service.ports.input.message.listener.OrderApprovalRequestMessageListener
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class OrderApprovalRequestKafkaListener(
    private val restaurantApprovalRequestMessageListener: OrderApprovalRequestMessageListener
): KafkaConsumer<RestaurantApprovalRequestAvroModel> {

    private val logger = LoggerFactory.getLogger(OrderApprovalRequestKafkaListener::class.java)

    @KafkaListener(id = "\${kafka.consumer.restaurant-consumer-group-id}",
        topics = ["\${kafka.topic.order-approval-request}"])
    override fun receive(
        @Payload values: List<RestaurantApprovalRequestAvroModel>,
        @Header(KafkaHeaders.RECEIVED_KEY) keys: List<String>,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partitions: List<Int>,
        @Header(KafkaHeaders.OFFSET) offsets: List<Long>
    ) {
        logger.info("{}개의 승인 요청이 {}키, {}파티션, {}오프셋과 전달되었습니다",
            values.size, keys.toString(), partitions.toString(), offsets.toString())

        values.forEach{
            restaurantApprovalRequestAvroModel ->
            restaurantApprovalRequestMessageListener.approveOrder(
                restaurantApprovalRequestAvroModelToRestaurantApprovalRequestDto(restaurantApprovalRequestAvroModel)
            ).subscribe()
        }
    }

    private fun restaurantApprovalRequestAvroModelToRestaurantApprovalRequestDto(
        restaurantApprovalRequestAvroModel: RestaurantApprovalRequestAvroModel): RestaurantApprovalRequestDto{
        return RestaurantApprovalRequestDto(
            restaurantId = restaurantApprovalRequestAvroModel.restaurantId,
            orderId = restaurantApprovalRequestAvroModel.orderId,
            restaurantOrderStatus = RestaurantOrderStatus.valueOf(
                restaurantApprovalRequestAvroModel.restaurantOrderStatus.name
            ),
            price = restaurantApprovalRequestAvroModel.price,
            products = restaurantApprovalRequestAvroModel.products.associate {
                it.productId to it.quantity
            }.toMap(HashMap()),
            createdAt = LocalDateTime.ofEpochSecond(restaurantApprovalRequestAvroModel.createdAt, 0, ZoneOffset.UTC)
        )
    }

}