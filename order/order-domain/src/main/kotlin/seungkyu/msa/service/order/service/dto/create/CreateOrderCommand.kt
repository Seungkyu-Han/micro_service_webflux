package seungkyu.msa.service.order.service.dto.create

data class CreateOrderCommand(
    val customerId: String,
    val restaurantId: String,
    val items: List<OrderItem>,
    val price: Long
)