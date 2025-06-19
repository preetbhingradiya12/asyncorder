package com.asyncorder.controller.product;

import com.asyncorder.dto.product.OrderDTO;
import com.asyncorder.dto.product.OrderResponse;
import com.asyncorder.jwtutil.CustomPrincipal;
import com.asyncorder.service.abstactlayer.order.orderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
public class OrderController{

    @Autowired
    private orderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<OrderResponse> createOrder(Authentication authentication, @Valid @RequestBody OrderDTO orderDto){
        CustomPrincipal object = (CustomPrincipal) authentication.getPrincipal();
        OrderResponse response = orderService.createOrder(object, orderDto);
        return ResponseEntity.ok(response);
    }
}