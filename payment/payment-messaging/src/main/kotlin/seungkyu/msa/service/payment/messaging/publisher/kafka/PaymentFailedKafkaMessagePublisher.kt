package seungkyu.msa.service.payment.messaging.publisher.kafka

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel
import seungkyu.msa.service.payment.domain.event.PaymentEvent
import seungkyu.msa.service.payment.service.ports.output.message.publisher.PaymentFailedMessagePublisher
import java.time.ZoneOffset

@Component
class PaymentFailedKafkaMessagePublisher(
    private val reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, PaymentFailedResponseAvroModel>,
    @Value("\${kafka.topic.payment-failed-response}")
    private val paymentFailedResponseTopic: String,
): PaymentFailedMessagePublisher {

    private val logger = LoggerFactory.getLogger(PaymentFailedKafkaMessagePublisher::class.java)

    override fun publish(domainEvent: PaymentEvent) {

        logger.info("주문: {}의 결제 실패 이벤트를 전송하겠습니다", domainEvent.payment.orderId.id.toString())

        val paymentCompletedResponseAvroModel = paymentFailedEventToPaymentFailedResponseAvroModel(domainEvent)
        
        reactiveKafkaProducerTemplate.send(
            paymentFailedResponseTopic,
            domainEvent.payment.orderId.id.toString(),
            paymentCompletedResponseAvroModel
        ).subscribe()
    }

    private fun paymentFailedEventToPaymentFailedResponseAvroModel(
        paymentEvent: PaymentEvent
    ): PaymentFailedResponseAvroModel {
        return PaymentFailedResponseAvroModel.newBuilder()
            .setPaymentId(paymentEvent.payment.id.id.toString())
            .setCustomerId(paymentEvent.payment.customerId.id.toString())
            .setOrderId(paymentEvent.payment.orderId.id.toString())
            .setPrice(paymentEvent.payment.price.amount)
            .setCreatedAt(paymentEvent.createdAt.toEpochSecond(ZoneOffset.UTC))
            .setFailureMessages(paymentEvent.failureMessages)
            .build()
    }
}