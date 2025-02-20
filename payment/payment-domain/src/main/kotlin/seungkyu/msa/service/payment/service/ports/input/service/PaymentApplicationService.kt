package seungkyu.msa.service.payment.service.ports.input.service

import reactor.core.publisher.Mono
import seungkyu.msa.service.payment.service.dto.DepositCommand

interface PaymentApplicationService {

    fun createDeposit(depositCommand: DepositCommand): Mono<Void>
}