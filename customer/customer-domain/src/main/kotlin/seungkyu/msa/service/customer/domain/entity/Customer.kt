package seungkyu.msa.service.customer.domain.entity

import seungkyu.msa.service.common.valueObject.CustomerId

data class Customer(
    val id: CustomerId,
    val username: String,
    val email: String
)