package seungkyu.msa.service.payment.service.ports.output.repository

import org.bson.types.ObjectId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.payment.service.outbox.model.PaymentOutboxMessage
import seungkyu.msa.service.saga.SagaConstants.Companion.ORDER_SAGA_NAME

interface PaymentOutboxRepository {

    fun save(paymentOutboxMessage: PaymentOutboxMessage): Mono<PaymentOutboxMessage>

    fun findByTypeAndOutboxStatus(
        type: String = ORDER_SAGA_NAME,
        outboxStatus: OutboxStatus): Flux<PaymentOutboxMessage>


    fun findByTypeAndIdAndOrderStatus(
        type: String = ORDER_SAGA_NAME,
        id: ObjectId,
        orderStatus: OrderStatus
    ): Mono<PaymentOutboxMessage>

    fun deleteByTypeAndOutboxStatus(type: String = ORDER_SAGA_NAME,
                                    outboxStatus: OutboxStatus): Mono<Void>
}