package com.bgasparotto.spring.kafka.avro.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.bgasparotto.spring.kafka.avro.test.config.AvroConsumerConfig;
import com.bgasparotto.spring.kafka.avro.test.config.AvroProducerConfig;
import com.bgasparotto.spring.kafka.avro.test.message.TestMessage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {EmbeddedKafkaAvro.class, AvroConsumerConfig.class, AvroProducerConfig.class})
@ActiveProfiles("test")
@EmbeddedKafka
public class EmbeddedKafkaAvroTest {

    public static final String TEST_TOPIC = "message.test.topic";

    @Autowired
    private EmbeddedKafkaAvro embeddedKafkaAvro;

    @Test
    public void shouldInjectTheConfigObject() {
        assertThat(embeddedKafkaAvro).isNotNull();
    }

    @Test
    public void shouldCreateConsumer() {
        Consumer<String, TestMessage> consumer = embeddedKafkaAvro.createConsumer();

        assertThat(consumer).isNotNull();
    }

    @Test
    public void shouldCreateProducer() {
        Producer<String, TestMessage> producer = embeddedKafkaAvro.createProducer();

        assertThat(producer).isNotNull();
    }

    @Test
    public void shouldProduceAndConsumeRecord() {
        TestMessage message = buildTestMessage();
        embeddedKafkaAvro.produce(TEST_TOPIC, "test-message-id", message);

        ConsumerRecord<String, TestMessage> record = embeddedKafkaAvro.consumeOne(TEST_TOPIC);

        assertThat(record.key()).isEqualTo("test-message-id");
        assertThat(record.value()).isEqualTo(message);
    }

    private TestMessage buildTestMessage() {
        return TestMessage.newBuilder()
            .setDescription("Test Message")
            .build();
    }
}
