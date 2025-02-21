package seungkyu.msa.service.payment.domain.entity

import seungkyu.msa.service.common.status.PaymentStatus
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.common.valueObject.OrderId
import java.time.LocalDateTime

class Payment(
    val id: OrderId,
    val customerId: CustomerId,
    val price: Money,
    var paymentStatus: PaymentStatus,
    val createdAt: LocalDateTime = LocalDateTime.now(),
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Payment

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun validatePayment(failureMessages: MutableList<String>){
        if(!price.isGreaterThanZero()){
            failureMessages.add("결제 금액은 0원을 넘어야합니다.")
        }
    }
}