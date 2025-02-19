package seungkyu.msa.service.common.valueObject

import org.bson.types.ObjectId

@JvmInline
value class CustomerId(
    val id: ObjectId
)