package com.bgasparotto.spring.kafka.avro.test.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.stereotype.Component;

@Component
public class AvroProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaBrokers;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializerClass;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializerClass;

    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    public Map<String, Object> getConfig() {
        HashMap<String, Object> config = createDefaultConfig();
        addProducerConfig(config);
        addAvroSerializerConfig(config);

        return config;
    }

    private HashMap<String, Object> createDefaultConfig() {
        return new HashMap<>(KafkaTestUtils.senderProps(kafkaBrokers));
    }

    private void addProducerConfig(HashMap<String, Object> config) {
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializerClass);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClass);
    }

    private void addAvroSerializerConfig(HashMap<String, Object> config) {
        config.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
    }
}
