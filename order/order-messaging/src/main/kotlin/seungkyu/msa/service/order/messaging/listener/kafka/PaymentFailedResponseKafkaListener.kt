package seungkyu.msa.service.order.messaging.listener.kafka

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import seungkyu.msa.service.common.status.PaymentStatus
import seungkyu.msa.service.kafka.consumer.KafkaConsumer
import seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel
import seungkyu.msa.service.order.service.dto.message.PaymentResponse
import seungkyu.msa.service.order.service.ports.input.message.listener.payment.PaymentResponseMessageListener
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class PaymentFailedResponseKafkaListener(
    private val paymentResponseMessageListener: PaymentResponseMessageListener
): KafkaConsumer<PaymentFailedResponseAvroModel>{

    private val logger = LoggerFactory.getLogger(PaymentFailedResponseKafkaListener::class.java)

    @KafkaListener(id = "\${kafka.consumer.order-failed-consumer-group-id}",
        topics = ["\${kafka.topic.payment-failed-response}"])
    override fun receive(
        @Payload values: List<PaymentFailedResponseAvroModel>,
        @Header(KafkaHeaders.RECEIVED_KEY) keys: List<String>,
        @Header(KafkaHeaders.PARTITION) partitions: List<Int>,
        @Header(KafkaHeaders.OFFSET) offsets: List<Long>
    ) {
        logger.info("{}개의 결제 완료 응답이 {}키, {}파티션, {}오프셋과 전달되었습니다",
            values.size, keys.toString(), partitions.toString(), offsets.toString())

        values.forEach{
            logger.info("{} 주문이 성공적으로 결제되었습니다", it.id.toString())
            paymentResponseMessageListener.paymentCompleted(
                paymentFailedResponseAvroModelToPaymentResponse(it)
            )
        }
    }

    private fun paymentFailedResponseAvroModelToPaymentResponse(
        paymentFailedResponseAvroModel: PaymentFailedResponseAvroModel
    ): PaymentResponse{
        return PaymentResponse(
            id = paymentFailedResponseAvroModel.id,
            customerId = paymentFailedResponseAvroModel.customerId,
            createdAt = LocalDateTime.ofEpochSecond(paymentFailedResponseAvroModel.createdAt, 0, ZoneOffset.UTC),
            paymentStatus = PaymentStatus.FAILED,
            price = paymentFailedResponseAvroModel.price,
        )
    }

}