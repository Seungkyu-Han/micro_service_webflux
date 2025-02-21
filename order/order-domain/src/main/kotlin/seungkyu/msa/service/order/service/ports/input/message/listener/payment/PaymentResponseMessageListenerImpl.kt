package seungkyu.msa.service.order.service.ports.input.message.listener.payment

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import seungkyu.msa.service.order.service.dto.message.PaymentResponse

@Service
class PaymentResponseMessageListenerImpl: PaymentResponseMessageListener{

    private val logger = LoggerFactory.getLogger(PaymentResponseMessageListenerImpl::class.java)

    override fun paymentCompleted(paymentResponse: PaymentResponse) {
        logger.error("하이 아직 구현 안했지롱")
    }

    override fun paymentCancelled(paymentResponse: PaymentResponse) {
        logger.error("하이 아직 구현 안했지롱")
    }
}