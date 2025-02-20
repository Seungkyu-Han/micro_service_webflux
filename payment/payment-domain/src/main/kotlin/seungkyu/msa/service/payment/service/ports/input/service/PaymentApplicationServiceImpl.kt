package seungkyu.msa.service.payment.service.ports.input.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.payment.domain.PaymentDomainService
import seungkyu.msa.service.payment.domain.entity.Credit
import seungkyu.msa.service.payment.service.dto.DepositCommand
import seungkyu.msa.service.payment.service.exception.PaymentServiceException
import seungkyu.msa.service.payment.service.ports.output.repository.CreditRepository

@Service
class PaymentApplicationServiceImpl(
    private val creditRepository: CreditRepository,
    private val paymentDomainService: PaymentDomainService
): PaymentApplicationService {

    @Transactional
    override fun createDeposit(depositCommand: DepositCommand): Mono<Void> =
        creditRepository.findByCustomerId(customerId = CustomerId(ObjectId(depositCommand.customerId)))
            .switchIfEmpty(
                Mono.error(PaymentServiceException("사용자 ${depositCommand.customerId}을 찾을 수 없습니다."))
            )
            .map{
                credit: Credit ->
                paymentDomainService.deposit(credit, Money(depositCommand.amount))
                credit
            }
            .publishOn(Schedulers.boundedElastic())
            .map{
                creditRepository.save(it).subscribe()
            }
            .then()

    override fun createUser(): Mono<Void> {
        return creditRepository.save(
            paymentDomainService.createUser(ObjectId.get())
        ).then()
    }
}