package br.com.microservices.orchestrated.orchestratorservice.core.producer;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class SagaOrchestratorProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEvent(String event, String topic) {
        try {
            kafkaTemplate.send(topic, event);
            log.info("Sending event: {}, to topic: {}", event, topic);
        } catch (Exception e) {
            log.error("Error sending event: {}, to topic: {}", event, topic, e);
        }
    }
}
