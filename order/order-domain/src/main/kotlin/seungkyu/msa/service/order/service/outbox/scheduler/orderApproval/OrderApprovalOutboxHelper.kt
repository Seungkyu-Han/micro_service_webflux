package seungkyu.msa.service.order.service.outbox.scheduler.orderApproval

import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalOutboxMessage
import seungkyu.msa.service.order.service.ports.output.repository.OrderApprovalOutboxRepository
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.saga.SagaConstants.Companion.ORDER_SAGA_NAME

@Component
class OrderApprovalOutboxHelper(
    private val orderApprovalOutboxRepository: OrderApprovalOutboxRepository
) {

    @Transactional(readOnly = true)
    fun getApprovalOutboxMessageByOutboxStatusAndOrderStatus(
        outboxStatus: OutboxStatus,
        orderStatuses: List<OrderStatus>): Flux<OrderApprovalOutboxMessage> {
        return orderApprovalOutboxRepository.findByTypeAndOutboxStatusAndOrderStatus(
            ORDER_SAGA_NAME, outboxStatus, orderStatuses)
    }

    @Transactional(readOnly = true)
    fun getApprovalOutboxMessageByIdAndOrderStatus(
        id: ObjectId, orderStatuses: List<OrderStatus>): Mono<OrderApprovalOutboxMessage>{
        return orderApprovalOutboxRepository.findByIdAndTypeAndOrderStatus(
            ORDER_SAGA_NAME, id, orderStatuses)
    }

    @Transactional
    fun save(orderApprovalOutboxMessage: OrderApprovalOutboxMessage): Mono<OrderApprovalOutboxMessage> {
        return orderApprovalOutboxRepository.save(orderApprovalOutboxMessage)
    }

    @Transactional
    fun deleteOrderApprovalOutboxMessageByOutboxStatusAndOrderStatus(
        outboxStatus: OutboxStatus,
        orderStatuses: List<OrderStatus>
    ): Mono<Void>{
        return orderApprovalOutboxRepository.deleteByTypeAndOutboxStatusAndOrderStatus(
            ORDER_SAGA_NAME, outboxStatus, orderStatuses
        )
    }

}