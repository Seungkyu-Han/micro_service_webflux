package seungkyu.msa.service.common.valueObject

import org.bson.types.ObjectId

@JvmInline
value class OrderId(
    val id: ObjectId
)