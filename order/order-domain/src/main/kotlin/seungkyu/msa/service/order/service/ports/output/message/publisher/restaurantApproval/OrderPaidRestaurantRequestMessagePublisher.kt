package seungkyu.msa.service.order.service.ports.output.message.publisher.restaurantApproval

import seungkyu.msa.service.common.event.publisher.DomainEventPublisher
import seungkyu.msa.service.order.domain.event.OrderPaidEvent

interface OrderPaidRestaurantRequestMessagePublisher: DomainEventPublisher<OrderPaidEvent> {
}