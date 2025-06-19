package com.asyncorder.entity.order;


public enum OrderStatus {
    PENDING,
    RELAYED,
    PROCESSING,
    COMPLETED,
    FAILED,
    CANCELLED
}