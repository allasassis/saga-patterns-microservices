package br.com.microservices.orchestrated.inventoryservice.core.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.orchestrator}")
    private String orchestratorTopic;

    public void sendEvent(String event) {
        try {
            kafkaTemplate.send(orchestratorTopic, event);
            log.info("Sending event: {}, to topic: {}", event, orchestratorTopic);
        } catch (Exception e) {
            log.error("Error sending event: {}, to topic: {}", event, orchestratorTopic, e);
        }
    }
}
