package seungkyu.msa.service.order.messaging.listener.kafka

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import seungkyu.msa.service.common.status.OrderApprovalStatus
import seungkyu.msa.service.kafka.consumer.KafkaConsumer
import seungkyu.msa.service.kafka.model.RestaurantApprovalResponseAvroModel
import seungkyu.msa.service.order.service.dto.message.RestaurantApprovalResponse
import seungkyu.msa.service.order.service.ports.input.message.listener.restaurantApproval.RestaurantApprovalResponseMessageListener
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class RestaurantApprovalResponseKafkaListener(
    private val restaurantApprovalResponseMessageListener: RestaurantApprovalResponseMessageListener
): KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private val logger = LoggerFactory.getLogger(RestaurantApprovalResponseKafkaListener::class.java)

    @KafkaListener(id = "\${kafka.consumer.order-approval-consumer-group-id}",
        topics = ["\${kafka.topic.order-approval-response}"])
    override fun receive(
        @Payload values: List<RestaurantApprovalResponseAvroModel>,
        @Header(KafkaHeaders.RECEIVED_KEY) keys: List<String>,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partitions: List<Int>,
        @Header(KafkaHeaders.OFFSET) offsets: List<Long>
    ) {
        logger.info("{}개의 승인 응답이 {}키, {}파티션, {}오프셋과 전달되었습니다",
            values.size, keys.toString(), partitions.toString(), offsets.toString())

        values.forEach {
            if(it.orderApprovalStatus == seungkyu.msa.service.kafka.model.OrderApprovalStatus.APPROVED) {
                restaurantApprovalResponseMessageListener.orderApproved(
                    restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(it)
                )
            }
            else{
                restaurantApprovalResponseMessageListener.orderRejected(
                    restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(it)
                )
            }
        }
    }

    private fun restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(
        restaurantApprovalResponseAvroModel: RestaurantApprovalResponseAvroModel
    ): RestaurantApprovalResponse {
        return RestaurantApprovalResponse(
            id = restaurantApprovalResponseAvroModel.orderId,
            createdAt = LocalDateTime.ofEpochSecond(
                restaurantApprovalResponseAvroModel.createdAt, 0, ZoneOffset.UTC
            ),
            orderApprovalStatus = OrderApprovalStatus.valueOf(
                restaurantApprovalResponseAvroModel.orderApprovalStatus.name
            )
        )
    }
}