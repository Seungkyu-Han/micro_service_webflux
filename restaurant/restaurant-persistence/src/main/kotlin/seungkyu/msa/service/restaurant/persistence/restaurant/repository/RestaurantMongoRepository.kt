package seungkyu.msa.service.restaurant.persistence.restaurant.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import seungkyu.msa.service.restaurant.persistence.restaurant.entity.RestaurantEntity

interface RestaurantMongoRepository: ReactiveMongoRepository<RestaurantEntity, ObjectId>