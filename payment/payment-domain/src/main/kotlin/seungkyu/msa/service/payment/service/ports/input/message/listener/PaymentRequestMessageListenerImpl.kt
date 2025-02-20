package seungkyu.msa.service.payment.service.ports.input.message.listener

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.PaymentStatus
import seungkyu.msa.service.payment.domain.event.PaymentEvent
import seungkyu.msa.service.payment.service.dto.PaymentRequestDto
import seungkyu.msa.service.payment.service.helper.PaymentRequestHelper
import seungkyu.msa.service.payment.service.ports.output.message.publisher.PaymentCancelledMessagePublisher
import seungkyu.msa.service.payment.service.ports.output.message.publisher.PaymentCompletedMessagePublisher
import seungkyu.msa.service.payment.service.ports.output.message.publisher.PaymentFailedMessagePublisher

@Service
class PaymentRequestMessageListenerImpl(
    private val paymentRequestHelper: PaymentRequestHelper,
    private val paymentCompletedMessagePublisher: PaymentCompletedMessagePublisher,
    private val paymentCancelledMessagePublisher: PaymentCancelledMessagePublisher,
    private val paymentFailedMessagePublisher: PaymentFailedMessagePublisher
): PaymentRequestMessageListener{

    private val logger = LoggerFactory.getLogger(PaymentRequestMessageListenerImpl::class.java)

    override fun completePayment(paymentRequestDto: PaymentRequestDto): Mono<Void> =
        paymentRequestHelper.persistPayment(paymentRequestDto)
            .doOnNext{
                paymentEvent: PaymentEvent ->
                if(paymentEvent.payment.paymentStatus == PaymentStatus.COMPLETED)
                    paymentCompletedMessagePublisher.publish(paymentEvent)
                else
                    paymentFailedMessagePublisher.publish(paymentEvent)
            }.doOnNext{
                logger.info("주문 :{}의 결제 :{} 이벤트가 전송되었습니다", it.payment.orderId.id, it.payment.id.id)
            }
            .then()


    override fun cancelPayment(paymentRequestDto: PaymentRequestDto): Mono<Void> =
        paymentRequestHelper.persistCancelPayment(paymentRequestDto)
            .doOnNext{
                    paymentEvent: PaymentEvent ->
                if(paymentEvent.payment.paymentStatus == PaymentStatus.CANCELLED)
                    paymentCancelledMessagePublisher.publish(paymentEvent)
                else
                    paymentFailedMessagePublisher.publish(paymentEvent)
            }.doOnNext{
                logger.info("주문 :{}의 결제취소 :{} 이벤트가 전송되었습니다", it.payment.orderId.id, it.payment.id.id)
            }
            .then()

}