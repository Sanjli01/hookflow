
package com.hookflow.repository;

import com.hookflow.model.WebhookEvent;
import com.hookflow.model.DeliveryStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebhookEventRepository
        extends JpaRepository<WebhookEvent, Long> {

    List<WebhookEvent> findByStatusAndAttemptsLessThan(
            DeliveryStatus status,
            int attempts
    );
}
