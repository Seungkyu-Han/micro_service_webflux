package seungkyu.msa.service.payment.domain.event

import seungkyu.msa.service.common.event.DomainEvent
import seungkyu.msa.service.payment.domain.entity.Payment
import java.time.LocalDateTime

abstract class PaymentEvent(
    open val payment: Payment,
    open val createdAt: LocalDateTime,
    open val failureMessages: MutableList<String>
): DomainEvent<Payment>