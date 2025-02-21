package seungkyu.msa.service.order.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import seungkyu.msa.service.order.service.dto.create.CreateOrderCommand
import seungkyu.msa.service.order.service.dto.create.CreateOrderResponse
import seungkyu.msa.service.order.service.ports.input.service.OrderApplicationService

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderApplicationService: OrderApplicationService
) {

    @PostMapping("/create")
    fun createOrder(@RequestBody createOrderCommand: CreateOrderCommand): Mono<ResponseEntity<CreateOrderResponse>> {
        return orderApplicationService.createOrder(createOrderCommand)
            .map{
                ResponseEntity.ok(it)
            }
    }
}