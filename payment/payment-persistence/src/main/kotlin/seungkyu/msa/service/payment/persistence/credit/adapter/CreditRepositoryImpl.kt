package seungkyu.msa.service.payment.persistence.credit.adapter

import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.types.ObjectId
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.payment.domain.entity.Credit
import seungkyu.msa.service.payment.persistence.credit.entity.CreditEntity
import seungkyu.msa.service.payment.service.ports.output.repository.CreditRepository

@Repository
class CreditRepositoryImpl(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
): CreditRepository {

    private val redisPrefix = "credit:"

    override fun save(credit: Credit): Mono<Credit>{
        return reactiveRedisTemplate.opsForValue().set(
            "${redisPrefix}${credit.customerId.id}",
            objectMapper.writeValueAsString(creditToCreditEntity(credit))
        ).thenReturn(credit)
    }

    override fun findByCustomerId(customerId: CustomerId): Mono<Credit> {
        return reactiveRedisTemplate.opsForValue().get("${redisPrefix}${customerId.id}")
            .map{
                objectMapper.readValue(it, CreditEntity::class.java)
            }.map(::creditEntityToCredit)
    }

    private fun creditEntityToCredit(creditEntity: CreditEntity): Credit{
        return Credit(
            customerId = CustomerId(id = ObjectId(creditEntity.customerId)),
            totalCreditAmount = Money(amount = creditEntity.totalCreditAmount)
        )
    }
    private fun creditToCreditEntity(credit: Credit): CreditEntity{
        return CreditEntity(
            customerId = credit.customerId.id.toString(),
            totalCreditAmount = credit.totalCreditAmount.amount
        )
    }
}