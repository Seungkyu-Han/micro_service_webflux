package seungkyu.msa.service.saga

import seungkyu.msa.service.common.event.DomainEvent

interface SagaStep<T, SuccessEvent: DomainEvent<*>, FailEvent: DomainEvent<*>> {

    fun process(data: T): SuccessEvent
    fun rollback(data: T): FailEvent
}