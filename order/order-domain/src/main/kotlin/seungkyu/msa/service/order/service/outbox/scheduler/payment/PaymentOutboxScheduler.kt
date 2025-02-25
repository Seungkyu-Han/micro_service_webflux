package seungkyu.msa.service.order.service.outbox.scheduler.payment

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.scheduler.Schedulers
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.order.service.ports.output.message.publisher.payment.PaymentRequestMessagePublisher
import seungkyu.msa.service.outbox.OutboxScheduler
import seungkyu.msa.service.outbox.OutboxStatus

@Component
class PaymentOutboxScheduler(
    private val paymentOutboxHelper: PaymentOutboxHelper,
    private val paymentRequestMessagePublisher: PaymentRequestMessagePublisher
): OutboxScheduler {

    private val logger = LoggerFactory.getLogger(PaymentOutboxScheduler::class.java)

    @Transactional
    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    override fun processOutboxMessages() {
        logger.info("결제를 요청하는 스케줄러가 실행되었습니다.")
        paymentOutboxHelper.getPaymentOutboxMessageByOutboxStatusAndOrderStatus(
            OutboxStatus.STARTED,
            listOf(OrderStatus.PENDING, OrderStatus.CANCELLING)
        ).publishOn(Schedulers.boundedElastic()).map{
            paymentOutboxMessage ->
            paymentRequestMessagePublisher.publish(
                paymentOutboxMessage = paymentOutboxMessage,
                callback = {
                    message, status ->
                    message.outboxStatus = status
                    paymentOutboxHelper.save(message).subscribe()
                    logger.info("{} PaymentOutboxMessage는 {}로 업데이트 되었습니다.",
                        message.outboxStatus, status.name)
                }
            ).subscribe()
        }.subscribe()
    }
}