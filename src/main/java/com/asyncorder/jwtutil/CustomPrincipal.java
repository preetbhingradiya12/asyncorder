package com.asyncorder.jwtutil;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPrincipal {
    private UUID userId;
    private String jti;
    private Object role;
}
