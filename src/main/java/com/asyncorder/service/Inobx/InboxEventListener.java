package com.asyncorder.service.Inobx;

import com.asyncorder.dto.product.OrderEventDTO;
import com.asyncorder.entity.User;
import com.asyncorder.entity.emailOutBox.EmailOutBoxEvent;
import com.asyncorder.entity.emailOutBox.OutboxStatus;
import com.asyncorder.entity.inbox.InBoxEvent;
import com.asyncorder.entity.order.Order;
import com.asyncorder.entity.order.OrderStatus;
import com.asyncorder.repository.EmailOutBoxEventRepository;
import com.asyncorder.repository.InboxEventRepository;
import com.asyncorder.repository.OrderRepository;
import com.asyncorder.repository.UserRepository;
import com.asyncorder.security.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InboxEventListener {


    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final InboxEventRepository inboxEventRepository;
    private final UserRepository userRepository;
    private final EmailOutBoxEventRepository emailOutBoxEventRepository;
    private final RabbitTemplate rabbitTemplate;


    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void handleOrderEvent(Message message, String payload) {
        try{
            System.out.println("Received order event: " + payload);

            OrderEventDTO event = objectMapper.readValue(payload, OrderEventDTO.class);

            //Get Order Id
            UUID orderId = event.getId();
            if (inboxEventRepository.existsById(orderId)) {
                System.out.println("Event " + orderId + " already processed. Skipping.");
                return;
            }

            InBoxEvent inBoxEvent = InBoxEvent.builder()
                    .eventId(orderId)
                    .eventType("ORDER_CREATED")
                    .receivedAt(Instant.now().toEpochMilli())
                    .build();

            inboxEventRepository.save(inBoxEvent);

            Optional<Order> order = orderRepository.findById(orderId);

            if(order.isPresent()){
                Order orderData = order.get();

                //Check if order not process
                if(orderData.getStatus() != OrderStatus.PROCESSING){
                    orderData.setStatus(OrderStatus.PROCESSING);
                    orderRepository.save(orderData);


                    // Mark inbox event as processed
                    inBoxEvent.setProcessed(true);
                    inBoxEvent.setProcessedAt(Instant.now().toEpochMilli());
                    inboxEventRepository.save(inBoxEvent);

                    //set email box events
                    Optional<User> user = userRepository.findById(orderData.getUser().getId());
                    EmailOutBoxEvent email = EmailOutBoxEvent.builder()
                                    .eventId(orderId)
                                    .status(OutboxStatus.PENDING)
                                    .receivedAt(Instant.now().toEpochMilli())
                                    .toAddress(user.get().getEmail())
                                    .subject("Order Create Message")
                                    .build();

                    rabbitTemplate.convertAndSend(
                            RabbitMQConfig.ORDER_EXCHANGE,
                            RabbitMQConfig.EMAIL_ROUTING_KEY,
                            email.getEventId().toString()
                    );

                    emailOutBoxEventRepository.save(email);

                    System.out.println("Order " + orderId + " updated to PROCESSING");
                } else {
                    System.out.println("Order " + orderId + " already PROCESSING, skipping...");
                }
            }else{
                System.err.println("Order not found: " + orderId);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
