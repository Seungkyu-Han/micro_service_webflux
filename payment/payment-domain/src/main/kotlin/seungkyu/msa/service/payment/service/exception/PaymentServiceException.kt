package seungkyu.msa.service.payment.service.exception

class PaymentServiceException(
    override val message: String
): RuntimeException(message)