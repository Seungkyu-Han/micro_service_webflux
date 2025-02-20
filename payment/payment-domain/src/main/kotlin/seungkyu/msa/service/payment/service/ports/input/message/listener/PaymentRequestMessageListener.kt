package seungkyu.msa.service.payment.service.ports.input.message.listener

import reactor.core.publisher.Mono
import seungkyu.msa.service.payment.service.dto.PaymentRequestDto

interface PaymentRequestMessageListener {

    fun completePayment(paymentRequestDto: PaymentRequestDto): Mono<Void>

    fun cancelPayment(paymentRequestDto: PaymentRequestDto): Mono<Void>
}