package seungkyu.msa.service.order.service.ports.input.message.listener.payment

import org.springframework.stereotype.Service
import seungkyu.msa.service.order.service.dto.message.PaymentResponse
import seungkyu.msa.service.order.service.ports.output.message.publisher.restaurantApproval.OrderPaidRestaurantRequestMessagePublisher
import seungkyu.msa.service.order.service.saga.OrderPaymentSaga

@Service
class PaymentResponseMessageListenerImpl(
    private val orderPaymentSaga: OrderPaymentSaga,
    private val orderPaidRestaurantRequestMessagePublisher: OrderPaidRestaurantRequestMessagePublisher
): PaymentResponseMessageListener{

    override fun paymentCompleted(paymentResponse: PaymentResponse) {
        orderPaymentSaga.process(paymentResponse)
            .map{
                orderPaidRestaurantRequestMessagePublisher.publish(it)
            }
            .subscribe()
    }

    override fun paymentCancelled(paymentResponse: PaymentResponse) {
        orderPaymentSaga.rollback(paymentResponse).subscribe()
    }
}