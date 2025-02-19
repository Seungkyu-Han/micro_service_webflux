package seungkyu.msa.service.common.valueObject

import org.bson.types.ObjectId

@JvmInline
value class ProductId(
    val id: ObjectId
)
