package seungkyu.msa.service.order.persistence.outbox.orderApproval.entity

import org.bson.types.ObjectId

data class OrderApprovalEventProductEntity(
    val id: ObjectId,
    val quantity: Int
)
