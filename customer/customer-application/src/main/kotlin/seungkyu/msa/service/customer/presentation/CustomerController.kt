package seungkyu.msa.service.customer.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import seungkyu.msa.service.customer.dto.create.CreateCustomerCommand
import seungkyu.msa.service.customer.dto.create.CreateCustomerResponse
import seungkyu.msa.service.customer.service.ports.input.service.CustomerApplicationService

@RestController
@RequestMapping("/customer")
class CustomerControllerController(
    private val customerApplicationService: CustomerApplicationService
) {

    @PostMapping("/create")
    fun createCustomer(createCustomerCommand: CreateCustomerCommand): Mono<ResponseEntity<CreateCustomerResponse>> {
        return customerApplicationService.createCustomer(createCustomerCommand)
            .map{
                ResponseEntity.ok(it)
            }
    }

}