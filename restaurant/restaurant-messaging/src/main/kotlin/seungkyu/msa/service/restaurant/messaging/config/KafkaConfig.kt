package seungkyu.msa.service.restaurant.messaging.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.SenderOptions
import seungkyu.msa.service.kafka.model.RestaurantApprovalResponseAvroModel


@Configuration
@EnableKafka
class KafkaConfig {

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

    @Bean
    fun reactiveKafkaRestaurantApprovalResponseAvroModelProducerTemplate(): ReactiveKafkaProducerTemplate<String, RestaurantApprovalResponseAvroModel> =
        ReactiveKafkaProducerTemplate(SenderOptions.create(HashMap<String, Any>().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer::class.java)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer::class.java)
            put("schema.registry.url", "http://localhost:8081")
        }))

    @Bean
    fun reactiveKafkaConsumerTemplate(): ReactiveKafkaConsumerTemplate<String, String> =
        ReactiveKafkaConsumerTemplate(ReceiverOptions.create(HashMap<String, Any>().apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer::class)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroDeserializer::class)
        }))
}
