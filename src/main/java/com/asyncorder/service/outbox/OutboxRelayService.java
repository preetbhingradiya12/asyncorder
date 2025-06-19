package com.asyncorder.service.outbox;

import com.asyncorder.entity.order.Order;
import com.asyncorder.entity.order.OrderStatus;
import com.asyncorder.repository.OrderRepository;
import com.asyncorder.repository.OutBoxEventRepository;
import com.asyncorder.security.RabbitMQConfig;
import com.asyncorder.entity.outboxEvent.OutboxEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//@Service
public class OutboxRelayService {

    @Autowired
    private OutBoxEventRepository outboxRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(fixedRate = 5000)  // every 5 seconds
    public void relayOutboxEvents() {
        List<OutboxEvent> events = outboxRepository.findByProcessedFalse();

        for (OutboxEvent event : events) {
            try {

                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.ORDER_EXCHANGE,
                        RabbitMQConfig.ORDER_ROUTING_KEY,
                        event.getPayload()
                );

                Optional<Order> optionalOrder = orderRepository.findById(event.getAggregateId());

                if (optionalOrder.isPresent()) {

                    Order order = optionalOrder.get();
                    order.setStatus(OrderStatus.RELAYED);
                    orderRepository.save(order);

                } else {
                    System.err.println("Order not found for ID: " + event.getAggregateId());
                    continue;
                }


                event.setProcessed(true);
                outboxRepository.save(event);
                System.out.println("Relayed event: " + event.getId());
            } catch (Exception e) {
                System.err.println("Failed to relay event: " + e.getMessage());
            }
        }
    }
}
