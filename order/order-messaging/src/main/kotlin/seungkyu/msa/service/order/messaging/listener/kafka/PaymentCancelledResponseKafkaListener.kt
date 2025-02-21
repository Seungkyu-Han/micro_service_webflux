package seungkyu.msa.service.order.messaging.listener.kafka

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import seungkyu.msa.service.common.status.PaymentStatus
import seungkyu.msa.service.kafka.consumer.KafkaConsumer
import seungkyu.msa.service.kafka.model.PaymentCancelledResponseAvroModel
import seungkyu.msa.service.order.service.dto.message.PaymentResponse
import seungkyu.msa.service.order.service.ports.input.message.listener.payment.PaymentResponseMessageListener
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class PaymentCancelledResponseKafkaListener(
    private val paymentResponseMessageListener: PaymentResponseMessageListener
): KafkaConsumer<PaymentCancelledResponseAvroModel>{

    private val logger = LoggerFactory.getLogger(PaymentCancelledResponseKafkaListener::class.java)

    @KafkaListener(id = "\${kafka.consumer.order-cancelled-consumer-group-id}",
        topics = ["\${kafka.topic.payment-cancelled-response}"])
    override fun receive(
        @Payload values: List<PaymentCancelledResponseAvroModel>,
        @Header(KafkaHeaders.RECEIVED_KEY) keys: List<String>,
        @Header(KafkaHeaders.PARTITION) partitions: List<Int>,
        @Header(KafkaHeaders.OFFSET) offsets: List<Long>
    ) {
        logger.info("{}개의 결제 취소 응답이 {}키, {}파티션, {}오프셋과 전달되었습니다",
            values.size, keys.toString(), partitions.toString(), offsets.toString())

        values.forEach{
            logger.info("{} 주문이 성공적으로 결제되었습니다", it.id.toString())
            paymentResponseMessageListener.paymentCancelled(
                paymentCompletedResponseAvroModelToPaymentResponse(it)
            )
        }
    }

    private fun paymentCompletedResponseAvroModelToPaymentResponse(
        paymentCancelledResponseAvroModel: PaymentCancelledResponseAvroModel
    ): PaymentResponse{
        return PaymentResponse(
            id = paymentCancelledResponseAvroModel.id,
            customerId = paymentCancelledResponseAvroModel.customerId,
            createdAt = LocalDateTime.ofEpochSecond(paymentCancelledResponseAvroModel.createdAt, 0, ZoneOffset.UTC),
            paymentStatus = PaymentStatus.CANCELLED,
            price = paymentCancelledResponseAvroModel.price,
        )
    }

}