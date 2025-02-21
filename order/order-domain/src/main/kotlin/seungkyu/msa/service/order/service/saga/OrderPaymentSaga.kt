package seungkyu.msa.service.order.service.saga

import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.event.EmptyEvent
import seungkyu.msa.service.order.domain.OrderDomainService
import seungkyu.msa.service.order.domain.event.OrderPaidEvent
import seungkyu.msa.service.order.service.dto.message.PaymentResponse
import seungkyu.msa.service.order.service.ports.output.message.publisher.restaurantApproval.OrderPaidRestaurantRequestMessagePublisher
import seungkyu.msa.service.order.service.ports.output.repository.OrderRepository
import seungkyu.msa.service.saga.SagaStep

@Component
class OrderPaymentSaga(
    private val orderDomainService: OrderDomainService,
    private val orderRepository: OrderRepository,
    private val orderPaidRestaurantRequestMessagePublisher: OrderPaidRestaurantRequestMessagePublisher
): SagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {

    private val logger = LoggerFactory.getLogger(OrderPaymentSaga::class.java)

    override fun process(data: PaymentResponse): Mono<OrderPaidEvent> {
        logger.info("주문 {}의 상태를 결제완료로 변경합니다", data.id)
        return orderRepository.findById(ObjectId(data.id))
            .flatMap{
                val orderPaidEvent = orderDomainService.payOrder(order = it)
                orderRepository.save(it)
                    .thenReturn(orderPaidEvent)
            }.doOnNext{
                logger.info("주문 {}의 상태가 결제완료로 변경되어 저장되었습니다.", it.order.orderId.id)
            }
    }

    override fun rollback(data: PaymentResponse): Mono<EmptyEvent> {
        logger.info("주문 {}의 상태를 취소로 변경합니다.", data.id)
        return orderRepository.findById(ObjectId(data.id))
            .flatMap {
                orderDomainService.cancelOrder(order = it)
                orderRepository.save(it)
                    .thenReturn(EmptyEvent())
            }.doOnNext{
                logger.info("주문 {}의 상태가 취소로 변경되어 저장되었습니다.", data.id)
            }
    }
}