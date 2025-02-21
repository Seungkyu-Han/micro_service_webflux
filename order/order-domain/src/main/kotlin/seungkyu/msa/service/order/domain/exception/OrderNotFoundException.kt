package seungkyu.msa.service.order.domain.exception

class OrderNotFoundException(
    override val message: String
): RuntimeException(message)