package com.asyncorder.service.outbox;

import com.asyncorder.entity.order.OrderStatus;
import com.asyncorder.repository.OrderRepository;
import com.asyncorder.repository.OutBoxEventRepository;
import com.asyncorder.security.RabbitMQConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KafkaOutboxListener {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OutBoxEventRepository outBoxEventRepository;

    @KafkaListener(topics = "outbox.public.outbox_event", groupId = "order-relay-group")
    public void listen(String message) {
        try {
            System.out.println("Received CDC event from Kafka: " + message);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(message);
            String payload = json.get("payload").toString(); // fixes the issue
            System.out.println("KafkaListener sending to RabbitMQ: " + payload);

            // Relay to RabbitMQ
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_ROUTING_KEY,
                    payload
            );

            // Optional: mark order as RELAYED if needed
            UUID orderId = UUID.fromString(json.get("aggregateId").asText());
            orderRepository.findById(orderId).ifPresent(order -> {
                order.setStatus(OrderStatus.RELAYED);
                orderRepository.save(order);
            });

            // Optional: mark Outbox processed = true if you still persist it
            outBoxEventRepository.findById(orderId).ifPresent(outbox -> {
                outbox.setProcessed(true);
                outBoxEventRepository.save(outbox);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
