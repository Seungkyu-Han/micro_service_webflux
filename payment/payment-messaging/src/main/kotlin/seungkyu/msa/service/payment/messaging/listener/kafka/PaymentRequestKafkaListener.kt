package seungkyu.msa.service.payment.messaging.listener.kafka

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import seungkyu.msa.service.common.status.PaymentOrderStatus
import seungkyu.msa.service.kafka.consumer.KafkaConsumer
import seungkyu.msa.service.kafka.model.PaymentRequestAvroModel
import seungkyu.msa.service.payment.service.dto.PaymentRequestDto
import seungkyu.msa.service.payment.service.ports.input.message.listener.PaymentRequestMessageListener
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class PaymentRequestKafkaListener(
    private val paymentRequestMessageListener: PaymentRequestMessageListener
): KafkaConsumer<PaymentRequestAvroModel> {

    private val logger = LoggerFactory.getLogger(PaymentRequestKafkaListener::class.java)

    @KafkaListener(id = "\${kafka.consumer.payment-consumer-group-id}",
        topics = ["\${kafka.topic.payment-request}"])
    override fun receive(
        @Payload values: List<PaymentRequestAvroModel>,
        @Header(KafkaHeaders.RECEIVED_KEY) keys: List<String>,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partitions: List<Int>,
        @Header(KafkaHeaders.OFFSET) offsets: List<Long>
    ) {
        logger.info("{}개의 결제 요청이 {}키, {}파티션, {}오프셋과 전달되었습니다",
            values.size, keys.toString(), partitions.toString(), offsets.toString())

        values.forEach {
            paymentRequestAvroModel ->
            val paymentRequestDto = paymentRequestAvroModelToPaymentRequestDto(paymentRequestAvroModel)
            if(paymentRequestDto.paymentOrderStatus == PaymentOrderStatus.PENDING){
                logger.info("주문 {}의 결제가 진행 중입니다", paymentRequestDto.id)
                paymentRequestMessageListener.completePayment(paymentRequestDto)
            }else{
                logger.info("주문 {}의 결제가 취소 중입니다", paymentRequestDto.id)
                paymentRequestMessageListener.cancelPayment(paymentRequestDto)
            }.subscribe()
        }
    }

    private fun paymentRequestAvroModelToPaymentRequestDto(
        paymentRequestAvroModel: PaymentRequestAvroModel
    ): PaymentRequestDto {
        return PaymentRequestDto(
            id = paymentRequestAvroModel.id,
            customerId = paymentRequestAvroModel.customerId,
            price = paymentRequestAvroModel.price,
            createdAt = LocalDateTime.ofEpochSecond(paymentRequestAvroModel.createdAt, 0, ZoneOffset.UTC),
            paymentOrderStatus = PaymentOrderStatus.valueOf(paymentRequestAvroModel.paymentOrderStatus.name)
        )
    }
}