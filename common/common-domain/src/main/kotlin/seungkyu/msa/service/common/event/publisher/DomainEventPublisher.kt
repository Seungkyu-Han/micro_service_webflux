package seungkyu.msa.service.common.event.publisher

import seungkyu.msa.service.common.event.DomainEvent

interface DomainEventPublisher<T :DomainEvent<*>> {

    fun publish(domainEvent: T)
}