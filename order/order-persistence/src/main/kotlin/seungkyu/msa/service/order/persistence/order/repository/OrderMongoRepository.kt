package seungkyu.msa.service.order.persistence.order.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import seungkyu.msa.service.order.persistence.order.entity.OrderEntity

interface OrderMongoRepository: ReactiveMongoRepository<OrderEntity, ObjectId> {
}