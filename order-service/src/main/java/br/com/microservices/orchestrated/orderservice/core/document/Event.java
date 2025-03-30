package br.com.microservices.orchestrated.orderservice.core.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "event")
public class Event {

    private String id;
    private String transactionId;
    private String orderId;
    private String source;
    private String status;
    private Order payload;
    private List<History> eventHistory;
    private LocalDateTime createdAt;
}
