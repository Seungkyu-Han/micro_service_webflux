package seungkyu.msa.service.restaurant.domain.entity

import seungkyu.msa.service.common.status.OrderApprovalStatus
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.common.valueObject.OrderId
import seungkyu.msa.service.common.valueObject.ProductId
import seungkyu.msa.service.common.valueObject.RestaurantId

data class OrderDetail(
    val id: OrderId,
    val restaurantId: RestaurantId,
    val totalAmount: Money,
    val orderProducts: HashMap<ProductId, Int>,
    val orderStatus: OrderStatus,
    var orderApprovalStatus: OrderApprovalStatus
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderDetail

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun validateOrder(restaurant: Restaurant, failureMessages: MutableList<String>){
        if(!restaurant.isActive){
            failureMessages.add("현재 ${restaurant.id.id}식당이 운영 중이 아닙니다")
            return
        }

        if(orderStatus != OrderStatus.PAID){
            failureMessages.add("$id 주문의 결제가 완료되지 않았습니다.")
            return
        }

        var priceToPay = Money.ZERO

        for(productId in orderProducts.keys){
            val product = restaurant.products[productId]
            if(product == null){
                failureMessages.add("$productId 상품이 존재하지 않습니다.")
                return
            }
            else if(!product.isActive || product.quantity <= orderProducts[productId]!!){
                failureMessages.add("$productId 상품을 이용할 수 없습니다.")
                return
            }
            else{
                priceToPay += (product.price * orderProducts.getOrDefault(productId, 0))
            }
        }

        if(priceToPay != totalAmount){
            failureMessages.add("실제 결제해야 하는 금액은 ${priceToPay.amount}으로 ${totalAmount.amount}와 일치하지 않습니다.")
            return
        }
    }

    fun approveOrder(restaurant: Restaurant) {
        for(productId in orderProducts.keys){
            restaurant.products[productId]!!.quantity -= orderProducts[productId]!!
        }
        orderApprovalStatus = OrderApprovalStatus.APPROVED
    }

    fun rejectOrder() {
        orderApprovalStatus = OrderApprovalStatus.REJECTED
    }
}