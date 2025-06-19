package com.asyncorder.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEventDTO {
    private UUID id;
    private String productName;
    private Integer quantity;
    private String item;
    private String status;
    private UUID userId;
}
