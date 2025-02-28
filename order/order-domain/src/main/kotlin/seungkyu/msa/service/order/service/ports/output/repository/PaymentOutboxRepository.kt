package seungkyu.msa.service.order.service.ports.output.repository

import org.bson.types.ObjectId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentOutboxMessage
import seungkyu.msa.service.outbox.OutboxStatus

interface PaymentOutboxRepository {

    fun save(paymentOutboxMessage: PaymentOutboxMessage): Mono<PaymentOutboxMessage>

    fun findByTypeAndOutboxStatusAndOrderStatus(
        type: String,
        outboxStatus: OutboxStatus,
        orderStatuses: List<OrderStatus>): Flux<PaymentOutboxMessage>

    fun findByTypeAndIdAndOrderStatus(
        type: String,
        id: ObjectId,
        orderStatuses: List<OrderStatus>): Mono<PaymentOutboxMessage>

    fun deleteByTypeAndOutboxStatusAndOrderStatus(
        type: String,
        outboxStatus: OutboxStatus,
        orderStatuses: List<OrderStatus>): Mono<Void>
}