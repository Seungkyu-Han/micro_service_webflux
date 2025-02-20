package seungkyu.msa.service.restaurant.domain.exception

class RestaurantDomainException(
    override val message: String
): RuntimeException(message)