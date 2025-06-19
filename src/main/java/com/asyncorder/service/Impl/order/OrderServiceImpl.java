package com.asyncorder.service.Impl.order;

import com.asyncorder.dto.product.OrderDTO;
import com.asyncorder.dto.product.OrderEventDTO;
import com.asyncorder.dto.product.OrderResponse;
import com.asyncorder.entity.User;
import com.asyncorder.entity.order.Order;
import com.asyncorder.entity.order.OrderStatus;
import com.asyncorder.jwtutil.CustomPrincipal;
import com.asyncorder.repository.OrderRepository;
import com.asyncorder.repository.OutBoxEventRepository;
import com.asyncorder.service.abstactlayer.AbstractUserService;
import com.asyncorder.service.abstactlayer.order.orderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.asyncorder.entity.outboxEvent.OutboxEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class OrderServiceImpl extends AbstractUserService implements orderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OutBoxEventRepository outboxRepository;

    @Override
    public OrderResponse createOrder(CustomPrincipal user,OrderDTO orderDTO) {
        UUID userId = user.getUserId();
        User userData = getUserByIdOrThrow(userId);

        Order order = Order.builder()
                .user(userData)
                .item(orderDTO.getItem())
                .productName(orderDTO.getProductName())
                .quantity(orderDTO.getQuantity())
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(order);
        OrderEventDTO eventDTO = new OrderEventDTO(
                order.getId(),
                order.getProductName(),
                order.getQuantity(),
                order.getItem(),
                order.getStatus().toString(),
                userData.getId()
        );
        ObjectMapper objectMapper = new ObjectMapper();

        String payload;
        try {
            payload = objectMapper.writeValueAsString(eventDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize order to JSON", e);
        }

        OutboxEvent event = OutboxEvent.builder()
                .aggregateId(order.getId())
                .eventType("ORDER_CREATED")
                .payload(payload)
                .build();
        outboxRepository.save(event);

        return new OrderResponse(200, "Order Create sucessfull", order);
    }
}
