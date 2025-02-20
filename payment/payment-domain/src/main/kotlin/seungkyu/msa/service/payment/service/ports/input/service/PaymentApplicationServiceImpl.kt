package seungkyu.msa.service.payment.service.ports.input.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.payment.domain.entity.Credit
import seungkyu.msa.service.payment.service.dto.DepositCommand
import seungkyu.msa.service.payment.service.exception.PaymentServiceException
import seungkyu.msa.service.payment.service.ports.output.repository.CreditRepository

@Service
class PaymentApplicationServiceImpl(
    private val creditRepository: CreditRepository
): PaymentApplicationService {

    @Transactional
    override fun createDeposit(depositCommand: DepositCommand): Mono<Void> =
        creditRepository.findByCustomerId(customerId = CustomerId(ObjectId(depositCommand.customerId)))
            .switchIfEmpty(
                Mono.error(PaymentServiceException("사용자 ${depositCommand.customerId}을 찾을 수 없습니다."))
            )
            .map{
                credit: Credit ->
                credit.addCreditAmount(Money(depositCommand.amount))
                creditRepository.save(credit)
            }
            .then()

}