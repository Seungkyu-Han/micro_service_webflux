package seungkyu.msa.service.order.domain.entity

import org.bson.types.ObjectId
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.common.valueObject.OrderId
import seungkyu.msa.service.common.valueObject.RestaurantId
import seungkyu.msa.service.order.domain.exception.OrderDomainException

data class Order(
    val orderId: OrderId = OrderId(ObjectId.get()),
    val customerId: CustomerId,
    val restaurantId: RestaurantId,
    val price: Money,
    val orderItems: List<OrderItem>,
    var orderStatus: OrderStatus = OrderStatus.PENDING
){
    fun pay(){
        if(orderStatus != OrderStatus.PENDING)
            throw OrderDomainException("${orderId.id} 주문은 결제가 필요한 상태가 아닙니다.")
        orderStatus = OrderStatus.PAID
    }

    fun cancelPay(){
        if(orderStatus != OrderStatus.PAID){
            throw OrderDomainException("${orderId.id} 주문은 결제한 상태가 아닙니다.")
        }
        orderStatus = OrderStatus.CANCELLING
    }

    fun cancel(){
        if(!(orderStatus == OrderStatus.PENDING || orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)){
            throw OrderDomainException("${orderId.id} 주문은 취소 할 수 없습니다.")
        }
        orderStatus = OrderStatus.CANCELLED
    }

    fun approve(){
        if(orderStatus != OrderStatus.PAID){
            throw OrderDomainException("${orderId.id} 주문의 결제가 필요합니다.")
        }
        orderStatus = OrderStatus.APPROVED
    }
}