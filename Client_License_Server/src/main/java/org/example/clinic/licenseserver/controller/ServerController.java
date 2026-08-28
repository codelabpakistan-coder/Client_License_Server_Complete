package org.example.clinic.licenseserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ServerController {

    @GetMapping("/api/health")
    public Map<String, Object> health() {
        return Map.of(
                "success", true,
                "message", "Clinic License Server is running"
        );
    }
}
