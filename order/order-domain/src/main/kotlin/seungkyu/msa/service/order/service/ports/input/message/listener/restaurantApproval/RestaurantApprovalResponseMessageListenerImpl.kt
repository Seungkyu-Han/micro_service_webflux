package seungkyu.msa.service.order.service.ports.input.message.listener.restaurantApproval

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import seungkyu.msa.service.order.service.dto.message.RestaurantApprovalResponse
import seungkyu.msa.service.order.service.saga.RestaurantApprovalSaga

@Component
class RestaurantApprovalResponseMessageListenerImpl(
    private val restaurantApprovalSaga: RestaurantApprovalSaga,
): RestaurantApprovalResponseMessageListener {

    override fun orderApproved(restaurantApprovalResponse: RestaurantApprovalResponse) {
        restaurantApprovalSaga.process(restaurantApprovalResponse).subscribe()
    }

    @Transactional
    override fun orderRejected(restaurantApprovalResponse: RestaurantApprovalResponse) {
        restaurantApprovalSaga.rollback(restaurantApprovalResponse).subscribe()
    }
}