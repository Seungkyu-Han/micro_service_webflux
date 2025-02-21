package seungkyu.msa.service.order.messaging.publisher.kafka

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import seungkyu.msa.service.kafka.model.PaymentOrderStatus
import seungkyu.msa.service.kafka.model.PaymentRequestAvroModel
import seungkyu.msa.service.order.domain.event.OrderCreatedEvent
import seungkyu.msa.service.order.service.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher
import java.time.ZoneOffset

@Component
class OrderCreatedPaymentRequestKafkaPublisher(
    private val reactiveKafkaProducer: ReactiveKafkaProducerTemplate<String, PaymentRequestAvroModel>,
    @Value("\${kafka.topic.payment-request}")
    private val paymentRequestTopic: String,
): OrderCreatedPaymentRequestMessagePublisher {

    private val logger = LoggerFactory.getLogger(OrderCreatedPaymentRequestKafkaPublisher::class.java)

    override fun publish(domainEvent: OrderCreatedEvent) {
        logger.info("{} 주문에 대한 결제 요청을 전송하겠습니다", domainEvent.order.orderId.id)

        val paymentRequestAvroModel = orderCreatedEventToPaymentRequestAvroModel(domainEvent)

        reactiveKafkaProducer.send(
            paymentRequestTopic,
            domainEvent.order.orderId.id.toString(),
            paymentRequestAvroModel,
        ).subscribe()
    }

    private fun orderCreatedEventToPaymentRequestAvroModel(
        orderCreatedEvent: OrderCreatedEvent): PaymentRequestAvroModel {
        return PaymentRequestAvroModel.newBuilder()
            .setId(orderCreatedEvent.order.orderId.id.toString())
            .setCustomerId(orderCreatedEvent.order.customerId.id.toString())
            .setPrice(orderCreatedEvent.order.price.amount)
            .setPaymentOrderStatus(
                PaymentOrderStatus.valueOf(orderCreatedEvent.order.orderStatus.name)
            )
            .setCreatedAt(orderCreatedEvent.createdAt.toEpochSecond(ZoneOffset.UTC))
            .build()
    }
}