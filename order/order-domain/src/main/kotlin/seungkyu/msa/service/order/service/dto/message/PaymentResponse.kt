package seungkyu.msa.service.order.service.dto.message

import seungkyu.msa.service.common.status.PaymentStatus
import java.time.LocalDateTime

data class PaymentResponse(
    val id: String,
    val customerId: String,
    val price: Long,
    val createdAt: LocalDateTime,
    val paymentStatus: PaymentStatus,
)
