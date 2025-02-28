package seungkyu.msa.service.payment.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money

class Credit(
    val customerId: CustomerId,
    var totalCreditAmount: Money
){

    @JsonIgnore
    fun addCreditAmount(amount: Money){
        totalCreditAmount += amount
    }

    @JsonIgnore
    fun subtractCreditAmount(amount: Money){
        totalCreditAmount -= amount
    }

}