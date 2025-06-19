package com.asyncorder.entity.outboxEvent;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outboxevents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID aggregateId;         // e.g., Order ID

    private String eventType;         // e.g., "ORDER_CREATED"


    @Column(columnDefinition = "TEXT")
    private String payload;

    @CreationTimestamp
    private Instant createdAt;

    private boolean processed = false;
}
