package seungkyu.msa.service.payment.domain

import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.payment.domain.entity.Credit
import seungkyu.msa.service.payment.domain.entity.Payment
import seungkyu.msa.service.payment.domain.event.PaymentEvent

interface PaymentDomainService {

    fun validateAndInitPayment(payment: Payment, credit: Credit, failureMessages: MutableList<String>): PaymentEvent

    fun validateAndCancelPayment(payment: Payment, credit: Credit, failureMessages: MutableList<String>): PaymentEvent

    fun deposit(credit: Credit, money: Money)
}