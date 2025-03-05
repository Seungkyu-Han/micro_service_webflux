package seungkyu.msa.service.order.messaging.publisher.kafka

import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import seungkyu.msa.service.kafka.model.PaymentOrderStatus
import seungkyu.msa.service.kafka.model.PaymentRequestAvroModel
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentEventPayload
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentOutboxMessage
import seungkyu.msa.service.order.service.ports.output.message.publisher.payment.PaymentRequestMessagePublisher
import seungkyu.msa.service.outbox.OutboxStatus
import java.time.ZoneOffset

@Component
class PaymentEventKafkaPublisher(
    private val reactiveKafkaProducer: ReactiveKafkaProducerTemplate<String, PaymentRequestAvroModel>,
    @Value("\${kafka.topic.payment-request}")
    private val paymentRequestTopic: String
): PaymentRequestMessagePublisher {

    private val logger = LoggerFactory.getLogger(PaymentEventKafkaPublisher::class.java)

    override fun publish(
        paymentOutboxMessage: PaymentOutboxMessage,
        callback: (PaymentOutboxMessage, OutboxStatus) -> Mono<Void>
    ): Mono<Void> {
        return mono{
//
//            val paymentEventPayload = paymentOutboxMessage.payload
//
//            logger.info("{} 주문에 대한 이벤트 전송을 준비 중입니다.", paymentEventPayload.orderId.toString())
//
//            val paymentRequestAvroModel = paymentEventPayloadToPaymentRequestAvroModel(paymentEventPayload)
//
//            reactiveKafkaProducer.send(
//                paymentRequestTopic,
//                paymentEventPayload.orderId.toString(),
//                paymentRequestAvroModel
//            ).publishOn(Schedulers.boundedElastic()).map{
//                callback(paymentOutboxMessage, OutboxStatus.COMPLETED).subscribe()
//            }.doOnError{
//                callback(paymentOutboxMessage, OutboxStatus.FAILED).subscribe()
//            }.subscribe()
//
//            logger.info("{}의 주문이 메시지 큐로 결제 요청을 위해 전송되었습니다", paymentEventPayload.orderId.toString())

        }.then()
    }

    private fun paymentEventPayloadToPaymentRequestAvroModel(
        paymentEventPayload: PaymentEventPayload
    ): PaymentRequestAvroModel {
        return PaymentRequestAvroModel.newBuilder()
            .setId(paymentEventPayload.orderId.toString())
            .setPaymentOrderStatus(PaymentOrderStatus.valueOf(
                paymentEventPayload.paymentOrderStatus.name
            ))
            .setCustomerId(paymentEventPayload.customerId.toString())
            .setCreatedAt(paymentEventPayload.createdAt.toEpochSecond(ZoneOffset.UTC))
            .setPrice(paymentEventPayload.price)
            .build()
    }
}