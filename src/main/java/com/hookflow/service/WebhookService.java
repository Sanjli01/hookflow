
package com.hookflow.service;

import com.hookflow.model.WebhookEvent;
import com.hookflow.model.DeliveryStatus;
import com.hookflow.repository.WebhookEventRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
public class WebhookService {

    private final WebhookEventRepository repository;
    private final RestClient restClient;

    private static final int MAX_ATTEMPTS = 3;

    // Constructor injection
    public WebhookService(
            WebhookEventRepository repository,
            RestClient.Builder restClientBuilder) {

        this.repository = repository;
        this.restClient = restClientBuilder.build();
    }

    // Create a webhook event
    public WebhookEvent createEvent(
            String eventType,
            String destinationUrl,
            String payload) {

        WebhookEvent event = new WebhookEvent();

        event.setEventType(eventType);
        event.setDestinationUrl(destinationUrl);
        event.setPayload(payload);
        event.setStatus(DeliveryStatus.PENDING);
        event.setAttempts(0);

        return repository.save(event);
    }

    // Retrieve all events
    public List<WebhookEvent> getAllEvents() {
        return repository.findAll();
    }

    // Retrieve event by ID
    public WebhookEvent getEventById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Webhook event not found"));
    }

    // Deliver or retry a webhook event
    public WebhookEvent deliverEvent(Long id) {

        WebhookEvent event = getEventById(id);

        // Prevent duplicate successful delivery
        if (event.getStatus() == DeliveryStatus.SUCCESS) {
            throw new IllegalStateException(
                    "Event already delivered successfully");
        }

        // Check maximum attempts
        if (event.getAttempts() >= MAX_ATTEMPTS) {
            throw new IllegalStateException(
                    "Maximum delivery attempts reached");
        }

        // Increment attempt count
        event.setAttempts(event.getAttempts() + 1);

        try {

            restClient.post()
                    .uri(event.getDestinationUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(event.getPayload())
                    .retrieve()
                    .toBodilessEntity();

            event.setStatus(DeliveryStatus.SUCCESS);

        } catch (RestClientException |
                 IllegalArgumentException exception) {

            event.setStatus(DeliveryStatus.FAILED);

            System.out.println(
                    "Webhook delivery failed: "
                    + exception.getMessage());
        }

        return repository.save(event);
    }
}
