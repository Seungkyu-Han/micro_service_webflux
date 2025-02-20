package seungkyu.msa.service.payment.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import seungkyu.msa.service.payment.service.dto.DepositCommand
import seungkyu.msa.service.payment.service.ports.input.service.PaymentApplicationService

@RestController
@RequestMapping("/payment")
class PaymentController(
    private val paymentApplicationService: PaymentApplicationService
) {

    @PostMapping("/deposit")
    fun deposit(@RequestBody depositCommand: DepositCommand): Mono<ResponseEntity<Void>> {
        return paymentApplicationService.createDeposit(depositCommand)
            .thenReturn(ResponseEntity.ok().build())
    }

}