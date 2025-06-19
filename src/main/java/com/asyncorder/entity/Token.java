package com.asyncorder.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;

    @Column(nullable = false, unique = true)
    private String jti;

    private boolean revoked;

    private Instant createdAt;
}
