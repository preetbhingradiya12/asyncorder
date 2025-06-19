package com.asyncorder.entity.inbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name =  "inboxevents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InBoxEvent {

    @Id
    private UUID eventId;

    @Column(name = "eventType", nullable = false)
    private String eventType;

    @Column(name = "received_at")
    private long receivedAt;

    private boolean processed = false;

    @Column(name = "processed_at")
    private long processedAt;
}
