
package com.hookflow.controller;

import com.hookflow.dto.WebhookRequest;
import com.hookflow.model.WebhookEvent;
import com.hookflow.service.WebhookService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    // Create a webhook event
    @PostMapping
    public ResponseEntity<WebhookEvent> createEvent(
            @Valid @RequestBody WebhookRequest request) {

        WebhookEvent event = webhookService.createEvent(
                request.getEventType(),
                request.getDestinationUrl(),
                request.getPayload()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(event);
    }

    // Get all webhook events
    @GetMapping
    public ResponseEntity<List<WebhookEvent>> getAllEvents() {

        return ResponseEntity.ok(
                webhookService.getAllEvents()
        );
    }

    // Get a webhook event by ID
    @GetMapping("/{id}")
    public ResponseEntity<WebhookEvent> getEventById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                webhookService.getEventById(id)
        );
    }

    
    // Manually trigger webhook delivery
    @PostMapping("/{id}/deliver")
    public ResponseEntity<WebhookEvent> deliverEvent(
            @PathVariable Long id) {

        WebhookEvent event = webhookService.deliverEvent(id);

        return ResponseEntity.ok(event);
    }

}
