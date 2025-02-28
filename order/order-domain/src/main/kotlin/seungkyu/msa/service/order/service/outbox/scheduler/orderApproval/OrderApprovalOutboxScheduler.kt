package seungkyu.msa.service.order.service.outbox.scheduler.orderApproval

import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalOutboxMessage
import seungkyu.msa.service.order.service.ports.output.message.publisher.restaurantApproval.OrderApprovalRequestMessagePublisher
import seungkyu.msa.service.outbox.OutboxScheduler
import seungkyu.msa.service.outbox.OutboxStatus

@Component
class OrderApprovalOutboxScheduler(
    private val orderApprovalOutboxHelper: OrderApprovalOutboxHelper,
    private val restaurantRequestMessagePublisher: OrderApprovalRequestMessagePublisher
): OutboxScheduler {

    private val logger = LoggerFactory.getLogger(OrderApprovalOutboxScheduler::class.java)

    @Transactional
    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    override fun processOutboxMessages() {
        logger.info("승인 요청을 보내는 스케줄러가 동작합니다.")
        mono{
            orderApprovalOutboxHelper.getApprovalOutboxMessageByOutboxStatusAndOrderStatus(OutboxStatus.STARTED, listOf(OrderStatus.PAID)
            ).flatMap{
                orderApprovalOutboxMessage ->
                logger.info("스케줄러에 의해 {} 주문의 승인 요청을 전송려고 합니다.", orderApprovalOutboxMessage.id)
                restaurantRequestMessagePublisher.publish(
                    orderApprovalOutboxMessage, ::updateOutboxStatus
                ).doOnSuccess {
                    logger.info("스케줄러에 의해 {} 주문의 승인 요청을 전송했습니다.", orderApprovalOutboxMessage.id)
                }
            }.subscribe()
        }.subscribe()
    }

    private fun updateOutboxStatus(orderApprovalOutboxMessage: OrderApprovalOutboxMessage, outboxStatus: OutboxStatus): Mono<Void> {
        orderApprovalOutboxMessage.outboxStatus = outboxStatus
        return orderApprovalOutboxHelper.save(orderApprovalOutboxMessage).then()
    }
}