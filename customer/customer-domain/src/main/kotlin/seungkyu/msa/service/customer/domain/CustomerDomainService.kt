package seungkyu.msa.service.customer.domain

import seungkyu.msa.service.customer.domain.entity.Customer
import seungkyu.msa.service.customer.domain.event.CustomerCreatedEvent

interface CustomerDomainService {

    fun validateAndCreateCustomer(customer: Customer): CustomerCreatedEvent
}