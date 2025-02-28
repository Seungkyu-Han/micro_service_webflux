package seungkyu.msa.service.payment.service.outbox.scheduler

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import seungkyu.msa.service.outbox.OutboxScheduler
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.payment.service.ports.output.repository.PaymentOutboxRepository

@Component
class PaymentOutboxCleanerScheduler(
    private val paymentOutboxRepository: PaymentOutboxRepository
): OutboxScheduler{

    private val logger = LoggerFactory.getLogger(PaymentOutboxCleanerScheduler::class.java)

    @Transactional
    @Scheduled(cron = "@midnight")
    override fun processOutboxMessages() {
        paymentOutboxRepository.deleteByTypeAndOutboxStatus(
            outboxStatus = OutboxStatus.COMPLETED
        ).subscribe()

        logger.info("PaymentOutboxMessage가 청소되었습니다.")
    }
}