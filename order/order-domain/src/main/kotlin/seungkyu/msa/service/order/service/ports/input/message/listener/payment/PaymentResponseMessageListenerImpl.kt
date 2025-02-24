package seungkyu.msa.service.order.service.ports.input.message.listener.payment

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import seungkyu.msa.service.order.service.dto.message.PaymentResponse
import seungkyu.msa.service.order.service.ports.output.message.publisher.restaurantApproval.OrderPaidRestaurantRequestMessagePublisher
import seungkyu.msa.service.order.service.saga.OrderPaymentSaga

@Service
class PaymentResponseMessageListenerImpl(
    private val orderPaymentSaga: OrderPaymentSaga,
    private val orderPaidRestaurantRequestMessagePublisher: OrderPaidRestaurantRequestMessagePublisher
): PaymentResponseMessageListener{

    private val logger = LoggerFactory.getLogger(PaymentResponseMessageListenerImpl::class.java)

    override fun paymentCompleted(paymentResponse: PaymentResponse) {
        orderPaymentSaga.process(paymentResponse)
            .map{
                orderPaidRestaurantRequestMessagePublisher.publish(it)
            }
            .subscribe()
    }

    override fun paymentCancelled(paymentResponse: PaymentResponse) {
        logger.info("결제 취소 rollback을 수행하겠습니다 id: ${paymentResponse.id}")
        orderPaymentSaga.rollback(paymentResponse).subscribe()
    }
}