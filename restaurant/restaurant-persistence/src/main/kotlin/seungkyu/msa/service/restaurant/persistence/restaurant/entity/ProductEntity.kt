package seungkyu.msa.service.restaurant.persistence.restaurant.entity

import org.bson.types.ObjectId

data class ProductEntity(
    val productId: ObjectId,
    val name: String,
    var quantity: Int,
    val isActive: Boolean,
    val price: Long
)
