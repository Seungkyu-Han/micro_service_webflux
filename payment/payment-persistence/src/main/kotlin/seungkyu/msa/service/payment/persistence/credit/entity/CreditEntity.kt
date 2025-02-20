package seungkyu.msa.service.payment.persistence.credit.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "credits")
data class CreditEntity(
    @Id
    val id: ObjectId,
    val totalCreditAmount: Long
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CreditEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
