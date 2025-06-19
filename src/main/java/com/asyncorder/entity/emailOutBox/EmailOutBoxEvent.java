package com.asyncorder.entity.emailOutBox;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "emailoutevent")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailOutBoxEvent {

    @Id
    private UUID eventId;

    private String toAddress;

    private String subject;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    @Column(name = "receive_at")
    private Long receivedAt;

    private boolean processed = false;

    @Column(name = "processed_at")
    private Long processedAt;
}
