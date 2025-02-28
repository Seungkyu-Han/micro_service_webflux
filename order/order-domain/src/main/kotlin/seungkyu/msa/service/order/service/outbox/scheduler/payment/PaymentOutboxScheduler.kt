package seungkyu.msa.service.order.service.outbox.scheduler.payment

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.order.service.outbox.model.payment.PaymentOutboxMessage
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
        logger.info("결제 요청을 보내는 스케줄러가 동작합니다.")
        paymentOutboxHelper.getPaymentOutboxMessageByOutboxStatusAndOrderStatus(
            OutboxStatus.STARTED,
            listOf(OrderStatus.PENDING, OrderStatus.CANCELLING)
        ).publishOn(Schedulers.boundedElastic()).map{
            paymentOutboxMessage: PaymentOutboxMessage ->

            logger.info("스케줄러에 의해 {} 주문의 결제 요청을 전송하려고 합니다.", paymentOutboxMessage.id)
            paymentRequestMessagePublisher.publish(
                paymentOutboxMessage = paymentOutboxMessage,
                callback = ::updateOutboxStatus
            ).doOnSuccess {
                logger.info("스케줄러에 의해 {} 주문의 결제 요청을 전송했습니다.", paymentOutboxMessage.id)
            }.subscribe()
        }.subscribe()
    }

    private fun updateOutboxStatus(paymentOutboxMessage: PaymentOutboxMessage, outboxStatus: OutboxStatus): Mono<Void> {
        paymentOutboxMessage.outboxStatus = outboxStatus
        return paymentOutboxHelper.save(paymentOutboxMessage).then()
    }
}