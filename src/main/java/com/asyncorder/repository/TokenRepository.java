package com.asyncorder.repository;

import com.asyncorder.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    Optional<Token> findByJti(String jti);
    Optional<Token> findByJtiAndUserIdAndRevoked(String jti, UUID userId, Boolean revoked);
}
