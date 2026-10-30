
package com.hookflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class WebhookRequest {

    @NotBlank(message = "Event type is required")
    private String eventType;

    @NotBlank(message = "Destination URL is required")
    @Pattern(
        regexp = "^https?://.+",
        message = "Destination must start with http:// or https://"
    )
    private String destinationUrl;

    @NotBlank(message = "Payload is required")
    private String payload;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDestinationUrl() {
        return destinationUrl;
    }

    public void setDestinationUrl(String destinationUrl) {
        this.destinationUrl = destinationUrl;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
