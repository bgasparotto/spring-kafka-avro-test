package com.bgasparotto.spring.kafka.avro.test.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {AvroProducerConfig.class})
@ActiveProfiles("test")
public class AvroProducerConfigTest {

    @Autowired
    private AvroProducerConfig avroProducerConfig;

    @Value("${spring.embedded.kafka.brokers}")
    private String springEmbeddedKafkaBrokers;

    private Map<String, Object> config;

    @BeforeEach
    public void setUp() {
        config = avroProducerConfig.getConfig();
    }

    @Test
    public void shouldInjectTheConfigObject() {
        assertThat(avroProducerConfig).isNotNull();
    }

    @Test
    public void shouldContainKafkaBrokerSetAsSpringEmbeddedBroker() {
        assertThat(config).containsEntry("bootstrap.servers", springEmbeddedKafkaBrokers);
    }

    @Test
    public void shouldLoadConsumerConfigFromSpringApplicationValues() {
        assertThat(config).containsEntry("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        assertThat(config).containsEntry("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
    }

    @Test
    public void shouldLoadAvroConfigFromSpringApplicationValues() {
        assertThat(config).containsEntry("schema.registry.url", "http://localhost:8081");
    }
}
