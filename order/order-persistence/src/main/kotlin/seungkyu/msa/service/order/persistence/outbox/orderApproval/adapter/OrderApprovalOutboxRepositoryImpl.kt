package seungkyu.msa.service.order.persistence.outbox.orderApproval.adapter

import org.bson.types.ObjectId
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.order.persistence.outbox.orderApproval.mapper.OrderApprovalOutboxDataAccessMapper
import seungkyu.msa.service.order.persistence.outbox.orderApproval.repository.OrderApprovalOutboxMongoRepository
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalOutboxMessage
import seungkyu.msa.service.order.service.ports.output.repository.OrderApprovalOutboxRepository
import seungkyu.msa.service.outbox.OutboxStatus

@Repository
class OrderApprovalOutboxRepositoryImpl(
    private val orderApprovalMongoRepository: OrderApprovalOutboxMongoRepository,
    private val orderApprovalOutboxDataAccessMapper: OrderApprovalOutboxDataAccessMapper): OrderApprovalOutboxRepository {

    override fun save(orderApprovalOutboxMessage: OrderApprovalOutboxMessage): Mono<OrderApprovalOutboxMessage> {
        return orderApprovalMongoRepository.save(
            orderApprovalOutboxDataAccessMapper.orderApprovalOutboxMessageToOrderApprovalOutboxEntity(orderApprovalOutboxMessage)
        ).map(orderApprovalOutboxDataAccessMapper::orderApprovalOutboxEntityToOrderApprovalOutboxMessage)
    }

    override fun findByTypeAndOutboxStatusAndOrderStatus(
        type: String,
        outboxStatus: OutboxStatus,
        orderStatuses: List<OrderStatus>
    ): Flux<OrderApprovalOutboxMessage> {
        return orderApprovalMongoRepository.findByTypeAndOutboxStatusAndOrderStatusIn(
            type, outboxStatus, orderStatuses
        ).map(orderApprovalOutboxDataAccessMapper::orderApprovalOutboxEntityToOrderApprovalOutboxMessage)
    }

    override fun findByIdAndTypeAndOrderStatus(
        type: String,
        id: ObjectId,
        orderStatuses: List<OrderStatus>
    ): Mono<OrderApprovalOutboxMessage> {
        return orderApprovalMongoRepository.findByIdAndTypeAndOrderStatusIn(
            id, type, orderStatuses
        ).map(orderApprovalOutboxDataAccessMapper::orderApprovalOutboxEntityToOrderApprovalOutboxMessage)
    }

    override fun deleteByTypeAndOutboxStatusAndOrderStatus(
        type: String,
        outboxStatus: OutboxStatus,
        orderStatuses: List<OrderStatus>
    ): Mono<Void> {
        return orderApprovalMongoRepository.deleteByTypeAndOutboxStatusAndOrderStatusIn(type, outboxStatus, orderStatuses)
    }
}