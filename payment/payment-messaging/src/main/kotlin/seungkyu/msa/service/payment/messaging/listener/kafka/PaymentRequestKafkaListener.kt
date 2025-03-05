package seungkyu.msa.service.payment.messaging.listener.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import seungkyu.msa.service.common.status.PaymentOrderStatus
import seungkyu.msa.service.payment.service.dto.PaymentRequestDto
import seungkyu.msa.service.payment.service.ports.input.message.listener.PaymentRequestMessageListener
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class PaymentRequestKafkaListener(
    private val paymentRequestMessageListener: PaymentRequestMessageListener,
    private val objectMapper: ObjectMapper){

    private val logger = LoggerFactory.getLogger(PaymentRequestKafkaListener::class.java)

    @KafkaListener(id = "\${kafka.consumer.payment-consumer-group-id}",
        topics = ["\${kafka.topic.payment-request}"])
    fun receive(
        @Payload value: String,
        @Header(KafkaHeaders.RECEIVED_KEY) keys: String,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partitions: Int,
        @Header(KafkaHeaders.OFFSET) offsets: Long
    ) {

        val cdcJson = objectMapper.readTree(value)

        if(cdcJson["payload"]["op"].asText() == "c"){

            val paymentRequestJson = objectMapper.readTree(cdcJson["payload"]["after"].asText())

            val paymentRequestDto = PaymentRequestDto(
                id = paymentRequestJson["_id"]["\$oid"].asText(),
                customerId = paymentRequestJson["payload"]["customerId"]["\$oid"].asText(),
                price = paymentRequestJson["payload"]["price"]["\$numberLong"].asLong(),
                createdAt= LocalDateTime.ofEpochSecond(paymentRequestJson["createdAt"]["\$date"].asLong() / 1000, 0, ZoneOffset.UTC),
                paymentOrderStatus = PaymentOrderStatus.valueOf(paymentRequestJson["payload"]["paymentOrderStatus"].asText()),
            )

            logger.info("paymentRequestDto $paymentRequestDto")

            if(paymentRequestDto.paymentOrderStatus == PaymentOrderStatus.PENDING){
                logger.info("주문 {}의 결제가 진행 중입니다", paymentRequestDto.id)
                paymentRequestMessageListener.completePayment(paymentRequestDto)
            }else{
                logger.info("주문 {}의 결제가 취소 중입니다", paymentRequestDto.id)
                paymentRequestMessageListener.cancelPayment(paymentRequestDto)
            }.subscribe()
        }
    }

}