package seungkyu.msa.service.common.valueObject

import org.bson.types.ObjectId

@JvmInline
value class PaymentId(
    val id: ObjectId = ObjectId.get()
)