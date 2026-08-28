package org.example.clinic.licenseserver.model;

import jakarta.validation.constraints.NotBlank;

public class ActivateLicenseRequest {

    @NotBlank(message = "License key is required")
    private String licenseKey;

    @NotBlank(message = "Computer ID is required")
    private String computerId;

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public String getComputerId() {
        return computerId;
    }

    public void setComputerId(String computerId) {
        this.computerId = computerId;
    }
}
