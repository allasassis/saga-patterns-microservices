package br.com.microservices.orchestrated.orchestratorservice.config.kafka;

import br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics;
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

import static br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics.BASE_ORCHESTRATOR;
import static br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics.FINISH_FAIL;
import static br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics.FINISH_SUCCESS;
import static br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics.INVENTORY_FAIL;
import static br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics.INVENTORY_SUCCESS;
import static br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics.NOTIFY_ENDING;
import static br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics.PAYMENT_FAIL;
import static br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics.PAYMENT_SUCCESS;
import static br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics.PRODUCT_VALIDATION_FAIL;
import static br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics.PRODUCT_VALIDATION_SUCCESS;
import static br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics.START_SAGA;

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
    public NewTopic startSagaTopic() {
        return buildTopic(START_SAGA.getTopic());
    }

    @Bean
    public NewTopic orchestratorTopic() {
        return buildTopic(BASE_ORCHESTRATOR.getTopic());
    }

    @Bean
    public NewTopic finishSuccessTopic() {
        return buildTopic(FINISH_SUCCESS.getTopic());
    }

    @Bean
    public NewTopic finishFailTopic() {
        return buildTopic(FINISH_FAIL.getTopic());
    }

    @Bean
    public NewTopic inventorySuccessTopic() {
        return buildTopic(INVENTORY_SUCCESS.getTopic());
    }

    @Bean
    public NewTopic inventoryFailTopic() {
        return buildTopic(INVENTORY_FAIL.getTopic());
    }

    @Bean
    public NewTopic paymentSuccessTopic() {
        return buildTopic(PAYMENT_SUCCESS.getTopic());
    }

    @Bean
    public NewTopic paymentFailTopic() {
        return buildTopic(PAYMENT_FAIL.getTopic());
    }

    @Bean
    public NewTopic productValidationSuccessTopic() {
        return buildTopic(PRODUCT_VALIDATION_SUCCESS.getTopic());
    }

    @Bean
    public NewTopic productValidationFailTopic() {
        return buildTopic(PRODUCT_VALIDATION_FAIL.getTopic());
    }

    @Bean
    public NewTopic notifyEndingTopic() {
        return buildTopic(NOTIFY_ENDING.getTopic());
    }
}
