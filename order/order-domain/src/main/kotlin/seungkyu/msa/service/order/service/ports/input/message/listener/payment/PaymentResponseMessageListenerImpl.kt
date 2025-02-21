package seungkyu.msa.service.order.service.ports.input.message.listener.payment

import org.springframework.stereotype.Service
import seungkyu.msa.service.order.service.dto.message.PaymentResponse

@Service
class PaymentResponseMessageListenerImpl: PaymentResponseMessageListener{

    override fun paymentCompleted(paymentResponse: PaymentResponse) {

    }

    override fun paymentCancelled(paymentResponse: PaymentResponse) {

    }
}