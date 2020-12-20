# Spring Kafka Avro Test
Library with helper methods for testing producers and consumers of Kafka messages with Avro schemas.

## Adding to your project
#### 1. Add the dependency
Gradle:
```groovy
testImplementation 'com.bgasparotto:spring-kafka-avro-test:1.0.3'
```
Maven:
```xml
<dependency>
  <groupId>com.bgasparotto</groupId>
  <artifactId>spring-kafka-avro-test</artifactId>
  <version>1.0.3</version>
  <scope>test</scope>
</dependency>
```

#### 2. Add the library package to the test component scan configuration:
```java
@Profile("test")
@Configuration
@ComponentScan("com.bgasparotto.spring.kafka.avro.test")
public class TestConfiguration {
}
```

#### 3. Set Kafka `bootstrap.servers` to the embedded Kafka:
On the `@SpringBootTest` annotation of the test class:
```java
@SpringBootTest(properties = {"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"})
public class KafkaAvroTest {
}
```

or on the `application-test.xml`:
```yaml
spring:
  kafka:
    bootstrap-servers: ${spring.embedded.kafka.brokers}
```

#### 4. Annotate your test with `@EmbeddedKafka` and autowire the component:
```java
@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka
@DirtiesContext
public class KafkaAvroTest {
    @Autowired
    private EmbeddedKafkaAvro embeddedKafkaAvro;
}
```

## Usage
Consume a message:
```java
ConsumerRecord<String, SomeType> consumedRecord = embeddedKafkaAvro.consumeOne("topic");
```

Produce a message:
```java
SomeType someType = ...
embeddedKafkaAvro.produce("topic", "message-key", someType);
```

## Notes
#### Minimum configuration
As this is a Kafka with Avro library, it expects the minimum configuration below:
```yaml
spring:
  kafka:
    consumer:
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: io.confluent.kafka.serializers.KafkaAvroDeserializer
      properties:
        specific:
          avro:
            reader: true
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: io.confluent.kafka.serializers.KafkaAvroSerializer
    listener:
      missing-topics-fatal: false
    properties:
      schema:
        registry:
          url: http://localhost:8081
```
Please refer to [this project's docker-compose](https://github.com/bgasparotto/spring-kafka-avro-test/blob/master/docker-compose.yml) for a working example of Zookeeper, Kafka and Schema-registry.

#### Adding `${spring.embedded.kafka.brokers}` to your `application-test.yml` profile
When you choose to set your kafka brokers to `${spring.embedded.kafka.brokers}` on the
`application-test.yml` instead of adding the property to `@SpringBootTest`, please bear in mind that
any test class using the `test` profile annotated with `@SpringBootTest` will have to provided an
embedded Kafka container so the application contexts loads, by either:
- Also annotating the test class with `@EmbeddedKafka` or 
- Specifying the test components with `SpringBootTest(classes={ClassUnderTest.class})` to
  narrow-down the application context.

In any case, it's a good practise to load as fewer components as necessary for your test, so always
prefer more specific contexts such as `@WebMvcTest`, `@RestClientTest`, etc instead of the wider
`@SpringBootTest`.

#### Dirty Spring's Application context when using `@EmbeddedKafka`
If you observe log errors when the embedded Kafka is shutting down, such as:
```
kafka.server.LogDirFailureChannel: Error while renaming dir for message.scheduler.run-hansard-update-0 in dir /tmp/kafka-11062061126913658073
java.nio.file.NoSuchFileException: /tmp/kafka-11062061126913658073/message.scheduler.run-hansard-update-0/00000000000000000001.snapshot
```
Add the annotation `@DirtiesContext` to your test class to tell Spring the context shouldn't be
cached.

## Building the project
Run docker-compose:
```shell script
git clone https://github.com/bgasparotto/spring-kafka-avro-test.git
cd spring-kafka-avro-test.git
docker-compose up -d
```

Publish on the local Maven repository:
```shell script
./gradlew publishToMavenLocal
```
