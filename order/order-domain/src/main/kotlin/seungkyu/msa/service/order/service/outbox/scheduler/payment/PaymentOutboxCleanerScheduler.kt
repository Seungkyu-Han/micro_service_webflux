package seungkyu.msa.service.order.service.outbox.scheduler.payment

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.outbox.OutboxScheduler
import seungkyu.msa.service.outbox.OutboxStatus

@Component
class PaymentOutboxCleanerScheduler(
    private val paymentOutboxHelper: PaymentOutboxHelper
): OutboxScheduler {

    private val logger = LoggerFactory.getLogger(PaymentOutboxCleanerScheduler::class.java)

    @Scheduled(cron = "@midnight")
    override fun processOutboxMessages() {
        paymentOutboxHelper.deletePaymentOutboxMessageByOutboxStatusAndOrderStatus(
            OutboxStatus.COMPLETED,
            listOf(OrderStatus.CANCELLED, OrderStatus.APPROVED)
        ).subscribe()

        logger.info("PaymentOutboxMessage가 청소되었습니다.")
    }
}