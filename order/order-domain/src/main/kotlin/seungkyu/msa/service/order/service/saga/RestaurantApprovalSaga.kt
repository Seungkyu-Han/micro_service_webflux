package seungkyu.msa.service.order.service.saga

import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.event.EmptyEvent
import seungkyu.msa.service.order.domain.OrderDomainService
import seungkyu.msa.service.order.domain.event.OrderCancelledEvent
import seungkyu.msa.service.order.service.dto.message.RestaurantApprovalResponse
import seungkyu.msa.service.order.service.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher
import seungkyu.msa.service.order.service.ports.output.repository.OrderRepository
import seungkyu.msa.service.saga.SagaStep

@Component
class RestaurantApprovalSaga(
    private val orderDomainService: OrderDomainService,
    private val orderRepository: OrderRepository,
    private val orderCancelledPaymentRequestMessagePublisher: OrderCancelledPaymentRequestMessagePublisher
): SagaStep<RestaurantApprovalResponse, EmptyEvent, OrderCancelledEvent> {

    private val logger = LoggerFactory.getLogger(RestaurantApprovalSaga::class.java)

    @Transactional
    override fun process(data: RestaurantApprovalResponse): Mono<EmptyEvent> {
        logger.info("{} 주문이 승인 완료되었습니다", data.id)

        return orderRepository.findById(ObjectId(data.id))
            .flatMap {
                orderDomainService.approveOrder(order = it)
                logger.info("바뀐거: $it")
                orderRepository.save(it)
                    .thenReturn(EmptyEvent())
            }.doOnNext{
                logger.info("{} 주문이 승인 완료되어 저장되었습니다", data.id)
            }
    }

    @Transactional
    override fun rollback(data: RestaurantApprovalResponse): Mono<OrderCancelledEvent> {
        logger.info("{} 주문이 미승인 되었습니다", data.id)
        return orderRepository.findById(ObjectId(data.id))
            .flatMap {
                val orderCancelledEvent = orderDomainService.cancelOrderPayment(it)
                orderRepository.save(it)
                    .thenReturn(orderCancelledEvent)
            }.doOnNext{
                logger.info("{} 주문이 취소 중 상태로 저장되었습니다", it.order.orderId.id.toString())
            }.doOnNext{
                orderCancelledPaymentRequestMessagePublisher.publish(it)
            }.doOnNext {
                logger.info("{} 주문의 결제 취소 이벤트를 전송했습니다.", it.order.orderId.id.toString())
            }
    }
}