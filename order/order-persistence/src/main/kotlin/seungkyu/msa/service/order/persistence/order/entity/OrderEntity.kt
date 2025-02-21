package seungkyu.msa.service.order.persistence.order.entity

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import seungkyu.msa.service.common.status.OrderStatus

@Document(collection = "orders")
data class OrderEntity(
    val id: ObjectId,
    val customerId: ObjectId,
    val restaurantId: ObjectId,
    val price: Long,
    val items: List<OrderItemEntity>,
    val orderStatus: OrderStatus){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}