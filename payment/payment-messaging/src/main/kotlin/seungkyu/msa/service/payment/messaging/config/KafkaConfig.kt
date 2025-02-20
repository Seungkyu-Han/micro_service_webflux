package seungkyu.msa.service.payment.messaging.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.SenderOptions
import seungkyu.msa.service.kafka.model.PaymentCancelledResponseAvroModel
import seungkyu.msa.service.kafka.model.PaymentCompletedResponseAvroModel
import seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel


@Configuration
@EnableKafka
class KafkaConfig {

    @Bean
    fun reactiveKafkaPaymentCompletedResponseAvroModelProducerTemplate(): ReactiveKafkaProducerTemplate<String, PaymentCompletedResponseAvroModel> =
        ReactiveKafkaProducerTemplate(SenderOptions.create(HashMap<String, Any>().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer::class)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer::class)
        }))

    @Bean
    fun reactiveKafkaPaymentCancelledResponseAvroModelProducerTemplate(): ReactiveKafkaProducerTemplate<String, PaymentCancelledResponseAvroModel> =
        ReactiveKafkaProducerTemplate(SenderOptions.create(HashMap<String, Any>().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer::class)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer::class)
        }))

    @Bean
    fun reactiveKafkaPaymentFailedResponseAvroModelProducerTemplate(): ReactiveKafkaProducerTemplate<String, PaymentFailedResponseAvroModel> =
        ReactiveKafkaProducerTemplate(SenderOptions.create(HashMap<String, Any>().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer::class)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer::class)
        }))

    @Bean
    fun reactiveKafkaConsumerTemplate(): ReactiveKafkaConsumerTemplate<String, String> =
        ReactiveKafkaConsumerTemplate(ReceiverOptions.create(HashMap<String, Any>().apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer::class)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroDeserializer::class)
        }))
}
