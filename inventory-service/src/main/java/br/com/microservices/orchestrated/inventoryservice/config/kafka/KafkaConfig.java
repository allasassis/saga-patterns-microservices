package br.com.microservices.orchestrated.inventoryservice.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.topic.orchestrator}")
    private String orchestratorTopic;

    @Value("${spring.kafka.topic.inventory-success}")
    private String inventorySuccessTopic;

    @Value("${spring.kafka.topic.inventory-fail}")
    private String inventoryFailTopic;

    private static final Integer PARTITION_COUNT = 1;
    private static final Integer REPLICA_COUNT = 1;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }

    private Map<String, Object> consumerProps() {
        return Map.of(
                "bootstrap.servers", bootstrapServers,
                "group.id", groupId,
                "auto.offset.reset", autoOffsetReset,
                "key.deserializer", StringDeserializer.class,
                "value.deserializer", StringDeserializer.class
        );
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerProps());
    }

    private Map<String, Object> producerProps() {
        return Map.of(
                "bootstrap.servers", bootstrapServers,
                "group.id", groupId,
                "auto.offset.reset", autoOffsetReset,
                "key.serializer", StringSerializer.class,
                "value.serializer", StringSerializer.class
        );
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    private NewTopic buildTopic(String name) {
        return TopicBuilder.name(name).replicas(REPLICA_COUNT).partitions(PARTITION_COUNT).build();
    }

    @Bean
    public NewTopic orchestratorTopic() {
        return buildTopic(orchestratorTopic);
    }

    @Bean
    public NewTopic inventorySuccessTopic() {
        return buildTopic(inventorySuccessTopic);
    }

    @Bean
    public NewTopic inventoryFailTopic() {
        return buildTopic(inventoryFailTopic);
    }
}
