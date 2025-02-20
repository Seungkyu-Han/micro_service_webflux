package seungkyu.msa.service.restaurant.persistence.restaurant.adapter

import org.bson.types.ObjectId
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.common.valueObject.ProductId
import seungkyu.msa.service.common.valueObject.RestaurantId
import seungkyu.msa.service.restaurant.domain.entity.Product
import seungkyu.msa.service.restaurant.domain.entity.Restaurant
import seungkyu.msa.service.restaurant.persistence.restaurant.entity.ProductEntity
import seungkyu.msa.service.restaurant.persistence.restaurant.entity.RestaurantEntity
import seungkyu.msa.service.restaurant.persistence.restaurant.repository.RestaurantMongoRepository
import seungkyu.msa.service.restaurant.service.ports.output.repository.RestaurantRepository

@Repository
class RestaurantRepositoryImpl(
    private val restaurantMongoRepository: RestaurantMongoRepository
): RestaurantRepository {

    override fun save(restaurant: Restaurant): Mono<Restaurant> {
        return restaurantMongoRepository.save(
            restaurantToRestaurantEntity(restaurant)
        ).map(this::restaurantEntityToRestaurant)
    }

    override fun findById(id: ObjectId): Mono<Restaurant> {
        return restaurantMongoRepository.findById(id)
            .map(this::restaurantEntityToRestaurant)
    }

    private fun restaurantEntityToRestaurant(restaurantEntity: RestaurantEntity): Restaurant {
        return Restaurant(
            id = RestaurantId(restaurantEntity.id),
            isActive = restaurantEntity.isActive,
            products = restaurantEntity.products.map(this::productEntityToProduct)
                .associateBy{it.productId}.toMap(HashMap()),
        )
    }

    private fun restaurantToRestaurantEntity(restaurant: Restaurant): RestaurantEntity {
        return RestaurantEntity(
            id = restaurant.id.id,
            isActive = restaurant.isActive,
            products = restaurant.products.values.map(this::productToProductEntity)
        )
    }

    private fun productEntityToProduct(productEntity: ProductEntity): Product {
        return Product(
            productId = ProductId(productEntity.productId),
            name = productEntity.name,
            quantity = productEntity.quantity,
            isActive = productEntity.isActive,
            price = Money(productEntity.price),
        )
    }

    private fun productToProductEntity(product: Product): ProductEntity {
        return ProductEntity(
            productId = product.productId.id,
            name = product.name,
            quantity = product.quantity,
            isActive = product.isActive,
            price = product.price.amount
        )
    }
}