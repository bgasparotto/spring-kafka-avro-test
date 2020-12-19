# Spring Kafka Avro Test
Library with helper methods for testing producers and consumers of Kafka messages with Avro schemas.

## Adding to your project
#### 1. Add the dependency
Gradle:
```groovy
testImplementation 'com.bgasparotto:spring-kafka-avro-test:1.0.2'
```
Maven:
```xml
<dependency>
  <groupId>com.bgasparotto</groupId>
  <artifactId>spring-kafka-avro-test</artifactId>
  <version>1.0.2</version>
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

#### 3. Set Kafka `bootstrap.servers` to th embedded Kafka:
`application-test.xml`:
```yaml
spring:
  kafka:
    bootstrap-servers: ${spring.embedded.kafka.brokers}
```
or set on the test properties:
```java
@SpringBootTest(properties = {"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"})
public class KafkaAvroTest {
}
```

#### 4. Annotate your test with `@EmbeddedKafka` and autowire the component:
```java
@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka
public class KafkaAvroTest {
    @Autowired
    private EmbeddedKafkaAvro embeddedKafkaAvro;
}
```

## Usage
Consume a message:
```java
SomeType someType = ...
embeddedKafkaAvro.produce("topic", "message-key", someType);
```

Produce a message:
```java
ConsumerRecord<String, SomeType> consumedRecord = embeddedKafkaAvro.consumeOne("topic");
```

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
