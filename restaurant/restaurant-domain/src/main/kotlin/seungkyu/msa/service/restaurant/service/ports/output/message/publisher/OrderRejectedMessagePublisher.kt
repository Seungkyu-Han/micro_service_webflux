package seungkyu.msa.service.restaurant.service.ports.output.message.publisher

import seungkyu.msa.service.common.event.publisher.DomainEventPublisher
import seungkyu.msa.service.restaurant.domain.event.OrderApprovalEvent

interface OrderRejectedMessagePublisher: DomainEventPublisher<OrderApprovalEvent> {
}