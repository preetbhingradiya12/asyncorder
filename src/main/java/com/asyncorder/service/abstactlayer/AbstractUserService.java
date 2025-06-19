package com.asyncorder.service.abstactlayer;

import com.asyncorder.entity.Token;
import com.asyncorder.entity.User;
import com.asyncorder.exception.EmailAlreadyExistsException;
import com.asyncorder.exception.EmailNotFoundException;
import com.asyncorder.jwtutil.JwtAuth;
import com.asyncorder.repository.TokenRepository;
import com.asyncorder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class AbstractUserService {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TokenRepository tokenRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected JwtAuth jwtAuthService;

    protected void validateEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    protected User getUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("User not found with email: " + email));
    }

    protected User getUserByIdOrThrow(UUID userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    protected Token getUserToken(String jti){
        return tokenRepository.findByJti(jti)
                .orElseThrow(() -> new RuntimeException("User Token expire"));
    }

    protected User hashPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }
}
