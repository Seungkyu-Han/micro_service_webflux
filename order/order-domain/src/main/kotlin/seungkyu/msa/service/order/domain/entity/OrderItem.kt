package seungkyu.msa.service.order.domain.entity

import seungkyu.msa.service.common.valueObject.ProductId

data class OrderItem(
    val productId: ProductId,
    val quantity: Int,
)
