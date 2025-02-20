package seungkyu.msa.service.restaurant.service.ports.input.message.listener

import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderApprovalStatus
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.common.valueObject.OrderId
import seungkyu.msa.service.common.valueObject.ProductId
import seungkyu.msa.service.common.valueObject.RestaurantId
import seungkyu.msa.service.restaurant.domain.RestaurantDomainService
import seungkyu.msa.service.restaurant.domain.entity.OrderDetail
import seungkyu.msa.service.restaurant.domain.entity.Restaurant
import seungkyu.msa.service.restaurant.service.dto.RestaurantApprovalRequestDto
import seungkyu.msa.service.restaurant.service.ports.output.message.publisher.OrderApprovedMessagePublisher
import seungkyu.msa.service.restaurant.service.ports.output.message.publisher.OrderRejectedMessagePublisher
import seungkyu.msa.service.restaurant.service.ports.output.repository.OrderDetailRepository
import seungkyu.msa.service.restaurant.service.ports.output.repository.RestaurantRepository

@Component
class OrderApprovalRequestMessageListenerImpl(
    private val restaurantDomainService: RestaurantDomainService,
    private val restaurantRepository: RestaurantRepository,
    private val orderDetailRepository: OrderDetailRepository,
    private val orderApprovedMessagePublisher: OrderApprovedMessagePublisher,
    private val orderRejectedMessagePublisher: OrderRejectedMessagePublisher
): OrderApprovalRequestMessageListener {

    private val logger = LoggerFactory.getLogger(OrderApprovalRequestMessageListenerImpl::class.java)

    override fun approveOrder(restaurantApprovalRequestDto: RestaurantApprovalRequestDto): Mono<Void> {
        logger.info("주문 {}에 대한 승인 요청이 들어왔습니다.", restaurantApprovalRequestDto.orderId)
        return findRestaurant(ObjectId(restaurantApprovalRequestDto.restaurantId))
            .map{
                restaurant ->
                val orderDetail = OrderDetail(
                    id = OrderId(ObjectId(restaurantApprovalRequestDto.orderId)),
                    restaurantId = RestaurantId(ObjectId(restaurantApprovalRequestDto.restaurantId)),
                    totalAmount = Money(restaurantApprovalRequestDto.price),
                    orderProducts = restaurantApprovalRequestDto.products.mapKeys{ ProductId(ObjectId(it.key)) }.toMap(HashMap()),
                    orderStatus = OrderStatus.valueOf(restaurantApprovalRequestDto.restaurantOrderStatus.name),
                    orderApprovalStatus = OrderApprovalStatus.UNKNOWN
                )

                val orderApprovalEvent = restaurantDomainService.validateOrder(
                    orderDetail = orderDetail,
                    restaurant = restaurant,
                    failureMessages = mutableListOf(),
                )

                if(orderApprovalEvent.orderApprovalStatus == OrderApprovalStatus.APPROVED) {
                    saveToDB(restaurant, orderDetail).thenReturn(
                        orderApprovedMessagePublisher.publish(orderApprovalEvent)
                    )
                }
                else{
                    Mono.just(orderRejectedMessagePublisher.publish(orderApprovalEvent))
                }
            }.then()
    }

    private fun findRestaurant(restaurantId: ObjectId): Mono<Restaurant> {
        return restaurantRepository.findById(restaurantId)
    }

    private fun saveToDB(restaurant: Restaurant, orderDetail: OrderDetail): Mono<Void> {
        return restaurantRepository.save(restaurant)
            .then(orderDetailRepository.save(orderDetail))
            .then()
    }
}