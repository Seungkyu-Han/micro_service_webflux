package seungkyu.msa.service.payment.persistence.outbox.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.payment.persistence.outbox.entity.PaymentOutboxEntity

interface PaymentOutboxMongoRepository: ReactiveMongoRepository<PaymentOutboxEntity, ObjectId> {

    fun save(paymentOutboxMessage: PaymentOutboxEntity): Mono<PaymentOutboxEntity>

    fun findByTypeAndOutboxStatus(
        type: String,
        outboxStatus: OutboxStatus
    ): Flux<PaymentOutboxEntity>


    fun findByTypeAndIdAndOrderStatus(
        type: String,
        id: ObjectId,
        orderStatus: OrderStatus
    ): Mono<PaymentOutboxEntity>

    fun deleteByTypeAndOutboxStatus(type: String,
                                    outboxStatus: OutboxStatus): Mono<Void>
}