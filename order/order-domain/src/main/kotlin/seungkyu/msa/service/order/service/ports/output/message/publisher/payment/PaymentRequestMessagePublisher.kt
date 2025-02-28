package seungkyu.msa.service.order.service.ports.output.message.publisher.payment

import reactor.core.publisher.Mono
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentOutboxMessage
import seungkyu.msa.service.outbox.OutboxStatus

interface PaymentRequestMessagePublisher {

    fun publish(paymentOutboxMessage: PaymentOutboxMessage,
                callback: (PaymentOutboxMessage, OutboxStatus) -> Mono<Void>): Mono<Void>
}