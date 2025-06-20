package com.asyncorder.dto;

import com.asyncorder.entity.enums.Role;
import lombok.*;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class UserDTO {
//    private String name;
//    private String email;
//    private String password;
//    private Role role;
//}

public class UserDTO {
    private String name;
    private String email;
    private String password;
    private Role role;

    UserDTO(String name, String email, String password, Role role){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    UserDTO(){

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateDTO {
        private String name;
        private String email;
        private Role role;
    }
}


