package com.asyncorder.repository;

import com.asyncorder.entity.inbox.InBoxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InboxEventRepository extends JpaRepository<InBoxEvent, UUID> {
}
