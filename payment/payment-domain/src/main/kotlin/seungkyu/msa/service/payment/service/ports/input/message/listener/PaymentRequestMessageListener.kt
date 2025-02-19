package seungkyu.msa.service.payment.service.ports.input.message.listener

import seungkyu.msa.service.payment.service.dto.PaymentRequestDto

interface PaymentRequestMessageListener {

    fun completePayment(paymentRequestDto: PaymentRequestDto)

    fun cancelPayment(paymentRequestDto: PaymentRequestDto)
}