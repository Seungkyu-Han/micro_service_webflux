package seungkyu.msa.service.customer.service.ports.input.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.customer.domain.CustomerDomainService
import seungkyu.msa.service.customer.domain.entity.Customer
import seungkyu.msa.service.customer.dto.create.CreateCustomerCommand
import seungkyu.msa.service.customer.dto.create.CreateCustomerResponse
import seungkyu.msa.service.customer.service.ports.output.message.publisher.CustomerMessagePublisher
import seungkyu.msa.service.customer.service.ports.output.repository.CustomerRepository

@Service
class CustomerApplicationServiceImpl(
    private val customerDomainService: CustomerDomainService,
    private val customerRepository: CustomerRepository,
    private val customerMessagePublisher: CustomerMessagePublisher,
): CustomerApplicationService {

    override fun createCustomer(createCustomerCommand: CreateCustomerCommand): Mono<CreateCustomerResponse> {
        val customer = Customer(
            id = CustomerId(ObjectId.get()),
            username = createCustomerCommand.username,
            email = createCustomerCommand.email
        )

        val customerCreatedEvent = customerDomainService.validateAndCreateCustomer(customer)

        return customerMessagePublisher.publish(customerCreatedEvent)
            .then(
                customerRepository.save(customer)
            ).thenReturn(CreateCustomerResponse(customer.id.toString(), customer.username, customer.email))
    }
}