package seungkyu.msa.service.order.persistence.outbox.payment.exception

class PaymentOutboxNotFoundException(
    override val message: String
): RuntimeException(message)