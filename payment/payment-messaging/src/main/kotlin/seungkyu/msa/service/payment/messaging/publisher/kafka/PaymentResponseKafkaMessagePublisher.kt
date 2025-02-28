package seungkyu.msa.service.payment.messaging.publisher.kafka

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import seungkyu.msa.service.kafka.model.PaymentResponseAvroModel
import seungkyu.msa.service.kafka.model.PaymentStatus
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.payment.service.outbox.model.PaymentOutboxMessage
import seungkyu.msa.service.payment.service.ports.output.message.publisher.PaymentResponseMessagePublisher
import java.time.ZoneOffset

@Component
class PaymentResponseKafkaMessagePublisher(
    private val reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, PaymentResponseAvroModel>,
    @Value("\${kafka.topic.payment-response}")
    private val paymentResponseTopic: String,
): PaymentResponseMessagePublisher {

    private val logger = LoggerFactory.getLogger(PaymentResponseKafkaMessagePublisher::class.java)

    override fun publish(
        paymentOutboxMessage: PaymentOutboxMessage,
        callback: (PaymentOutboxMessage, OutboxStatus) -> Mono<Void>
    ): Mono<Void> {
        return reactiveKafkaProducerTemplate.send(
            paymentResponseTopic,
            paymentOutboxMessage.id.toString(),
            paymentEventToPaymentResponseAvroModel(paymentOutboxMessage)
        ).publishOn(Schedulers.boundedElastic()).doOnNext {
            callback(paymentOutboxMessage, OutboxStatus.COMPLETED).subscribe()
        }.doOnError{
            callback(paymentOutboxMessage, OutboxStatus.FAILED).subscribe()
        }.doFinally {
            logger.info("주문 {}의 결제 이벤트 전송했니다.", paymentOutboxMessage.id)
        }.then()
    }

    private fun paymentEventToPaymentResponseAvroModel(
        paymentOutboxMessage: PaymentOutboxMessage
    ): PaymentResponseAvroModel {
        return PaymentResponseAvroModel.newBuilder()
            .setId(paymentOutboxMessage.id.toString())
            .setCustomerId(paymentOutboxMessage.payload.customerId.toString())
            .setPrice(paymentOutboxMessage.payload.price)
            .setCreatedAt(paymentOutboxMessage.createdAt.toEpochSecond(ZoneOffset.UTC))
            .setPaymentStatus(PaymentStatus.valueOf(paymentOutboxMessage.payload.paymentStatus.name))
            .build()
    }
}