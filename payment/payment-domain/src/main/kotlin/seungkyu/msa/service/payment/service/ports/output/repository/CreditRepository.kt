package seungkyu.msa.service.payment.service.ports.output.repository

import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.payment.domain.entity.Credit

interface CreditRepository {

    fun save(credit: Credit): Credit

    fun findByCustomerId(customerId: CustomerId): Credit
}