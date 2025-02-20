package seungkyu.msa.service.payment.domain.event

import seungkyu.msa.service.payment.domain.entity.Payment
import java.time.LocalDateTime

data class PaymentCompletedEvent(
    override val payment: Payment,
    override val createdAt: LocalDateTime,
    override val failureMessages: MutableList<String>
): PaymentEvent(
    payment = payment,
    createdAt = createdAt,
    failureMessages = failureMessages
)