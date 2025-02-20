package seungkyu.msa.service.restaurant.service.ports.output.repository

import org.bson.types.ObjectId
import reactor.core.publisher.Mono
import seungkyu.msa.service.restaurant.domain.entity.Restaurant

interface RestaurantRepository {

    fun save(restaurant: Restaurant): Mono<Restaurant>

    fun findById(id: ObjectId): Mono<Restaurant>
}