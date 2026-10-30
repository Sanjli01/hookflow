
package com.hookflow.service;

import com.hookflow.model.WebhookEvent;
import com.hookflow.model.DeliveryStatus;
import com.hookflow.repository.WebhookEventRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebhookRetryScheduler {

    private final WebhookEventRepository repository;
    private final WebhookService webhookService;

    public WebhookRetryScheduler(
            WebhookEventRepository repository,
            WebhookService webhookService) {

        this.repository = repository;
        this.webhookService = webhookService;
    }

    // Check for failed events every 10 seconds
    @Scheduled(fixedDelay = 10000)
    public void retryFailedEvents() {

        List<WebhookEvent> failedEvents =
                repository.findByStatusAndAttemptsLessThan(
                        DeliveryStatus.FAILED, 3
                );

        for (WebhookEvent event : failedEvents) {

            try {
                System.out.println(
                        "Retrying webhook event: " + event.getId()
                );

                webhookService.deliverEvent(event.getId());

            } catch (Exception exception) {

                System.out.println(
                        "Retry error for event "
                        + event.getId()
                        + ": "
                        + exception.getMessage()
                );
            }
        }
    }
}
