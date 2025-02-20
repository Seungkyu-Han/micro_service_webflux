package seungkyu.msa.service.payment.messaging.publisher.kafka

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import seungkyu.msa.service.kafka.model.PaymentCancelledResponseAvroModel
import seungkyu.msa.service.kafka.model.PaymentCompletedResponseAvroModel
import seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel
import seungkyu.msa.service.payment.domain.event.PaymentEvent
import seungkyu.msa.service.payment.service.ports.output.message.publisher.PaymentCompletedMessagePublisher
import java.time.ZoneOffset

@Component
class PaymentCancelledKafkaMessagePublisher(
    private val reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, PaymentCancelledResponseAvroModel>,
    @Value("\${kafka.topic.payment-cancelled-response}")
    private val paymentCancelledResponseTopic: String,
): PaymentCompletedMessagePublisher {

    private val logger = LoggerFactory.getLogger(PaymentCancelledKafkaMessagePublisher::class.java)

    override fun publish(domainEvent: PaymentEvent) {

        logger.info("주문: {}의 결제 실패 이벤트를 전송하겠습니다", domainEvent.payment.orderId.id.toString())

        val paymentCompletedResponseAvroModel = paymentFailedEventToPaymentCancelledResponseAvroModel(domainEvent)
        
        reactiveKafkaProducerTemplate.send(
            paymentCancelledResponseTopic,
            domainEvent.payment.orderId.id.toString(),
            paymentCompletedResponseAvroModel
        ).subscribe()
    }

    private fun paymentFailedEventToPaymentCancelledResponseAvroModel(
        paymentEvent: PaymentEvent
    ): PaymentCancelledResponseAvroModel {
        return PaymentCancelledResponseAvroModel.newBuilder()
            .setPaymentId(paymentEvent.payment.id.id.toString())
            .setCustomerId(paymentEvent.payment.customerId.id.toString())
            .setOrderId(paymentEvent.payment.orderId.id.toString())
            .setPrice(paymentEvent.payment.price.amount)
            .setCreatedAt(paymentEvent.createdAt.toEpochSecond(ZoneOffset.UTC))
            .setFailureMessages(paymentEvent.failureMessages)
            .build()
    }
}