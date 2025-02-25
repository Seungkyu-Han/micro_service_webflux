package seungkyu.msa.service.order.persistence.outbox.payment.adapter

import org.bson.types.ObjectId
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.order.persistence.outbox.payment.mapper.PaymentOutboxDataAccessMapper
import seungkyu.msa.service.order.persistence.outbox.payment.repository.PaymentOutboxMongoRepository
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentOutboxMessage
import seungkyu.msa.service.order.service.ports.output.repository.PaymentOutboxRepository
import seungkyu.msa.service.outbox.OutboxStatus

@Repository
class PaymentOutboxRepositoryImpl(
    private val paymentOutboxMongoRepository: PaymentOutboxMongoRepository,
    private val paymentOutboxDataAccessMapper: PaymentOutboxDataAccessMapper
): PaymentOutboxRepository {

    override fun save(paymentOutboxMessage: PaymentOutboxMessage): Mono<PaymentOutboxMessage> {
        return paymentOutboxMongoRepository.save(
            paymentOutboxDataAccessMapper.paymentOutboxMessageToPaymentOutboxEntity(
                paymentOutboxMessage
            )
        ).map(paymentOutboxDataAccessMapper::paymentOutboxEntityToPaymentOutboxMessage)
    }

    override fun findByTypeAndOutboxStatusAndOrderStatus(
        type: String,
        outboxStatus: OutboxStatus,
        orderStatuses: List<OrderStatus>
    ): Flux<PaymentOutboxMessage> {
        return paymentOutboxMongoRepository.findByTypeAndOutboxStatusAndOrderStatusIn(
            type, outboxStatus, orderStatuses
        ).map(paymentOutboxDataAccessMapper::paymentOutboxEntityToPaymentOutboxMessage)
    }

    override fun findByTypeAndIdAndOrderStatus(
        type: String,
        id: ObjectId,
        orderStatuses: List<OrderStatus>
    ): Mono<PaymentOutboxMessage> {
        return paymentOutboxMongoRepository.findByTypeAndIdAndOrderStatusIn(
            type, id, orderStatuses
        ).map(paymentOutboxDataAccessMapper::paymentOutboxEntityToPaymentOutboxMessage)
    }

    override fun deleteByTypeAndOutboxStatusAndOrderStatus(
        type: String,
        outboxStatus: OutboxStatus,
        orderStatuses: List<OrderStatus>
    ): Mono<Void> {
        return paymentOutboxMongoRepository.deleteByTypeAndOutboxStatusAndOrderStatusIn(
            type, outboxStatus, orderStatuses
        )
    }
}