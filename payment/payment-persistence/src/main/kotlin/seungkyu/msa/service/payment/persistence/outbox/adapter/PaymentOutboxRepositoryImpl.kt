package seungkyu.msa.service.payment.persistence.outbox.adapter

import org.bson.types.ObjectId
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.payment.persistence.outbox.entity.PaymentOutboxEntity
import seungkyu.msa.service.payment.persistence.outbox.entity.PaymentOutboxPayloadEntity
import seungkyu.msa.service.payment.persistence.outbox.repository.PaymentOutboxMongoRepository
import seungkyu.msa.service.payment.service.outbox.model.PaymentEventPayload
import seungkyu.msa.service.payment.service.outbox.model.PaymentOutboxMessage
import seungkyu.msa.service.payment.service.ports.output.repository.PaymentOutboxRepository

@Repository
class PaymentOutboxRepositoryImpl(
    private val paymentOutboxMongoRepository: PaymentOutboxMongoRepository
): PaymentOutboxRepository {

    override fun save(paymentOutboxMessage: PaymentOutboxMessage): Mono<PaymentOutboxMessage> {
        return paymentOutboxMongoRepository.save(
            paymentOutboxMessageToPaymentOutboxEntity(paymentOutboxMessage)
        ).map(::paymentOutboxEntityToPaymentOutboxMessage)
    }

    override fun findByTypeAndOutboxStatus(type: String, outboxStatus: OutboxStatus): Flux<PaymentOutboxMessage> {
        return paymentOutboxMongoRepository.findByTypeAndOutboxStatus(
            type = type, outboxStatus = outboxStatus
        ).map(::paymentOutboxEntityToPaymentOutboxMessage)
    }

    override fun findByTypeAndIdAndOrderStatus(
        type: String,
        id: ObjectId,
        orderStatus: OrderStatus
    ): Mono<PaymentOutboxMessage> {
        return paymentOutboxMongoRepository.findByTypeAndIdAndOrderStatus(
            type = type, id = id, orderStatus = orderStatus
        ).map(::paymentOutboxEntityToPaymentOutboxMessage)
    }

    override fun deleteByTypeAndOutboxStatus(type: String, outboxStatus: OutboxStatus): Mono<Void> {
        return paymentOutboxMongoRepository.deleteByTypeAndOutboxStatus(type, outboxStatus).then()
    }

    private fun paymentOutboxMessageToPaymentOutboxEntity(paymentOutboxMessage: PaymentOutboxMessage): PaymentOutboxEntity {
        return PaymentOutboxEntity(
            id = paymentOutboxMessage.id,
            createdAt = paymentOutboxMessage.createdAt,
            processedAt = paymentOutboxMessage.processedAt,
            type = paymentOutboxMessage.type,
            payload = PaymentOutboxPayloadEntity(
                orderId = paymentOutboxMessage.id,
                customerId = paymentOutboxMessage.payload.customerId,
                price = paymentOutboxMessage.payload.price,
                createdAt = paymentOutboxMessage.payload.createdAt,
                paymentOrderStatus = paymentOutboxMessage.payload.paymentOrderStatus,
                paymentStatus = paymentOutboxMessage.payload.paymentStatus,
            ),
            outboxStatus = paymentOutboxMessage.outboxStatus,
            orderStatus = paymentOutboxMessage.payload.orderStatus,
            version = paymentOutboxMessage.version,
        )
    }

    private fun paymentOutboxEntityToPaymentOutboxMessage(paymentOutboxEntity: PaymentOutboxEntity): PaymentOutboxMessage {
        return PaymentOutboxMessage(
            id = paymentOutboxEntity.id,
            createdAt = paymentOutboxEntity.createdAt,
            processedAt = paymentOutboxEntity.processedAt,
            type = paymentOutboxEntity.type,
            payload = PaymentEventPayload(
                orderId = paymentOutboxEntity.id,
                customerId = paymentOutboxEntity.payload.customerId,
                price = paymentOutboxEntity.payload.price,
                orderStatus = paymentOutboxEntity.orderStatus,
                paymentStatus = paymentOutboxEntity.payload.paymentStatus,
                createdAt = paymentOutboxEntity.createdAt,
                paymentOrderStatus = paymentOutboxEntity.payload.paymentOrderStatus,
            ),
            version = paymentOutboxEntity.version,
            outboxStatus = paymentOutboxEntity.outboxStatus
        )
    }
}