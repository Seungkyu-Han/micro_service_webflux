package seungkyu.msa.service.payment.service.ports.output.repository

import org.bson.types.ObjectId
import reactor.core.publisher.Mono
import seungkyu.msa.service.payment.domain.entity.Payment

interface PaymentRepository {

    fun save(payment: Payment): Mono<Payment>

    fun findByOrderId(orderId: ObjectId): Mono<Payment>
}