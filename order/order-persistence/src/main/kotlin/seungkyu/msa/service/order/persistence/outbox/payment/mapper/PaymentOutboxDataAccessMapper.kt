package seungkyu.msa.service.order.persistence.outbox.payment.mapper

import org.springframework.stereotype.Component
import seungkyu.msa.service.order.persistence.outbox.payment.entity.PaymentEventPayloadEntity
import seungkyu.msa.service.order.persistence.outbox.payment.entity.PaymentOutboxEntity
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentEventPayload
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentOutboxMessage

@Component
class PaymentOutboxDataAccessMapper {

    fun paymentOutboxMessageToPaymentOutboxEntity(payOutboxMessage: PaymentOutboxMessage): PaymentOutboxEntity {
        println("paymentOutboxMessageToPaymentOutboxEntity ${payOutboxMessage.payload.orderStatus}")
        return PaymentOutboxEntity(
            id = payOutboxMessage.id,
            createdAt = payOutboxMessage.createdAt,
            type = payOutboxMessage.type,
            payload = PaymentEventPayloadEntity(
                orderId = payOutboxMessage.payload.orderId,
                createdAt = payOutboxMessage.payload.createdAt,
                customerId = payOutboxMessage.payload.customerId,
                paymentOrderStatus = payOutboxMessage.payload.paymentOrderStatus,
                price = payOutboxMessage.payload.price,
            ),
            orderStatus = payOutboxMessage.payload.orderStatus,
            outboxStatus = payOutboxMessage.outboxStatus,
            version = payOutboxMessage.version,
            processedAt = payOutboxMessage.processedAt
        )
    }

    fun paymentOutboxEntityToPaymentOutboxMessage(paymentOutboxEntity: PaymentOutboxEntity): PaymentOutboxMessage {
        println("paymentOutboxEntityToPaymentOutboxMessage ${paymentOutboxEntity.orderStatus}")
        return PaymentOutboxMessage(
            id = paymentOutboxEntity.id,
            createdAt = paymentOutboxEntity.createdAt,
            type = paymentOutboxEntity.type,
            payload = PaymentEventPayload(
                orderId = paymentOutboxEntity.payload.orderId,
                createdAt = paymentOutboxEntity.payload.createdAt,
                customerId = paymentOutboxEntity.payload.customerId,
                paymentOrderStatus = paymentOutboxEntity.payload.paymentOrderStatus,
                price = paymentOutboxEntity.payload.price,
                orderStatus = paymentOutboxEntity.orderStatus,
            ),
            processedAt = paymentOutboxEntity.processedAt,
            outboxStatus = paymentOutboxEntity.outboxStatus,
            version = paymentOutboxEntity.version,
        )
    }
}