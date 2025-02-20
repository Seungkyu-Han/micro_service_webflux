package seungkyu.msa.service.restaurant.domain.exception

class RestaurantNotFoundException(
    override val message: String
): RuntimeException(message)