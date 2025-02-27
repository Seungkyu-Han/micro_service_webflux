package seungkyu.msa.service.order.persistence.outbox.orderApproval.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.order.persistence.outbox.orderApproval.entity.OrderApprovalOutboxEntity
import seungkyu.msa.service.outbox.OutboxStatus

interface OrderApprovalOutboxMongoRepository: ReactiveMongoRepository<OrderApprovalOutboxEntity, ObjectId> {

    fun findByTypeAndOutboxStatusAndOrderStatusIn(
        type: String, outboxStatus: OutboxStatus, orderStatus: List<OrderStatus>
    ): Flux<OrderApprovalOutboxEntity>

    fun findByIdAndTypeAndOrderStatusIn(
        id: ObjectId, type: String, orderStatus: List<OrderStatus>): Mono<OrderApprovalOutboxEntity>

    fun deleteByTypeAndOutboxStatusAndOrderStatusIn(
        type: String, outboxStatus: OutboxStatus, orderStatus: List<OrderStatus>): Mono<Void>
}