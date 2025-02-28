package seungkyu.msa.service.payment.service.outbox.scheduler

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.payment.service.outbox.model.PaymentOutboxMessage
import seungkyu.msa.service.payment.service.ports.output.message.publisher.PaymentResponseMessagePublisher
import seungkyu.msa.service.payment.service.ports.output.repository.PaymentOutboxRepository


@Component
class PaymentOutboxScheduler(
    private val paymentOutboxRepository: PaymentOutboxRepository,
    private val paymentResponseMessagePublisher: PaymentResponseMessagePublisher
) {

    private val logger = LoggerFactory.getLogger(PaymentOutboxScheduler::class.java)

    @Transactional
    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    fun processOutboxMessages() {
        logger.info("결제 응답을 전송하는 스케줄러가 동작합니다")
        paymentOutboxRepository.findByTypeAndOutboxStatus(
            outboxStatus = OutboxStatus.STARTED
        ).flatMap{
            paymentOutboxMessage ->

            logger.info("주문 {}에 대한 결제 응답을 전송하려고 합니다", paymentOutboxMessage)

            paymentResponseMessagePublisher.publish(paymentOutboxMessage, ::updateOutboxMessage)
                .doOnSuccess{
                    logger.info("주문 {}에 대한 결제 응답이 전송되었습니다", paymentOutboxMessage)
                }
        }.subscribe()
    }

    @Transactional
    fun updateOutboxMessage(paymentOutboxMessage: PaymentOutboxMessage, outboxStatus: OutboxStatus): Mono<Void> {
        paymentOutboxMessage.outboxStatus = outboxStatus
        return paymentOutboxRepository.save(paymentOutboxMessage)
            .doOnNext {
                logger.info("주문 {}의 outbox 상태가 {}로 변경되었습니다", paymentOutboxMessage.id, paymentOutboxMessage.outboxStatus.toString())
            }
            .then()
    }

}