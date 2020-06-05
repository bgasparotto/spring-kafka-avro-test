package com.bgasparotto.spring.kafka.avro.test.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.stereotype.Component;

@Component
public class AvroConsumerConfig {

    private final EmbeddedKafkaBroker embeddedKafkaBroker;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializerClass;

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializerClass;

    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    @Value("${spring.kafka.consumer.properties.specific.avro.reader}")
    private boolean specificAvroReader;

    @Autowired
    public AvroConsumerConfig(EmbeddedKafkaBroker embeddedKafkaBroker) {
        this.embeddedKafkaBroker = embeddedKafkaBroker;
    }

    public Map<String, Object> getConfig() {
        HashMap<String, Object> config = createDefaultConfig();
        addConsumerConfig(config);
        addAvroDeserializerConfig(config);

        return config;
    }

    private HashMap<String, Object> createDefaultConfig() {
        return new HashMap<>(KafkaTestUtils.consumerProps("test-consumer", "false", embeddedKafkaBroker));
    }

    private void addConsumerConfig(HashMap<String, Object> config) {
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializerClass);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializerClass);
    }

    private void addAvroDeserializerConfig(HashMap<String, Object> config) {
        config.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        config.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, specificAvroReader);
    }
}
