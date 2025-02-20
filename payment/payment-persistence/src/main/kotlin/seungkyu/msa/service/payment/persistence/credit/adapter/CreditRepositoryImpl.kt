package seungkyu.msa.service.payment.persistence.credit.adapter

import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.payment.domain.entity.Credit
import seungkyu.msa.service.payment.persistence.credit.entity.CreditEntity
import seungkyu.msa.service.payment.persistence.credit.repository.CreditMongoRepository
import seungkyu.msa.service.payment.service.ports.output.repository.CreditRepository

@Repository
class CreditRepositoryImpl(
    private val creditMongoRepository: CreditMongoRepository
): CreditRepository {

    override fun save(credit: Credit): Mono<Credit> =
        creditMongoRepository.save(
            creditToCreditEntity(credit)
        ).map(this::creditEntityToCredit)


    override fun findByCustomerId(customerId: CustomerId): Mono<Credit> =
        creditMongoRepository.findById(customerId.id)
            .map(this::creditEntityToCredit)

    private fun creditToCreditEntity(credit: Credit): CreditEntity =
        CreditEntity(
            id = credit.customerId.id,
            totalCreditAmount = credit.totalCreditAmount.amount
        )

    private fun creditEntityToCredit(creditEntity: CreditEntity): Credit =
        Credit(
            customerId = CustomerId(creditEntity.id),
            totalCreditAmount = Money(creditEntity.totalCreditAmount)
        )
}