package seungkyu.msa.service.kafka.consumer

import org.apache.avro.specific.SpecificRecordBase

interface KafkaConsumer<T: SpecificRecordBase> {

    fun receive(values: List<T>, keys: List<String>, partitions: List<Int>, offsets: List<Long>)
}