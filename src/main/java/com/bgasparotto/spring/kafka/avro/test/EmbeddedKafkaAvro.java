package com.bgasparotto.spring.kafka.avro.test;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.bgasparotto.spring.kafka.avro.test.config.AvroConsumerConfig;
import com.bgasparotto.spring.kafka.avro.test.config.AvroProducerConfig;
import java.time.Duration;
import java.util.List;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.stereotype.Component;

@Component
public class EmbeddedKafkaAvro {

    private static final long DEFAULT_CONSUMER_TIMEOUT = Duration.of(5, SECONDS).toMillis();

    private final AvroConsumerConfig avroConsumerConfig;
    private final AvroProducerConfig avroProducerConfig;

    @Autowired
    public EmbeddedKafkaAvro(AvroConsumerConfig avroConsumerConfig, AvroProducerConfig avroProducerConfig) {
        this.avroConsumerConfig = avroConsumerConfig;
        this.avroProducerConfig = avroProducerConfig;
    }

    public <K, V> Producer<K, V> createProducer() {
        return new DefaultKafkaProducerFactory<K, V>(avroProducerConfig.getConfig()).createProducer();
    }

    public <K, V> void produce(String topic, K key, V value) {
        try (Producer<K, V> producer = createProducer()) {
            ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, value);
            producer.send(record);
            producer.flush();
        }
    }

    public <K, V> Consumer<K, V> createConsumer() {
        return new DefaultKafkaConsumerFactory<K, V>(avroConsumerConfig.getConfig()).createConsumer();
    }

    public <K, V> ConsumerRecord<K, V> consumeOne(String topic) {
        try (Consumer<K, V> consumer = createConsumer()) {
            consumer.subscribe(List.of(topic));
            return KafkaTestUtils.getSingleRecord(consumer, topic, DEFAULT_CONSUMER_TIMEOUT);
        }
    }
}
