package seungkyu.msa.service.payment.service.helper

import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.status.PaymentStatus
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.payment.domain.PaymentDomainService
import seungkyu.msa.service.payment.domain.entity.Credit
import seungkyu.msa.service.payment.domain.entity.Payment
import seungkyu.msa.service.payment.domain.event.PaymentEvent
import seungkyu.msa.service.payment.service.dto.PaymentRequestDto
import seungkyu.msa.service.payment.service.exception.PaymentServiceException
import seungkyu.msa.service.payment.service.ports.output.repository.CreditRepository
import seungkyu.msa.service.payment.service.ports.output.repository.PaymentRepository

@Component
class PaymentRequestHelper(
    private val paymentDomainService: PaymentDomainService,
    private val paymentDataMapper: PaymentDataMapper,
    private val paymentRepository: PaymentRepository,
    private val creditRepository: CreditRepository
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun persistPayment(paymentRequestDto: PaymentRequestDto): Mono<PaymentEvent>{
        logger.info("주문 {}의 결제 완료 이벤트를 수신했습니다", paymentRequestDto.id)
        val payment: Payment = paymentDataMapper.paymentRequestDtoToPayment(paymentRequestDto = paymentRequestDto)
        //고객의 잔액을 조회
        return getCredit(customerId = payment.customerId)
            .flatMap { credit ->
                //거래를 시작
                val paymentEvent: PaymentEvent = paymentDomainService.validateAndInitPayment(
                    payment = payment, credit = credit, failureMessages = mutableListOf()
                )
                //결제 후 상태를 데이터베이스에 저장
                saveToDB(payment = payment, credit = credit)
                    //결제 이벤트를 반환
                    .thenReturn(paymentEvent)
            }
    }

    @Transactional
    fun persistCancelPayment(paymentRequestDto: PaymentRequestDto): Mono<PaymentEvent>{
        logger.info("주문 {}의 결제 취소 이벤트를 수신했습니다", paymentRequestDto.id)
        //해당 주문의 유무를 조회
        return findPaymentByOrderId(orderId = ObjectId(paymentRequestDto.id))
            .flatMap{
                payment ->
                //해당 고객의 잔액을 조회
                getCredit(payment.customerId)
                    .flatMap { credit ->
                        //거래를 확인하고 금액을 복구
                        val paymentEvent: PaymentEvent = paymentDomainService.validateAndCancelPayment(
                            payment = payment, credit = credit, failureMessages = mutableListOf()
                        )
                        logger.info("{}의 잔액이 복구되었습니다 {}", paymentRequestDto.customerId, credit.totalCreditAmount)
                        //결제 복구 후 상태를 데이터베이스에 저장
                        saveToDB(payment = payment, credit = credit)
                            //결제 이벤트를 반환
                            .thenReturn(paymentEvent)
                    }
            }
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


    private fun getCredit(customerId: CustomerId): Mono<Credit> =
        creditRepository.findByCustomerId(customerId = customerId)
            .switchIfEmpty(
                Mono.defer{
                    logger.error("{}의 잔액을 확인 할 수 없습니다.", customerId.id)
                    throw PaymentServiceException("${customerId.id}의 잔액을 확인 할 수 없습니다.")
                }
            )

    private fun findPaymentByOrderId(orderId: ObjectId): Mono<Payment> =
        paymentRepository.findByOrderId(orderId = orderId)
            .switchIfEmpty(
                Mono.defer{
                    logger.error("주문 {}가 존재하지 않습니다.", orderId)
                    throw PaymentServiceException("주문 ${orderId}가 존재하지 않습니다.")
                }
            )
}