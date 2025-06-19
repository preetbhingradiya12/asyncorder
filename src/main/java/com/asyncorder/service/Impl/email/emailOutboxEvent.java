package com.asyncorder.service.Impl.email;

import com.asyncorder.entity.emailOutBox.EmailOutBoxEvent;
import com.asyncorder.entity.emailOutBox.OutboxStatus;
import com.asyncorder.entity.order.Order;
import com.asyncorder.entity.order.OrderStatus;
import com.asyncorder.repository.EmailOutBoxEventRepository;
import com.asyncorder.repository.OrderRepository;
import com.asyncorder.security.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class emailOutboxEvent {
    private final EmailOutBoxEventRepository emailOutboxRepository;
    private final OrderRepository orderRepository;

    private final JavaMailSender mailSender;
    private Order order;
    private EmailOutBoxEvent event;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void relayEmails(String orderIdStr) {
        try{
            UUID orderId = UUID.fromString(orderIdStr);

            Optional<EmailOutBoxEvent> eventOpt = emailOutboxRepository.findById(orderId);
            if (eventOpt.isPresent() && eventOpt.get().isProcessed()) {
                System.err.println("This order is already process with processId : " + orderId);
                return;
            }
           event = eventOpt.get();

            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isEmpty()) {
                System.err.println("Order not found for ID: " + orderId);
                return;
            }

            order = orderOpt.get();
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(event.getToAddress());
            message.setSubject(event.getSubject());

            message.setText(
                    "Hello " + order.getUser().getName() + ",\n\n" +
                                "Thank you for your purchase!\n\n" +
                                "Here are the details of your order:\n" +
                                "--------------------------------------\n" +
                                "Order ID     : " + event.getEventId() + "\n" +
                                "Product Name : " + order.getProductName() + "\n" +
                                "Quantity     : " + order.getQuantity() + "\n" +
                                "Item Type    : " + order.getItem() + "\n" +
                                "Status       : " + order.getStatus() + "\n" +
                                "Placed At    : " + order.getCreatedAt() + "\n" +
                                "--------------------------------------\n\n" +
                                "We will notify you once your order is dispatched.\n\n" +
                                "If you have any questions, feel free to contact our support team.\n\n" +
                                "Best regards,\n" +
                                "DiamondBook Team"
                );

            mailSender.send(message);

            event.setProcessed(true);
            event.setStatus(OutboxStatus.SENT);
            event.setProcessedAt(Instant.now().toEpochMilli());

            emailOutboxRepository.save(event);

            System.out.println("✅ Email sent for order: " + orderId);
        } catch (Exception e) {
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);

            event.setStatus(OutboxStatus.FAILD);
            emailOutboxRepository.save(event);
            System.err.println("❌ Failed to send email: " + e.getMessage());
        }

    }
}
