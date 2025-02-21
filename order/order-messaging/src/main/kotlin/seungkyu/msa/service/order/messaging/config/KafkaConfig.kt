package seungkyu.msa.service.order.messaging.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.sender.SenderOptions
import seungkyu.msa.service.kafka.model.PaymentRequestAvroModel
import seungkyu.msa.service.kafka.model.RestaurantApprovalRequestAvroModel

@Configuration
@EnableKafka
class KafkaConfig {

    @Bean
    fun reactiveKafkaRestaurantApprovalRequestAvroModelProducerTemplate(): ReactiveKafkaProducerTemplate<String, RestaurantApprovalRequestAvroModel> =
        ReactiveKafkaProducerTemplate(SenderOptions.create(HashMap<String, Any>().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer::class.java)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer::class.java)
            put("schema.registry.url", "http://localhost:8081")
        }))

    @Bean
    fun reactiveKafkaPaymentRequestAvroModelProducerTemplate(): ReactiveKafkaProducerTemplate<String, PaymentRequestAvroModel> =
        ReactiveKafkaProducerTemplate(SenderOptions.create(HashMap<String, Any>().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer::class.java)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer::class.java)
            put("schema.registry.url", "http://localhost:8081")
        }))

    @Bean
    fun kafkaConsumerConfig(): Map<String, Any> {
        return mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to org.apache.kafka.common.serialization.StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to io.confluent.kafka.serializers.KafkaAvroDeserializer::class.java,
            "schema.registry.url" to "http://localhost:8081",
            "specific.avro.reader" to true
        )
    }
}