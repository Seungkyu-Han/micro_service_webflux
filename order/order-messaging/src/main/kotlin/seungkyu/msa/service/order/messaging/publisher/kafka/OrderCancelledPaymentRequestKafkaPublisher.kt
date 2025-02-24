package seungkyu.msa.service.order.messaging.publisher.kafka

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import seungkyu.msa.service.kafka.model.PaymentOrderStatus
import seungkyu.msa.service.kafka.model.PaymentRequestAvroModel
import seungkyu.msa.service.order.domain.event.OrderCancelledEvent
import seungkyu.msa.service.order.service.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher
import java.time.ZoneOffset

@Component
class OrderCancelledPaymentRequestKafkaPublisher(
    private val reactiveKafkaProducer: ReactiveKafkaProducerTemplate<String, PaymentRequestAvroModel>,
    @Value("\${kafka.topic.payment-request}")
    private val paymentRequestTopic: String
) : OrderCancelledPaymentRequestMessagePublisher{

    private val logger = LoggerFactory.getLogger(OrderCancelledPaymentRequestKafkaPublisher::class.java)

    override fun publish(domainEvent: OrderCancelledEvent) {
        logger.info("{} 주문에 대한 결제 취소 요청을 전송하겠습니다", domainEvent.order.orderId.id)

        val paymentRequestAvroModel = orderCreatedEventToPaymentRequestAvroModel(domainEvent)

        reactiveKafkaProducer.send(
            paymentRequestTopic,
            domainEvent.order.orderId.id.toString(),
            paymentRequestAvroModel,
        ).subscribe()
    }

    private fun orderCreatedEventToPaymentRequestAvroModel(
        orderCancelledEvent: OrderCancelledEvent
    ): PaymentRequestAvroModel {
        println(orderCancelledEvent)
        return PaymentRequestAvroModel.newBuilder()
            .setId(orderCancelledEvent.order.orderId.id.toString())
            .setCustomerId(orderCancelledEvent.order.customerId.id.toString())
            .setPrice(orderCancelledEvent.order.price.amount)
            .setPaymentOrderStatus(
                PaymentOrderStatus.valueOf(orderCancelledEvent.order.orderStatus.name)
            )
            .setCreatedAt(orderCancelledEvent.createdAt.toEpochSecond(ZoneOffset.UTC))
            .build()
    }
}