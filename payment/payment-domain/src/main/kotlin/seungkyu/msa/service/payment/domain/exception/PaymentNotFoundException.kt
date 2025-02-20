package seungkyu.msa.service.payment.domain.exception

class PaymentNotFoundException(override val message: String) : RuntimeException(message)