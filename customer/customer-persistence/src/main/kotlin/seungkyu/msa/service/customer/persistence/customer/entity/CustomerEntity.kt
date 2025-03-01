package seungkyu.msa.service.customer.persistence.customer.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "customers")
data class CustomerEntity(
    @Id
    val id: ObjectId,
    val username: String,
    val email: String
)
