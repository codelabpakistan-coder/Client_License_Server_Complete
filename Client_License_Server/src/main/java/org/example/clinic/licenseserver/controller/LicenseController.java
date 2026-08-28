package org.example.clinic.licenseserver.controller;

import jakarta.validation.Valid;
import org.example.clinic.licenseserver.model.ActivateLicenseRequest;
import org.example.clinic.licenseserver.model.ApiResponse;
import org.example.clinic.licenseserver.model.GenerateLicenseRequest;
import org.example.clinic.licenseserver.model.License;
import org.example.clinic.licenseserver.service.LicenseService;
import org.example.clinic.licenseserver.service.LicenseService.LicenseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/licenses")
@Validated
public class LicenseController {

    private final LicenseService service;

    public LicenseController(LicenseService service) {
        this.service = service;
    }

    // ADMIN: Generate a new license
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse> generate(
            @Valid @RequestBody GenerateLicenseRequest request) {

        License license = service.generateLicense(request.getCustomerName());

        return ResponseEntity.ok(
                new ApiResponse(true, "License generated successfully.", license)
        );
    }

    // CLIENT: Activate license on this computer
    @PostMapping("/activate")
    public ResponseEntity<ApiResponse> activate(
            @Valid @RequestBody ActivateLicenseRequest request) {

        License license = service.activate(
                request.getLicenseKey(),
                request.getComputerId()
        );

        return ResponseEntity.ok(
                new ApiResponse(true, "License activated successfully.", license)
        );
    }

    // CLIENT: Validate license when application starts
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse> validate(
            @Valid @RequestBody ActivateLicenseRequest request) {

        License license = service.validate(
                request.getLicenseKey(),
                request.getComputerId()
        );

        return ResponseEntity.ok(
                new ApiResponse(true, "License is valid.", license)
        );
    }

    // ADMIN: See all licenses
    @GetMapping
    public ResponseEntity<ApiResponse> all() {
        return ResponseEntity.ok(
                new ApiResponse(true, "Licenses loaded.", service.getAllLicenses())
        );
    }

    // ADMIN: Disable a license
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse> deactivate(@PathVariable long id) {
        service.deactivate(id);

        return ResponseEntity.ok(
                new ApiResponse(true, "License deactivated.")
        );
    }

    // ADMIN: Re-enable a license
    @PostMapping("/{id}/reactivate")
    public ResponseEntity<ApiResponse> reactivate(@PathVariable long id) {
        service.reactivate(id);

        return ResponseEntity.ok(
                new ApiResponse(true, "License reactivated.")
        );
    }

    @ExceptionHandler(LicenseException.class)
    public ResponseEntity<ApiResponse> handleLicenseException(
            LicenseException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(
                        false,
                        e.getMessage(),
                        Map.of("code", e.getCode())
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiResponse(
                        false,
                        "Server error: " + e.getMessage()
                )
        );
    }
}
