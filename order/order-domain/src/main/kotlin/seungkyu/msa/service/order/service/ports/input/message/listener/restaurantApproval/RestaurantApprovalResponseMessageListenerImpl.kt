package seungkyu.msa.service.order.service.ports.input.message.listener.restaurantApproval

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import seungkyu.msa.service.order.service.dto.message.RestaurantApprovalResponse
import seungkyu.msa.service.order.service.saga.OrderApprovalSaga

@Component
class RestaurantApprovalResponseMessageListenerImpl(
    private val orderApprovalSaga: OrderApprovalSaga,
): RestaurantApprovalResponseMessageListener {

    override fun orderApproved(restaurantApprovalResponse: RestaurantApprovalResponse) {
        orderApprovalSaga.process(restaurantApprovalResponse).subscribe()
    }

    @Transactional
    override fun orderRejected(restaurantApprovalResponse: RestaurantApprovalResponse) {
        orderApprovalSaga.rollback(restaurantApprovalResponse).subscribe()
    }
}