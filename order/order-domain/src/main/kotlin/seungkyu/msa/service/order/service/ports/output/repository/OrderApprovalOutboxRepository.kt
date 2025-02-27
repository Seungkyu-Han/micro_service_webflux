package seungkyu.msa.service.order.service.ports.output.repository

import org.bson.types.ObjectId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalOutboxMessage
import seungkyu.msa.service.outbox.OutboxStatus

interface OrderApprovalOutboxRepository {

    fun save(orderApprovalOutboxMessage: OrderApprovalOutboxMessage): Mono<OrderApprovalOutboxMessage>

    fun findByTypeAndOutboxStatusAndOrderStatus(type: String,
                                                outboxStatus: OutboxStatus,
                                                orderStatuses: List<OrderStatus>): Flux<OrderApprovalOutboxMessage>

    fun findByIdAndTypeAndOrderStatus(type: String,
                                      id: ObjectId,
                                      orderStatuses: List<OrderStatus>): Mono<OrderApprovalOutboxMessage>

    fun deleteByTypeAndOutboxStatusAndOrderStatus(type: String,
                                                  outboxStatus: OutboxStatus,
                                                  orderStatuses: List<OrderStatus>): Mono<Void>
}