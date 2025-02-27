package seungkyu.msa.service.payment.service.ports.output.message.publisher

import reactor.core.publisher.Mono
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.payment.service.outbox.model.PaymentOutboxMessage

interface PaymentResponseMessagePublisher {

    fun publish(paymentOutboxMessage: PaymentOutboxMessage,
                callback: (PaymentOutboxMessage, OutboxStatus) -> Unit): Mono<Void>
}