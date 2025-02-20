package seungkyu.msa.service.restaurant.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import seungkyu.msa.service.restaurant.service.dto.AddProductCommand
import seungkyu.msa.service.restaurant.service.ports.input.service.RestaurantApplicationService

@RestController
@RequestMapping("/restaurant")
class RestaurantController(
    private val restaurantApplicationService: RestaurantApplicationService
) {

    @PostMapping("/create")
    fun createRestaurant(): Mono<ResponseEntity<Void>> {
        return restaurantApplicationService.createRestaurant()
            .thenReturn(ResponseEntity.ok().build())
    }

    @PostMapping("/menu")
    fun createRestaurantMenu(
        @RequestBody addProductCommand: AddProductCommand
    ): Mono<ResponseEntity<Void>> {
        return restaurantApplicationService.addProduct(addProductCommand)
            .thenReturn(ResponseEntity.ok().build())
    }
}