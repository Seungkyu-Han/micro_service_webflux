package seungkyu.msa.service.payment.persistence.credit.entity


data class CreditEntity(
    val customerId: String,
    var totalCreditAmount: Long
)