package seungkyu.msa.service.order.persistence.order.entity

import org.bson.types.ObjectId

data class OrderItemEntity(
    val id: ObjectId,
    val quantity: Int,
)
