package seungkyu.msa.service.order.service.outbox.scheduler.payment

import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentEventPayload
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentOutboxMessage
import seungkyu.msa.service.order.service.ports.output.repository.PaymentOutboxRepository
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.saga.SagaConstants.Companion.ORDER_SAGA_NAME
import java.time.LocalDateTime


@Component
class PaymentOutboxHelper(
    private val paymentOutboxRepository: PaymentOutboxRepository) {

    @Transactional(readOnly = true)
    fun getPaymentOutboxMessageByOutboxStatusAndOrderStatus(
        outboxStatus: OutboxStatus,
        orderStatuses: List<OrderStatus>): Flux<PaymentOutboxMessage> {
        return paymentOutboxRepository.findByTypeAndOutboxStatusAndOrderStatus(
            ORDER_SAGA_NAME,
            outboxStatus,
            orderStatuses)
    }

    @Transactional(readOnly = true)
    fun getPaymentOutboxMessageByIdAndOrderStatus(
        id: ObjectId,
        orderStatuses: List<OrderStatus>): Mono<PaymentOutboxMessage>{
        return paymentOutboxRepository.findByTypeAndIdAndOrderStatus(ORDER_SAGA_NAME, id, orderStatuses)
    }

    @Transactional
    fun save(paymentOutboxMessage: PaymentOutboxMessage): Mono<PaymentOutboxMessage> {
        return paymentOutboxRepository.save(paymentOutboxMessage)
    }

    @Transactional
    fun savePaymentOutboxMessage(
        paymentOutboxPayload: PaymentEventPayload,
        orderStatus: OrderStatus,
        outboxStatus: OutboxStatus): Mono<Void> {
        return paymentOutboxRepository.save(
            PaymentOutboxMessage(
                id = paymentOutboxPayload.orderId,
                outboxStatus = outboxStatus,
                type = ORDER_SAGA_NAME,
                payload = paymentOutboxPayload,
                version = 0,
                createdAt = paymentOutboxPayload.createdAt,
                processedAt = LocalDateTime.now()
            )
        ).then()
    }

    @Transactional
    fun deletePaymentOutboxMessageByOutboxStatusAndOrderStatus(
        outboxStatus: OutboxStatus,
        orderStatuses: List<OrderStatus>): Mono<Void> {
        return paymentOutboxRepository.deleteByTypeAndOutboxStatusAndOrderStatus(
            type = ORDER_SAGA_NAME,
            outboxStatus = outboxStatus,
            orderStatuses = orderStatuses
        ).then()
    }

}