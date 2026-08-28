package org.example.clinic.licenseserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ServerController {

    // Existing health check
    @GetMapping("/api/health")
    public Map<String, Object> health() {
        return Map.of(
                "success", true,
                "message", "Clinic License Server is running"
        );
    }

    // NEW: Home/Welcome endpoint for the root path
    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
                "success", true,
                "message", "Clinic License Server is up and running.",
                "endpoints", Map.of(
                        "health", "/api/health",
                        "generate", "/api/licenses/generate (POST)",
                        "activate", "/api/licenses/activate (POST)",
                        "validate", "/api/licenses/validate (POST)",
                        "list", "/api/licenses (GET)"
                )
        );
    }
}