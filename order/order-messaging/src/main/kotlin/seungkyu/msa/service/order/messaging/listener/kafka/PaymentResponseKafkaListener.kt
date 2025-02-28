package seungkyu.msa.service.order.messaging.listener.kafka

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import seungkyu.msa.service.kafka.consumer.KafkaConsumer
import seungkyu.msa.service.kafka.model.PaymentResponseAvroModel
import seungkyu.msa.service.kafka.model.PaymentStatus
import seungkyu.msa.service.order.service.dto.message.PaymentResponse
import seungkyu.msa.service.order.service.ports.input.message.listener.payment.PaymentResponseMessageListener
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class PaymentResponseKafkaListener(
    private val paymentResponseMessageListener: PaymentResponseMessageListener
): KafkaConsumer<PaymentResponseAvroModel> {

    private val logger = LoggerFactory.getLogger(PaymentResponseKafkaListener::class.java)

    @KafkaListener(id = "\${kafka.consumer.payment-consumer-group-id}",
        topics = ["\${kafka.topic.payment-response}"])
    override fun receive(
        @Payload values: List<PaymentResponseAvroModel>,
        @Header(KafkaHeaders.RECEIVED_KEY) keys: List<String>,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partitions: List<Int>,
        @Header(KafkaHeaders.OFFSET) offsets: List<Long>
    ) {
        logger.info("{} number of payment response received", values.size)

        values.forEach{
            paymentResponseAvroModel ->
            if(paymentResponseAvroModel.paymentStatus == PaymentStatus.COMPLETED){
                logger.info("{} 주문이 결제에 성공했습니다.", paymentResponseAvroModel.id)
                paymentResponseMessageListener.paymentCompleted(
                    paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel)
                )
            }else if(paymentResponseAvroModel.paymentStatus == PaymentStatus.FAILED ||
                paymentResponseAvroModel.paymentStatus == PaymentStatus.CANCELLED){
                logger.info("{} 주문이 결제에 실패했습니다.", paymentResponseAvroModel.id)
                paymentResponseMessageListener.paymentCancelled(
                    paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel)
                )
            }
        }
    }

    private fun paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel: PaymentResponseAvroModel): PaymentResponse{
        return PaymentResponse(
            id = paymentResponseAvroModel.id,
            customerId = paymentResponseAvroModel.customerId,
            createdAt = LocalDateTime.ofEpochSecond(paymentResponseAvroModel.createdAt, 0, ZoneOffset.UTC),
            paymentStatus = seungkyu.msa.service.common.status.PaymentStatus.valueOf(paymentResponseAvroModel.paymentStatus.name),
            price = paymentResponseAvroModel.price,
        )
    }
}