package seungkyu.msa.service.restaurant.service.ports.input.service

import reactor.core.publisher.Mono
import seungkyu.msa.service.restaurant.service.dto.AddProductCommand

interface RestaurantApplicationService {

    fun createRestaurant(): Mono<Void>

    fun addProduct(addProductCommand: AddProductCommand): Mono<Void>
}