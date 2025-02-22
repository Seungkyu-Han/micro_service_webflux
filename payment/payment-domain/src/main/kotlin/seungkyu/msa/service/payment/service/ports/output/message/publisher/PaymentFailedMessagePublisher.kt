package seungkyu.msa.service.payment.service.ports.output.message.publisher

import seungkyu.msa.service.common.event.publisher.DomainEventPublisher
import seungkyu.msa.service.payment.domain.event.PaymentEvent

interface PaymentFailedMessagePublisher: DomainEventPublisher<PaymentEvent> {
}