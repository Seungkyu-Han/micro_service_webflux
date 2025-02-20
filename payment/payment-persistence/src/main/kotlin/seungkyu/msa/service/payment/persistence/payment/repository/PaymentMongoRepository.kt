package seungkyu.msa.service.payment.persistence.payment.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import seungkyu.msa.service.payment.persistence.payment.entity.PaymentEntity

interface PaymentMongoRepository: ReactiveMongoRepository<PaymentEntity, ObjectId>{
}