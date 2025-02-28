package seungkyu.msa.service.order.persistence.outbox.orderApproval.entity

import org.bson.types.ObjectId

data class OrderApprovalEventPayloadEntity(
    val restaurantId: ObjectId,
    val price: Long,
    val products: List<OrderApprovalEventProductEntity>
)
