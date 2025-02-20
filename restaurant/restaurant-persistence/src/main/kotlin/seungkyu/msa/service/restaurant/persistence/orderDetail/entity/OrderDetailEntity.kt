package seungkyu.msa.service.restaurant.persistence.orderDetail.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import seungkyu.msa.service.common.status.OrderApprovalStatus
import seungkyu.msa.service.common.status.OrderStatus

@Document(collection = "order_details")
data class OrderDetailEntity(
    @Id
    val id: ObjectId,
    val restaurantId: ObjectId,
    val totalAmount: Long,
    val orderProducts: Map<ObjectId, Int>,
    val orderStatus: OrderStatus,
    var orderApprovalStatus: OrderApprovalStatus
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderDetailEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}