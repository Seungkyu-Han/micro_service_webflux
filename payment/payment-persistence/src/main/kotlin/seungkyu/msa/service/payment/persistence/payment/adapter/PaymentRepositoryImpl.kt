package seungkyu.msa.service.payment.persistence.payment.adapter

import org.bson.types.ObjectId
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.common.valueObject.OrderId
import seungkyu.msa.service.payment.domain.entity.Payment
import seungkyu.msa.service.payment.persistence.payment.entity.PaymentEntity
import seungkyu.msa.service.payment.persistence.payment.repository.PaymentMongoRepository
import seungkyu.msa.service.payment.service.ports.output.repository.PaymentRepository

@Repository
class PaymentRepositoryImpl(
    private val paymentMongoRepository: PaymentMongoRepository
): PaymentRepository{

    override fun save(payment: Payment): Mono<Payment> {
        return paymentMongoRepository.save(paymentToPaymentEntity(payment))
            .map(this::paymentEntityToPayment)
    }

    override fun findByOrderId(orderId: ObjectId): Mono<Payment> =
        paymentMongoRepository.findById(orderId)
            .map(this::paymentEntityToPayment)

    private fun paymentEntityToPayment(paymentEntity: PaymentEntity): Payment {
        return Payment(
            id = OrderId(paymentEntity.id),
            customerId = CustomerId(paymentEntity.customerId),
            price = Money(paymentEntity.price),
            paymentStatus = paymentEntity.paymentStatus,
            createdAt = paymentEntity.createdAt
        )
    }

    private fun paymentToPaymentEntity(payment: Payment): PaymentEntity {
        return PaymentEntity(
            id = payment.id.id,
            customerId = payment.customerId.id,
            price = payment.price.amount,
            paymentStatus = payment.paymentStatus,
            createdAt = payment.createdAt,
        )
    }
}