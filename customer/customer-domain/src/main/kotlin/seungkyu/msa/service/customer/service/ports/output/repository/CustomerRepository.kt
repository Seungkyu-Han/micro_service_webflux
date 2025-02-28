package seungkyu.msa.service.customer.service.ports.output.repository

import reactor.core.publisher.Mono
import seungkyu.msa.service.customer.domain.entity.Customer

interface CustomerRepository {

    fun save(customer: Customer): Mono<Customer>
}