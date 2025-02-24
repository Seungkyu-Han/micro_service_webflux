package seungkyu.msa.service.saga

import reactor.core.publisher.Mono
import seungkyu.msa.service.common.event.DomainEvent

interface SagaStep<T, SuccessEvent: DomainEvent<*>, FailEvent: DomainEvent<*>> {

    fun process(data: T): Mono<SuccessEvent>
    fun rollback(data: T): Mono<FailEvent>
}