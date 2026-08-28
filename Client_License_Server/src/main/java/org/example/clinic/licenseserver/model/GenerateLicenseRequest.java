package org.example.clinic.licenseserver.model;

import jakarta.validation.constraints.NotBlank;

public class GenerateLicenseRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
