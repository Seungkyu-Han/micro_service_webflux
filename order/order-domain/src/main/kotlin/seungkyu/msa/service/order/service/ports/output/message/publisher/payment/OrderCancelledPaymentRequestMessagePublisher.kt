package seungkyu.msa.service.order.service.ports.output.message.publisher.payment

import seungkyu.msa.service.common.event.publisher.DomainEventPublisher
import seungkyu.msa.service.order.domain.event.OrderCancelledEvent

interface OrderCancelledPaymentRequestMessagePublisher: DomainEventPublisher<OrderCancelledEvent> {
}