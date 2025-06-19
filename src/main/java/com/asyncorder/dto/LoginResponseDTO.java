package com.asyncorder.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    String message;
    String AccessToken;
    String RefreshToken;
}