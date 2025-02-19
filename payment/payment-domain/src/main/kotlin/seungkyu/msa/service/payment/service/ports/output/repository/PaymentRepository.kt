package seungkyu.msa.service.payment.service.ports.output.repository

import seungkyu.msa.service.payment.domain.entity.Payment

interface PaymentRepository {

    fun save(payment: Payment): Payment

    fun findByOrderId(orderId: String): Payment
}