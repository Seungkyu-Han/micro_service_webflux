package seungkyu.msa.service.customer.messaging.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.sender.SenderOptions
import seungkyu.msa.service.kafka.model.CustomerCreateAvroModel

@Configuration
@EnableKafka
class KafkaConfig {


    @Bean
    fun reactiveKafkaCustomerCreateAvroModelProducerTemplate(): ReactiveKafkaProducerTemplate<String, CustomerCreateAvroModel> =
        ReactiveKafkaProducerTemplate(SenderOptions.create(HashMap<String, Any>().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer::class.java)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer::class.java)
            put("schema.registry.url", "http://localhost:8081")
        }))
}