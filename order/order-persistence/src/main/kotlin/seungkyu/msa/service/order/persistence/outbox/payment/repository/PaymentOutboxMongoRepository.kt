package seungkyu.msa.service.order.persistence.outbox.payment.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.order.persistence.outbox.payment.entity.PaymentOutboxEntity
import seungkyu.msa.service.outbox.OutboxStatus

interface PaymentOutboxMongoRepository: ReactiveMongoRepository<PaymentOutboxEntity, ObjectId> {

    fun findByTypeAndOutboxStatusAndOrderStatusIn(
        type: String,
        outboxStatus: OutboxStatus,
        orderStatuses: List<OrderStatus>
    ): Flux<PaymentOutboxEntity>

    fun findByTypeAndIdAndOrderStatusIn(
        type: String, id: ObjectId, orderStatus: List<OrderStatus>
    ): Mono<PaymentOutboxEntity>

    fun deleteByTypeAndOutboxStatusAndOrderStatusIn(
        type: String, outboxStatus: OutboxStatus, orderStatus: List<OrderStatus>
    ): Mono<Void>
}