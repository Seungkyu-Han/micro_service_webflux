package seungkyu.msa.service.order.service.dto.create

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateOrderResponse(
    @JsonProperty("orderId")
    val orderId: String
)
