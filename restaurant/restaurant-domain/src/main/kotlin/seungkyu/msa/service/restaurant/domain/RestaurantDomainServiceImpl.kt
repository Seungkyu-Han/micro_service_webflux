package seungkyu.msa.service.restaurant.domain

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import seungkyu.msa.service.common.valueObject.ProductId
import seungkyu.msa.service.restaurant.domain.entity.OrderDetail
import seungkyu.msa.service.restaurant.domain.entity.Restaurant
import seungkyu.msa.service.restaurant.domain.event.OrderApprovalEvent
import seungkyu.msa.service.restaurant.domain.event.OrderApprovedEvent
import seungkyu.msa.service.restaurant.domain.event.OrderRejectedEvent
import java.time.LocalDateTime

@Service
class RestaurantDomainServiceImpl: RestaurantDomainService {

    private val logger = LoggerFactory.getLogger(RestaurantDomainServiceImpl::class.java)

    override fun validateOrder(orderDetail: OrderDetail, failureMessages: MutableList<String>): OrderApprovalEvent {
        orderDetail.validateOrder(failureMessages)

        if(failureMessages.isEmpty()){
            logger.info("{} 주문이 승인되었습니다.", orderDetail.id.id)
            orderDetail.approveOrder()
            return OrderApprovedEvent(
                restaurantId = orderDetail.restaurant.id,
                orderId = orderDetail.id,
                orderApprovalStatus = orderDetail.orderApprovalStatus,
                createdAt = LocalDateTime.now(),
            )
        }
        else{
            logger.info("{} 주문이 거절되었습니다.", orderDetail.id.id)
            orderDetail.rejectOrder()
            return OrderRejectedEvent(
                restaurantId = orderDetail.restaurant.id,
                orderId = orderDetail.id,
                orderApprovalStatus = orderDetail.orderApprovalStatus,
                createdAt = LocalDateTime.now(),
            )
        }
    }
}