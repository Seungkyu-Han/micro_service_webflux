package seungkyu.msa.service.payment.service.helper

import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import seungkyu.msa.service.common.status.PaymentStatus
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.common.valueObject.OrderId
import seungkyu.msa.service.payment.domain.entity.Payment
import seungkyu.msa.service.payment.service.dto.PaymentRequestDto

@Component
class PaymentDataMapper {

    fun paymentRequestDtoToPayment(paymentRequestDto: PaymentRequestDto): Payment {
        return Payment(
            id = OrderId(id = ObjectId(paymentRequestDto.id)),
            customerId = CustomerId(id = ObjectId(paymentRequestDto.customerId)),
            price = Money(paymentRequestDto.price),
            paymentStatus = PaymentStatus.UNKNOWN
        )
    }
}