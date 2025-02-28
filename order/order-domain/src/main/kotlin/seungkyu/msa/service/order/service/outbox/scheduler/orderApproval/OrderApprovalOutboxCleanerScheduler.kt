package seungkyu.msa.service.order.service.outbox.scheduler.orderApproval

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.outbox.OutboxScheduler
import seungkyu.msa.service.outbox.OutboxStatus

@Component
class OrderApprovalOutboxCleanerScheduler(
    private val orderApprovalOutboxHelper: OrderApprovalOutboxHelper
): OutboxScheduler{

    private val logger = LoggerFactory.getLogger(OrderApprovalOutboxCleanerScheduler::class.java)

    @Scheduled(cron = "@midnight")
    override fun processOutboxMessages() {
        orderApprovalOutboxHelper.deleteOrderApprovalOutboxMessageByOutboxStatusAndOrderStatus(
            OutboxStatus.COMPLETED,
            listOf(OrderStatus.APPROVED, OrderStatus.CANCELLED)
        ).subscribe()

        logger.info("clean up 스케줄러에 의해 Order approval outbox 데이터베이스가 정리되었습니다.")
    }
}