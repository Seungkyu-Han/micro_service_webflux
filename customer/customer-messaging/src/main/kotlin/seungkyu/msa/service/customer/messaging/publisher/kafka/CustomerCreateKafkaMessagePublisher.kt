package seungkyu.msa.service.customer.messaging.publisher.kafka

import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import seungkyu.msa.service.customer.domain.event.CustomerCreatedEvent
import seungkyu.msa.service.customer.service.ports.output.message.publisher.CustomerMessagePublisher

@Component
class CustomerCreateKafkaMessagePublisher(
    private val reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, String>,
    @Value("\${kafka.topic.customer-create}")
    private val customerCreateTopic: String
): CustomerMessagePublisher{

    override fun publish(customerCreatedEvent: CustomerCreatedEvent): Mono<Void> {
        val customerId = customerCreatedEvent.customer.id.id.toString()

        return reactiveKafkaProducerTemplate.send(
            customerCreateTopic,
            customerId, customerId).then()
    }
}