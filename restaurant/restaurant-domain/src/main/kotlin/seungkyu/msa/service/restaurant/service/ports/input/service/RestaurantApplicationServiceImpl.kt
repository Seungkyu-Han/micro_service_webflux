package seungkyu.msa.service.restaurant.service.ports.input.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.common.valueObject.ProductId
import seungkyu.msa.service.restaurant.domain.RestaurantDomainService
import seungkyu.msa.service.restaurant.domain.entity.Product
import seungkyu.msa.service.restaurant.service.dto.AddProductCommand
import seungkyu.msa.service.restaurant.service.ports.output.repository.RestaurantRepository

@Service
class RestaurantApplicationServiceImpl(
    private val restaurantDomainService: RestaurantDomainService,
    private val restaurantRepository: RestaurantRepository
): RestaurantApplicationService {
    override fun createRestaurant(): Mono<Void> {
        return restaurantRepository.save(
            restaurantDomainService.createRestaurant()
        ).then()
    }

    override fun addProduct(addProductCommand: AddProductCommand): Mono<Void> {
        return restaurantRepository.findById(ObjectId(addProductCommand.restaurantId))
            .flatMap {
                restaurant ->
                restaurant.addProduct(
                    Product(
                        productId = ProductId(ObjectId.get()),
                        name = addProductCommand.name,
                        quantity = addProductCommand.quantity,
                        price = Money(addProductCommand.price),
                        isActive = true
                    )
                )
                restaurantRepository.save(restaurant)
            }.then()
    }
}