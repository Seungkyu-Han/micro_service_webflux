package seungkyu.msa.service.order.service.outbox.model.orderApproval

import org.bson.types.ObjectId

data class OrderApprovalEventProduct(
    val id: ObjectId,
    val quantity: Int
)
