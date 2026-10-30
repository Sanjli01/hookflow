
package com.hookflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test-receiver")
public class TestReceiverController {

    @PostMapping
    public ResponseEntity<Map<String, String>> receiveEvent(
            @RequestBody String payload) {

        System.out.println("Webhook received!");
        System.out.println("Payload: " + payload);

        return ResponseEntity.ok(
                Map.of("message", "Webhook received successfully")
        );
    }
}
