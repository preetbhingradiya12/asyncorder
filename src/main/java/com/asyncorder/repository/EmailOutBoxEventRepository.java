package com.asyncorder.repository;

import com.asyncorder.entity.emailOutBox.EmailOutBoxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmailOutBoxEventRepository extends JpaRepository<EmailOutBoxEvent, UUID> {
    List<EmailOutBoxEvent> findByProcessedFalse();
}
