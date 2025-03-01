package seungkyu.msa.service.customer.persistence.customer.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import seungkyu.msa.service.customer.persistence.customer.entity.CustomerEntity

interface CustomerMongoRepository: ReactiveMongoRepository<CustomerEntity, ObjectId> {
}