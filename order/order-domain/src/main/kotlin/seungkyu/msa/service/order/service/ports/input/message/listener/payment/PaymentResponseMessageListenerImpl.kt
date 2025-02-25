package seungkyu.msa.service.order.service.ports.input.message.listener.payment

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import seungkyu.msa.service.order.service.dto.message.PaymentResponse
import seungkyu.msa.service.order.service.saga.OrderPaymentSaga

@Service
class PaymentResponseMessageListenerImpl(
    private val orderPaymentSaga: OrderPaymentSaga
): PaymentResponseMessageListener{

    private val logger = LoggerFactory.getLogger(PaymentResponseMessageListenerImpl::class.java)

    override fun paymentCompleted(paymentResponse: PaymentResponse) {
        orderPaymentSaga.process(paymentResponse)
            .subscribe()

        logger.info("{} 주문의 결제가 완려되었습니다.", paymentResponse.id)
    }

    override fun paymentCancelled(paymentResponse: PaymentResponse) {
        orderPaymentSaga.rollback(paymentResponse).subscribe()
        logger.info("{}의 결제 취소 rollback을 수행했습니다 id:", paymentResponse.id)
    }
}