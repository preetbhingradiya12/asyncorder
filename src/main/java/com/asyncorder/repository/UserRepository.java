package com.asyncorder.repository;

import com.asyncorder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
}
