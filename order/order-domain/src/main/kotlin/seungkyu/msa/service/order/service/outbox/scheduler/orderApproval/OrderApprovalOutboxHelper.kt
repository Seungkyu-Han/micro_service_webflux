package seungkyu.msa.service.order.service.outbox.scheduler.orderApproval

import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
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

    private val logger = LoggerFactory.getLogger(OrderApprovalOutboxHelper::class.java)

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
        return orderApprovalOutboxRepository.findByTypeIdAndOrderStatus(
            ORDER_SAGA_NAME, id, orderStatuses)
    }

    @Transactional
    fun save(orderApprovalOutboxMessage: OrderApprovalOutboxMessage): Mono<OrderApprovalOutboxMessage> {
        return orderApprovalOutboxRepository.save(orderApprovalOutboxMessage)
            .doOnNext{
                logger.info("주문 승인 outbox message {}가 저장되었습니다", orderApprovalOutboxMessage.id.toString())
            }
    }

    @Transactional
    fun deleteOrderApprovalOutboxMessageByOutboxStatusAndOrderStatus(
        outboxStatus: OutboxStatus,
        orderStatuses: List<OrderStatus>
    ): Mono<Void>{
        return orderApprovalOutboxRepository.deleteByTypeAndOrderStatus(
            ORDER_SAGA_NAME, outboxStatus, orderStatuses
        )
    }

}