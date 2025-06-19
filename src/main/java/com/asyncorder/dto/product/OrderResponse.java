package com.asyncorder.dto.product;


import com.asyncorder.entity.order.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Number statusCode;
    private String message;
    private Order data;
}
