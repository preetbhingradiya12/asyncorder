package com.asyncorder.service.abstactlayer.order;

import com.asyncorder.dto.product.OrderDTO;
import com.asyncorder.dto.product.OrderResponse;
import com.asyncorder.jwtutil.CustomPrincipal;

public interface orderService {
    OrderResponse createOrder(CustomPrincipal user , OrderDTO orderDTO);
}
