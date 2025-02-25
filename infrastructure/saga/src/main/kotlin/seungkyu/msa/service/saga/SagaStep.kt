package seungkyu.msa.service.saga

import reactor.core.publisher.Mono

interface SagaStep<T> {

    fun process(data: T): Mono<Void>
    fun rollback(data: T): Mono<Void>
}