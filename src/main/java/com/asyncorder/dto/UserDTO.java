package com.asyncorder.dto;

import com.asyncorder.entity.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String name;
    private String email;
    private String password;
    private Role role;
}


