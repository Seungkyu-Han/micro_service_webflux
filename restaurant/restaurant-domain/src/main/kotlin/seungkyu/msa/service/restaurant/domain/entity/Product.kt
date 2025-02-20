package seungkyu.msa.service.restaurant.domain.entity

import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.common.valueObject.ProductId

data class Product(
    val productId: ProductId,
    val name: String,
    var quantity: Int,
    val isActive: Boolean,
    val price: Money
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        return productId == other.productId
    }

    override fun hashCode(): Int {
        return productId.hashCode()
    }
}