package seungkyu.msa.service.customer.service.ports.output.message.publisher

import reactor.core.publisher.Mono
import seungkyu.msa.service.customer.domain.event.CustomerCreatedEvent

interface CustomerMessagePublisher {

    fun publish(customerCreatedEvent: CustomerCreatedEvent): Mono<Void>
}