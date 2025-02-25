package seungkyu.msa.service.order.service.ports.output.message.publisher.restaurantApproval

import reactor.core.publisher.Mono
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalOutboxMessage
import seungkyu.msa.service.outbox.OutboxStatus

interface OrderApprovalRequestMessagePublisher {

    fun publish(orderApprovalOutboxMessage: OrderApprovalOutboxMessage,
                callback: (OrderApprovalOutboxMessage, OutboxStatus) -> Unit): Mono<Void>
}