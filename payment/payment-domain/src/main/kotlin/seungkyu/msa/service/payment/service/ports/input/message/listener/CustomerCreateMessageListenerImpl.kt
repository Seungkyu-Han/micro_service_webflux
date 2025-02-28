package seungkyu.msa.service.payment.service.ports.input.message.listener

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import seungkyu.msa.service.payment.domain.entity.Credit
import seungkyu.msa.service.payment.service.ports.output.repository.CreditRepository

@Component
class CustomerCreateMessageListenerImpl(
    private val creditRepository: CreditRepository
): CustomerCreateMessageListener {

    override fun createCredit(credit: Credit): Mono<Void> {
        return creditRepository.save(credit).then()
    }
}