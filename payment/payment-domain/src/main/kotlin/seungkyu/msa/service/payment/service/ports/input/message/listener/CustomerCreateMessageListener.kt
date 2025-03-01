package seungkyu.msa.service.payment.service.ports.input.message.listener

import reactor.core.publisher.Mono
import seungkyu.msa.service.payment.domain.entity.Credit

interface CustomerCreateMessageListener {

    fun createCredit(credit: Credit): Mono<Void>
}