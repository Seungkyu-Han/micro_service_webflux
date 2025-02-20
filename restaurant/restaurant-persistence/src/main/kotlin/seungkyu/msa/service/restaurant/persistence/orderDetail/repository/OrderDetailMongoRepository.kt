package seungkyu.msa.service.restaurant.persistence.orderDetail.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import seungkyu.msa.service.restaurant.persistence.orderDetail.entity.OrderDetailEntity

interface OrderDetailMongoRepository: ReactiveMongoRepository<OrderDetailEntity, ObjectId> {
}