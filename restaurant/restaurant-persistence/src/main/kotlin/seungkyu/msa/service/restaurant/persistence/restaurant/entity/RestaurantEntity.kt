package seungkyu.msa.service.restaurant.persistence.restaurant.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "restaurants")
data class RestaurantEntity(
    @Id
    val id: ObjectId = ObjectId.get(),
    val isActive: Boolean,
    val products: List<ProductEntity>
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}