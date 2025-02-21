package seungkyu.msa.service.order.domain.event

import seungkyu.msa.service.common.event.DomainEvent
import seungkyu.msa.service.order.domain.entity.Order
import java.time.LocalDateTime

class OrderCreatedEvent(
    val order: Order,
    val createdAt: LocalDateTime = LocalDateTime.now(),
): DomainEvent<Order>