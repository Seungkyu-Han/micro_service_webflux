package seungkyu.msa.service.payment.domain

import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import seungkyu.msa.service.common.status.PaymentStatus
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.payment.domain.entity.Credit
import seungkyu.msa.service.payment.domain.entity.Payment
import seungkyu.msa.service.payment.domain.event.PaymentCompletedEvent
import seungkyu.msa.service.payment.domain.event.PaymentEvent
import seungkyu.msa.service.payment.domain.event.PaymentFailedEvent
import java.time.LocalDateTime

@Service
class PaymentDomainServiceImpl: PaymentDomainService {

    private val logger = LoggerFactory.getLogger(PaymentDomainServiceImpl::class.java)

    override fun validateAndInitPayment(payment: Payment, credit: Credit, failureMessages: MutableList<String>): PaymentEvent {
        //결제 금액을 통해 결제의 유효성을 확인
        payment.validatePayment(failureMessages)

        //잔액이 충분한지 확인
        validateCredit(payment = payment, credit = credit, failureMessages = failureMessages)

        if(failureMessages.isEmpty()){
            logger.info("주문 {}의 결제가 시작되었습니다.", payment.orderId.id)
            //금액을 빼고 상태를 완료로 변경
            credit.subtractCreditAmount(payment.price)
            payment.paymentStatus = PaymentStatus.COMPLETED
            return PaymentCompletedEvent(payment, LocalDateTime.now(), failureMessages)
        }
        else{
            //금액을 빼지 않고 상태를 실패로 변경
            logger.info("주문 {}의 결제가 실패했습니다.", payment.orderId.id)
            payment.paymentStatus = PaymentStatus.FAILED
            return PaymentFailedEvent(payment, LocalDateTime.now(), failureMessages)
        }
    }

    override fun validateAndCancelPayment(payment: Payment, credit: Credit, failureMessages: MutableList<String>): PaymentEvent {
        //결제 금액을 통해 결제의 유효성을 확인
        payment.validatePayment(failureMessages)

        //결제 금액을 복구
        addCredit(credit = credit, payment = payment)

        if(failureMessages.isEmpty()){
            logger.info("주문 {}의 결제가 취소되었습니다.", payment.orderId.id)
            payment.paymentStatus = PaymentStatus.CANCELLED
            return PaymentFailedEvent(payment, LocalDateTime.now(), failureMessages)
        }
        else{
            logger.info("주문 {}의 결제 취소가 실패했습니다.", payment.orderId.id)
            payment.paymentStatus = PaymentStatus.FAILED
            return PaymentFailedEvent(payment, LocalDateTime.now(), failureMessages)
        }
    }

    override fun deposit(credit: Credit, money: Money) {
        credit.addCreditAmount(money)
    }

    private fun validateCredit(payment: Payment, credit: Credit, failureMessages: MutableList<String>) {
        if(payment.price.isGreaterThan(credit.totalCreditAmount)){
            logger.error("{} 고객님의 잔액이 충분하지 않습니다", payment.customerId.id)
            failureMessages.add("${payment.customerId.id} 고객님의 잔액이 충분하지 않습니다")
        }
    }

    private fun addCredit(credit: Credit, payment: Payment){
        credit.addCreditAmount(payment.price)
    }

    override fun createUser(id: ObjectId): Credit {
        return Credit(
            customerId = CustomerId(id),
            totalCreditAmount = Money.ZERO
        )
    }
}