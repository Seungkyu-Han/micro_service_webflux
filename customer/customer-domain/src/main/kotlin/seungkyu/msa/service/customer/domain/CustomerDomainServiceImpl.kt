package seungkyu.msa.service.customer.domain

import org.springframework.stereotype.Service
import seungkyu.msa.service.customer.domain.entity.Customer
import seungkyu.msa.service.customer.domain.event.CustomerCreatedEvent
import java.time.LocalDateTime

@Service
class CustomerDomainServiceImpl: CustomerDomainService {

    override fun validateAndCreateCustomer(customer: Customer): CustomerCreatedEvent {
        return CustomerCreatedEvent(customer, LocalDateTime.now())
    }
}