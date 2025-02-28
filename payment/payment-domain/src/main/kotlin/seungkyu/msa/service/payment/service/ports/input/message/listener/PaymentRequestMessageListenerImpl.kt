package seungkyu.msa.service.payment.service.ports.input.message.listener

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.OrderStatus
import seungkyu.msa.service.common.status.PaymentStatus
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.common.valueObject.OrderId
import seungkyu.msa.service.outbox.OutboxStatus
import seungkyu.msa.service.payment.domain.PaymentDomainService
import seungkyu.msa.service.payment.domain.entity.Credit
import seungkyu.msa.service.payment.domain.entity.Payment
import seungkyu.msa.service.payment.domain.event.PaymentEvent
import seungkyu.msa.service.payment.service.dto.PaymentRequestDto
import seungkyu.msa.service.payment.service.outbox.model.PaymentEventPayload
import seungkyu.msa.service.payment.service.outbox.model.PaymentOutboxMessage
import seungkyu.msa.service.payment.service.ports.output.repository.CreditRepository
import seungkyu.msa.service.payment.service.ports.output.repository.PaymentOutboxRepository
import seungkyu.msa.service.payment.service.ports.output.repository.PaymentRepository
import java.time.LocalDateTime

@Service
class PaymentRequestMessageListenerImpl(
    private val paymentDomainService: PaymentDomainService,
    private val paymentRepository: PaymentRepository,
    private val creditRepository: CreditRepository,
    private val paymentOutboxRepository: PaymentOutboxRepository,
): PaymentRequestMessageListener{

    private val logger = LoggerFactory.getLogger(PaymentRequestMessageListenerImpl::class.java)

    @Transactional
    override fun completePayment(paymentRequestDto: PaymentRequestDto): Mono<Void>{
        return mono {
            //해당 이벤트를 수신한 적 있는지 확인
            val existedPaymentOutboxMessage = paymentOutboxRepository.findByTypeAndIdAndOrderStatus(
                id = ObjectId(paymentRequestDto.id),
                orderStatus = OrderStatus.PAID
            ).awaitSingleOrNull()

            if (existedPaymentOutboxMessage == null){

                //결제를 생성
                val payment = paymentRequestDtoToPayment(paymentRequestDto)

                //잔액을 조회
                val credit = creditRepository.findByCustomerId(CustomerId(ObjectId(paymentRequestDto.customerId))).awaitSingle()

                //잔액에서 결제금액을 차감
                val paymentEvent: PaymentEvent = paymentDomainService.validateAndInitPayment(
                    payment = payment, credit = credit, failureMessages = mutableListOf()
                )

                //결제 후 상태를 데이터베이스에 저장
                logger.info("주문 {}의 결제 후 상태를 데이터베이스에 저장하려고 합니다", paymentRequestDto.id)
                saveToDB(payment = payment, credit = credit).awaitSingleOrNull()
                logger.info("주문 {}의 결제 후 상태를 데이터베이스에 저장완료", paymentRequestDto.id)

                //결제 Outbox message를 생성 후 데이터베이스에 저장
                val paymentOutboxMessage = paymentRequestDtoToPaymentOutboxMessage(paymentRequestDto, paymentEvent)
                logger.info("주문 {}의 outbox를 데이터베이스에 저장하려고 합니다", paymentOutboxMessage.id)
                paymentOutboxRepository.save(paymentOutboxMessage).awaitSingle()
                logger.info("주문 {}에 대한 결제 응답을 outbox에 저장했습니다.", paymentRequestDto.id)
            }
        }.then()
    }


    override fun cancelPayment(paymentRequestDto: PaymentRequestDto): Mono<Void>{
        return mono {
            //해당 이벤트를 수신한 적 있는지 확인
            val existedPaymentOutboxMessage = paymentOutboxRepository.findByTypeAndIdAndOrderStatus(
                id = ObjectId(paymentRequestDto.id),
                orderStatus = OrderStatus.CANCELLED
            ).awaitSingleOrNull()

            if (existedPaymentOutboxMessage == null){
                logger.info("주문 {}에 대한 결제 취소 요청을 수신했습니다", paymentRequestDto.id)

                //결제를 조회
                val payment = paymentRepository.findById(ObjectId(paymentRequestDto.id)).awaitSingle()

                //잔액을 조회
                val credit = creditRepository.findByCustomerId(CustomerId(ObjectId(paymentRequestDto.customerId))).awaitSingle()

                //잔액에서 결제금액을 복구
                val paymentEvent: PaymentEvent = paymentDomainService.validateAndCancelPayment(
                    payment = payment, credit = credit, failureMessages = mutableListOf()
                )

                saveToDB(payment = payment, credit = credit).awaitSingleOrNull()

                val paymentOutboxMessage = paymentRequestDtoToPaymentOutboxMessage(paymentRequestDto, paymentEvent)
                paymentOutboxRepository.save(paymentOutboxMessage).awaitSingle()
                logger.info("주문 {}에 대한 결제 취소를 outbox에 저장했습니다.", paymentRequestDto.id)
            }
        }.then()
    }


    private fun saveToDB(payment: Payment, credit: Credit):Mono<Void> =
        paymentRepository.save(payment)
            .filter{
                payment.paymentStatus == PaymentStatus.COMPLETED || payment.paymentStatus == PaymentStatus.CANCELLED
            }
            .flatMap{
                creditRepository.save(credit)
            }
            .then()

    private fun paymentRequestDtoToPayment(paymentRequestDto: PaymentRequestDto): Payment {
        return Payment(
            id = OrderId(id = ObjectId(paymentRequestDto.id)),
            customerId = CustomerId(id = ObjectId(paymentRequestDto.customerId)),
            price = Money(paymentRequestDto.price),
            paymentStatus = PaymentStatus.UNKNOWN
        )
    }

    private fun paymentRequestDtoToPaymentOutboxMessage(
        paymentRequestDto: PaymentRequestDto,
        paymentEvent: PaymentEvent): PaymentOutboxMessage {
        return PaymentOutboxMessage(
            id = ObjectId(paymentRequestDto.id),
            createdAt = paymentRequestDto.createdAt,
            outboxStatus = OutboxStatus.STARTED,
            processedAt = LocalDateTime.now(),
            payload = PaymentEventPayload(
                orderId = ObjectId(paymentRequestDto.id),
                customerId = ObjectId(paymentRequestDto.customerId),
                price = paymentRequestDto.price,
                createdAt = paymentRequestDto.createdAt,
                paymentOrderStatus = paymentRequestDto.paymentOrderStatus,
                orderStatus = OrderStatus.PENDING,
                paymentStatus = paymentEvent.payment.paymentStatus,
            ),
            version = 0
        )
    }
}