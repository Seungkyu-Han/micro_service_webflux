package seungkyu.msa.service.payment.messaging.publisher.kafka

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import seungkyu.msa.service.kafka.model.PaymentCompletedResponseAvroModel
import seungkyu.msa.service.payment.domain.event.PaymentEvent
import seungkyu.msa.service.payment.service.ports.output.message.publisher.PaymentCompletedMessagePublisher
import java.time.ZoneOffset

@Component
class PaymentCompletedKafkaMessagePublisher(
    private val reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, PaymentCompletedResponseAvroModel>,
    @Value("\${kafka.topic.payment-completed-response}")
    private val paymentCompletedResponseTopic: String,
): PaymentCompletedMessagePublisher {

    private val logger = LoggerFactory.getLogger(PaymentCompletedKafkaMessagePublisher::class.java)

    override fun publish(domainEvent: PaymentEvent) {

        logger.info("주문: {}의 결제 완료 이벤트를 전송하겠습니다", domainEvent.payment.id.id.toString())

        val paymentCompletedResponseAvroModel = paymentCompletedEventToPaymentCompletedResponseAvroModel(domainEvent)

        reactiveKafkaProducerTemplate.send(
            paymentCompletedResponseTopic,
            domainEvent.payment.id.id.toString(),
            paymentCompletedResponseAvroModel
        ).subscribe()
    }

    private fun paymentCompletedEventToPaymentCompletedResponseAvroModel(
        paymentEvent: PaymentEvent
    ): PaymentCompletedResponseAvroModel {
        return PaymentCompletedResponseAvroModel.newBuilder()
            .setId(paymentEvent.payment.id.id.toString())
            .setCustomerId(paymentEvent.payment.customerId.id.toString())
            .setPrice(paymentEvent.payment.price.amount)
            .setCreatedAt(paymentEvent.createdAt.toEpochSecond(ZoneOffset.UTC))
            .build()
    }
}