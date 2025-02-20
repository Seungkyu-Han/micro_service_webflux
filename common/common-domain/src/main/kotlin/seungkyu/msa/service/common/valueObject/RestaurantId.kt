package seungkyu.msa.service.common.valueObject

import org.bson.types.ObjectId

@JvmInline
value class RestaurantId(
    val id: ObjectId
)