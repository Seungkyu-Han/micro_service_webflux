package seungkyu.msa.service.restaurant.domain.entity

import seungkyu.msa.service.common.valueObject.ProductId
import seungkyu.msa.service.common.valueObject.RestaurantId

data class Restaurant(
    val id: RestaurantId,
    val isActive: Boolean,
    val products: HashMap<ProductId, Product>
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Restaurant

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}