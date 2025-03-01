package seungkyu.msa.service.customer.service.ports.input.service

import reactor.core.publisher.Mono
import seungkyu.msa.service.customer.dto.create.CreateCustomerCommand
import seungkyu.msa.service.customer.dto.create.CreateCustomerResponse

interface CustomerApplicationService {

    fun createCustomer(createCustomerCommand: CreateCustomerCommand): Mono<CreateCustomerResponse>
}