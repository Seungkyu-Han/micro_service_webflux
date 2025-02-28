package seungkyu.msa.service.customer.domain.event

import seungkyu.msa.service.customer.domain.entity.Customer
import java.time.LocalDateTime

data class CustomerCreatedEvent(
    val customer: Customer,
    val createdAt: LocalDateTime
)
