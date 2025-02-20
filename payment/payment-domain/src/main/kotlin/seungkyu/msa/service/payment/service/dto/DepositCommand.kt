package seungkyu.msa.service.payment.service.dto

data class DepositCommand(
    val customerId: String,
    val amount: Long,
)
