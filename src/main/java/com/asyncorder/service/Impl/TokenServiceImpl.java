package com.asyncorder.service.Impl;

import com.asyncorder.entity.Token;
import com.asyncorder.repository.TokenRepository;
import com.asyncorder.service.abstactlayer.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void saveToken(UUID userId, String jti) {
        Token token = Token.builder()
                .userId(userId)
                .jti(jti)
                .revoked(false)
                .createdAt(Instant.now())
                .build();
        tokenRepository.save(token);
    }
}
