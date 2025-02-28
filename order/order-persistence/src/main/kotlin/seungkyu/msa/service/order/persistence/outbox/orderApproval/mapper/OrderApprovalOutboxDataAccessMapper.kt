package seungkyu.msa.service.order.persistence.outbox.orderApproval.mapper

import org.springframework.stereotype.Component
import seungkyu.msa.service.order.persistence.outbox.orderApproval.entity.OrderApprovalEventPayloadEntity
import seungkyu.msa.service.order.persistence.outbox.orderApproval.entity.OrderApprovalEventProductEntity
import seungkyu.msa.service.order.persistence.outbox.orderApproval.entity.OrderApprovalOutboxEntity
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalEventPayload
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalEventProduct
import seungkyu.msa.service.order.service.outbox.model.orderApproval.OrderApprovalOutboxMessage

@Component
class OrderApprovalOutboxDataAccessMapper {

    fun orderApprovalOutboxEntityToOrderApprovalOutboxMessage(orderApprovalOutboxEntity: OrderApprovalOutboxEntity): OrderApprovalOutboxMessage {
        return OrderApprovalOutboxMessage(
            id = orderApprovalOutboxEntity.id,
            createdAt = orderApprovalOutboxEntity.createdAt,
            processedAt = orderApprovalOutboxEntity.processedAt,
            type = orderApprovalOutboxEntity.type,
            payload = OrderApprovalEventPayload(
                restaurantId = orderApprovalOutboxEntity.payload.restaurantId,
                price = orderApprovalOutboxEntity.payload.price,
                products = orderApprovalOutboxEntity.payload.products.map{
                    OrderApprovalEventProduct(
                        id = it.id,
                        quantity = it.quantity
                    )
                }
            ),
            orderStatus = orderApprovalOutboxEntity.orderStatus,
            outboxStatus = orderApprovalOutboxEntity.outboxStatus,
            version = orderApprovalOutboxEntity.version,
        )
    }

    fun orderApprovalOutboxMessageToOrderApprovalOutboxEntity(orderApprovalOutboxMessage: OrderApprovalOutboxMessage): OrderApprovalOutboxEntity{
        return OrderApprovalOutboxEntity(
            id = orderApprovalOutboxMessage.id,
            createdAt = orderApprovalOutboxMessage.createdAt,
            processedAt = orderApprovalOutboxMessage.processedAt,
            type = orderApprovalOutboxMessage.type,
            payload = OrderApprovalEventPayloadEntity(
                restaurantId = orderApprovalOutboxMessage.payload.restaurantId,
                price = orderApprovalOutboxMessage.payload.price,
                products = orderApprovalOutboxMessage.payload.products.map{
                    OrderApprovalEventProductEntity(
                        id = it.id,
                        quantity = it.quantity
                    )
                }
            ),
            outboxStatus = orderApprovalOutboxMessage.outboxStatus,
            orderStatus = orderApprovalOutboxMessage.orderStatus,
            version = orderApprovalOutboxMessage.version,
        )
    }
}