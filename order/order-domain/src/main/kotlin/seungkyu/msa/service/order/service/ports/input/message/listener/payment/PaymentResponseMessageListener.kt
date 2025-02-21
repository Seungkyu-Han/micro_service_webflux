package seungkyu.msa.service.order.service.ports.input.message.listener.payment

import seungkyu.msa.service.order.service.dto.message.PaymentResponse

interface PaymentResponseMessageListener {

    fun paymentCompleted(paymentResponse: PaymentResponse)

    fun paymentCancelled(paymentResponse: PaymentResponse)
}