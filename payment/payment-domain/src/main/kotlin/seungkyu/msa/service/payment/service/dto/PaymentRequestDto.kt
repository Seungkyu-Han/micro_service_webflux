package seungkyu.msa.service.payment.service.dto

import seungkyu.msa.service.common.status.PaymentOrderStatus
import java.time.LocalDateTime

data class PaymentRequestDto(
    val id: String,
    val customerId: String,
    val price: Long,
    val createdAt: LocalDateTime,
    val paymentOrderStatus: PaymentOrderStatus
){
    override fun toString(): String {
        return "PaymentRequestDto(id='$id', customerId='$customerId', price=$price, createdAt=$createdAt, paymentOrderStatus=$paymentOrderStatus)"
    }
}