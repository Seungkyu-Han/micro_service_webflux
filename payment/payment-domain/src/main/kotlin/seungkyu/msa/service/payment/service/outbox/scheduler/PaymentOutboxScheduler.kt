package seungkyu.msa.service.payment.service.outbox.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.payment.service.outbox.model.PaymentOutboxMessage
import seungkyu.msa.service.payment.service.ports.output.message.publisher.PaymentResponseMessagePublisher
import seungkyu.msa.service.payment.service.ports.output.repository.PaymentOutboxRepository


@Component
class PaymentOutboxScheduler(
    private val paymentOutboxRepository: PaymentOutboxRepository,
    private val paymentResponseMessagePublisher: PaymentResponseMessagePublisher
) {

    @Transactional
    @Scheduled(fixedDelay = 5000, initialDelay = 5000)
    fun processOutboxMessages() {
        paymentOutboxRepository.findByTypeAndOutboxStatus(
            outboxStatus = OutboxStatus.STARTED
        ).publishOn(Schedulers.boundedElastic()).map{
            paymentResponseMessagePublisher.publish(it, ::updateOutboxMessage).subscribe()
        }.subscribe()
    }

    @Transactional
    fun updateOutboxMessage(paymentOutboxMessage: PaymentOutboxMessage, outboxStatus: OutboxStatus): Mono<Void> {
        paymentOutboxMessage.outboxStatus = outboxStatus
        return paymentOutboxRepository.save(paymentOutboxMessage)
            .then()
    }
}