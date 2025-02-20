package seungkyu.msa.service.restaurant.domain

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import seungkyu.msa.service.restaurant.domain.entity.OrderDetail
import seungkyu.msa.service.restaurant.domain.entity.Restaurant
import seungkyu.msa.service.restaurant.domain.event.OrderApprovalEvent
import seungkyu.msa.service.restaurant.domain.event.OrderApprovedEvent
import seungkyu.msa.service.restaurant.domain.event.OrderRejectedEvent
import java.time.LocalDateTime

@Service
class RestaurantDomainServiceImpl: RestaurantDomainService {

    private val logger = LoggerFactory.getLogger(RestaurantDomainServiceImpl::class.java)

    override fun validateOrder(
        orderDetail: OrderDetail,
        restaurant: Restaurant,
        failureMessages: MutableList<String>): OrderApprovalEvent {
        orderDetail.validateOrder(restaurant, failureMessages)

        if(failureMessages.isEmpty()){
            logger.info("{} 주문이 승인되었습니다.", orderDetail.id.id)
            orderDetail.approveOrder(restaurant)
            return OrderApprovedEvent(
                orderDetail = orderDetail,
                orderApprovalStatus = orderDetail.orderApprovalStatus,
                createdAt = LocalDateTime.now(),
            )
        }
        else{
            logger.info("{} 주문이 거절되었습니다.", orderDetail.id.id)
            orderDetail.rejectOrder()
            return OrderRejectedEvent(
                orderDetail = orderDetail,
                orderApprovalStatus = orderDetail.orderApprovalStatus,
                createdAt = LocalDateTime.now(),
            )
        }
    }
}