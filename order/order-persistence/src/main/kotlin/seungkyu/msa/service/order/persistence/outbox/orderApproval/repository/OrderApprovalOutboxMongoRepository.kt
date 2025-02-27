package seungkyu.msa.service.order.persistence.outbox.orderApproval.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.order.persistence.outbox.orderApproval.entity.OrderApprovalOutboxEntity

interface OrderApprovalOutboxMongoRepository: MongoRepository<OrderApprovalOutboxEntity, ObjectId> {

    fun findByTypeAndOutboxStatusAndOrderStatusIn(
        type: String, outboxStatus: OrderStatus, orderStatus: List<OrderStatus>
    ): Flux<OrderApprovalOutboxEntity>

    fun findByIdAndTypeAndOrderStatus(
        id: ObjectId, type: String, orderStatus: OrderStatus): Mono<OrderApprovalOutboxEntity>

    fun deleteByTypeAndOutboxStatusAndOrderStatusIn(
        type: String, outboxStatus: OrderStatus, orderStatus: List<OrderStatus>): Mono<Void>
}