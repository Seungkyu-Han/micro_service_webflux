package seungkyu.msa.service.restaurant.service.dto

data class AddProductCommand(
    val restaurantId: String,
    val name: String,
    val quantity: Int,
    val price: Long
)
