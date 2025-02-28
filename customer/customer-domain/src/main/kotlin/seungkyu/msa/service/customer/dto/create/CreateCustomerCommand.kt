package seungkyu.msa.service.customer.dto.create

data class CreateCustomerCommand(
    val username: String,
    val email: String
)
