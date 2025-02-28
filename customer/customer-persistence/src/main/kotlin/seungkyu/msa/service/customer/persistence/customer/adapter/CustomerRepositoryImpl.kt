package seungkyu.msa.service.customer.persistence.customer.adapter

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.customer.domain.entity.Customer
import seungkyu.msa.service.customer.persistence.customer.entity.CustomerEntity
import seungkyu.msa.service.customer.persistence.customer.repository.CustomerMongoRepository
import seungkyu.msa.service.customer.service.ports.output.repository.CustomerRepository

@Component
class CustomerRepositoryImpl(
    private val customerMongoRepository: CustomerMongoRepository
): CustomerRepository {

    override fun save(customer: Customer): Mono<Customer> {
        return customerMongoRepository.save(customerToCustomerEntity(customer))
            .map(::customerEntityToCustomer)
    }

    private fun customerToCustomerEntity(customer: Customer): CustomerEntity {
        return CustomerEntity(
            id = customer.id.id,
            username = customer.username,
            email = customer.email,
        )
    }

    private fun customerEntityToCustomer(customerEntity: CustomerEntity): Customer {
        return Customer(
            id = CustomerId(customerEntity.id),
            username = customerEntity.username,
            email = customerEntity.email,
        )
    }
}