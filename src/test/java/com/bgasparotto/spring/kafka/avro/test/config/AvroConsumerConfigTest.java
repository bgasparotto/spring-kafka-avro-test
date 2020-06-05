package com.bgasparotto.spring.kafka.avro.test.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {AvroConsumerConfig.class})
@ActiveProfiles("test")
@EmbeddedKafka
public class AvroConsumerConfigTest {

    @Autowired
    private AvroConsumerConfig avroConsumerConfig;

    @Value("${spring.embedded.kafka.brokers}")
    private String springEmbeddedKafkaBrokers;

    private Map<String, Object> config;

    @BeforeEach
    public void setUp() {
        config = avroConsumerConfig.getConfig();
    }

    @Test
    public void shouldInjectTheConfigObject() {
        assertThat(avroConsumerConfig).isNotNull();
    }

    @Test
    public void shouldContainKafkaBrokerSetAsSpringEmbeddedBroker() {
        assertThat(config).containsEntry("bootstrap.servers", springEmbeddedKafkaBrokers);
    }

    @Test
    public void shouldContainAutoResetOffsetSetToEarliest() {
        assertThat(config).containsEntry("auto.offset.reset", "earliest");
    }

    @Test
    public void shouldLoadConsumerConfigFromSpringApplicationValues() {
        assertThat(config).containsEntry(
            "key.deserializer",
            "org.apache.kafka.common.serialization.StringDeserializer"
        );
        assertThat(config).containsEntry("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
    }

    @Test
    public void shouldLoadAvroConfigFromSpringApplicationValues() {
        assertThat(config).containsEntry("schema.registry.url", "http://localhost:8081");
        assertThat(config).containsEntry("specific.avro.reader", true);
    }
}
