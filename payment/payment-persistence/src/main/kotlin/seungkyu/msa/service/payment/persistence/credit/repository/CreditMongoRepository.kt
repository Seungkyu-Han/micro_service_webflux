package seungkyu.msa.service.payment.persistence.credit.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import seungkyu.msa.service.payment.persistence.credit.entity.CreditEntity

interface CreditMongoRepository: ReactiveMongoRepository<CreditEntity, ObjectId> {

}