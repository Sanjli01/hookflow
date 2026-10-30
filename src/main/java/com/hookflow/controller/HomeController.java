package com.hookflow.controller;

import com.hookflow.repository.WebhookEventRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    private final WebhookEventRepository webhookEventRepository;

    public HomeController(WebhookEventRepository webhookEventRepository) {
        this.webhookEventRepository = webhookEventRepository;
    }

    @GetMapping("/")
    public String home(Model model) {

        var events = webhookEventRepository.findAll();

        long totalEvents = events.size();
        long deliveredEvents = events.stream()
                .filter(event -> event.getStatus().name().equals("SUCCESS"))
                .count();
        long failedEvents = events.stream()
                .filter(event -> event.getStatus().name().equals("FAILED"))
                .count();

        model.addAttribute("events", events);
        model.addAttribute("totalEvents", totalEvents);
        model.addAttribute("deliveredEvents", deliveredEvents);
        model.addAttribute("failedEvents", failedEvents);

        return "index";
    }

    @GetMapping("/webhooks/{id}")
    public String webhookDetails(@PathVariable Long id, Model model) {

        var event = webhookEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Webhook event not found"));

        model.addAttribute("event", event);

        return "webhook-details";
    }
}