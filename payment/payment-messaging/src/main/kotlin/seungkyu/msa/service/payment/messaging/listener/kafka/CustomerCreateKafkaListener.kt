package seungkyu.msa.service.payment.messaging.listener.kafka

import org.bson.types.ObjectId
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import seungkyu.msa.service.common.valueObject.CustomerId
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.payment.domain.entity.Credit
import seungkyu.msa.service.payment.service.ports.input.message.listener.CustomerCreateMessageListener

@Component
class CustomerCreateKafkaListener(
    private val customerCreateMessageListener: CustomerCreateMessageListener
){

    @KafkaListener(id = "\${kafka.consumer.customer-consumer-group-id}",
        topics = ["\${kafka.topic.customer-create}"])
    fun receive(
        @Payload values: List<String>,
        @Header(KafkaHeaders.RECEIVED_KEY) keys: List<String>,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partitions: List<Int>,
        @Header(KafkaHeaders.OFFSET) offsets: List<Long>
    ) {
        values.forEach{
            customerCreateMessageListener.createCredit(
                Credit(
                    customerId = CustomerId(id = ObjectId(it)),
                    totalCreditAmount = Money.ZERO
                )
            ).subscribe()
        }
    }
}