package seungkyu.msa.service.order.service.outbox.model.orderApproval

import org.bson.types.ObjectId

data class OrderApprovalEventPayload(
    val restaurantId: ObjectId,
    val price: Long,
    val products: List<OrderApprovalEventProduct>
)
