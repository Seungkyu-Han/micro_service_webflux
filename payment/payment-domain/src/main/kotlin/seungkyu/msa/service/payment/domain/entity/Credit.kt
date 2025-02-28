package seungkyu.msa.service.payment.domain.entity

import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money

class Credit(
    val customerId: CustomerId,
    var totalCreditAmount: Money
){

    fun addCreditAmount(amount: Money){
        totalCreditAmount += amount
    }

    fun subtractCreditAmount(amount: Money){
        totalCreditAmount -= amount
    }

}