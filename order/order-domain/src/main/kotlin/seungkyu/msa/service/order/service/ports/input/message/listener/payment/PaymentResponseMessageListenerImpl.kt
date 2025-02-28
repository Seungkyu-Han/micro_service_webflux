package seungkyu.msa.service.order.service.ports.input.message.listener.payment

import org.springframework.stereotype.Service
import seungkyu.msa.service.order.service.dto.message.PaymentResponse
import seungkyu.msa.service.order.service.saga.PaymentSaga

@Service
class PaymentResponseMessageListenerImpl(
    private val paymentSaga: PaymentSaga
): PaymentResponseMessageListener{

    override fun paymentCompleted(paymentResponse: PaymentResponse) {
        paymentSaga.process(paymentResponse)
            .subscribe()
    }

    override fun paymentCancelled(paymentResponse: PaymentResponse) {
        paymentSaga.rollback(paymentResponse).subscribe()
    }
}