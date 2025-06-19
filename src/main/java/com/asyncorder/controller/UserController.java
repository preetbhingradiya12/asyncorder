package com.asyncorder.controller;

import com.asyncorder.dto.LoginDTO;
import com.asyncorder.dto.LoginResponseDTO;
import com.asyncorder.dto.UserDTO;
import com.asyncorder.entity.User;
import com.asyncorder.jwtutil.CustomPrincipal;
import com.asyncorder.respone.Response;
import com.asyncorder.respone.getUserResponse;
import com.asyncorder.service.abstactlayer.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDTO userDto) {
        User registeredUser = userService.registerUser(userDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDto){
        LoginResponseDTO message = userService.loginUser(loginDto);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/detail")
    public ResponseEntity<getUserResponse> userGetDetail(Authentication authentication){
        CustomPrincipal object = (CustomPrincipal) authentication.getPrincipal();
        getUserResponse response = userService.getUserDetail(object);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> userLogout(Authentication authentication){
        CustomPrincipal object = (CustomPrincipal) authentication.getPrincipal();
        Response response = userService.logoutUser(object);
        return ResponseEntity.ok(response);
    }

}
