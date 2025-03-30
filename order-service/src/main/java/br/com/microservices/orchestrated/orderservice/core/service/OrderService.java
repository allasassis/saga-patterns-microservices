package br.com.microservices.orchestrated.orderservice.core.service;

import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.document.Order;
import br.com.microservices.orchestrated.orderservice.core.dto.OrderRequest;
import br.com.microservices.orchestrated.orderservice.core.producer.SagaProducer;
import br.com.microservices.orchestrated.orderservice.core.repository.OrderRepository;
import br.com.microservices.orchestrated.orderservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final EventService eventService;
    private final SagaProducer sagaProducer;
    private final JsonUtil jsonUtil;
    private static final String TRANSACTION_ID_PATTERN = "%s_%s";

    public Event createPayload(Order order) {
        return eventService.save(Event
                .builder()
                .transactionId(order.getTransactionId())
                .orderId(order.getId())
                .payload(order)
                .createdAt(LocalDateTime.now())
                .build());
    }

    public Order createOrder(OrderRequest orderRequest) {
        Order order = orderRepository.save(Order
                .builder()
                .products(orderRequest.getProducts())
                .createdAt(LocalDateTime.now())
                .transactionId(
                        String.format(TRANSACTION_ID_PATTERN, Instant.now().toEpochMilli(), UUID.randomUUID())
                ).build()
        );
        sagaProducer.sendEvent(jsonUtil.toJson(order));
        return order;
    }

    public Order findById(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order findByTransactionId(String transactionId) {
        return orderRepository.findByTransactionId(transactionId);
    }
}
