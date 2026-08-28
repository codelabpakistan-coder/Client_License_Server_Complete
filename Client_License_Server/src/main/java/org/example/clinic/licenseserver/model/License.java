package org.example.clinic.licenseserver.model;

public class License {

    private long id;
    private String licenseKey;
    private String customerName;
    private String computerId;
    private boolean activated;
    private boolean active;
    private String createdAt;
    private String activatedAt;

    public License() {
    }

    public License(long id, String licenseKey, String customerName,
                   String computerId, boolean activated, boolean active,
                   String createdAt, String activatedAt) {
        this.id = id;
        this.licenseKey = licenseKey;
        this.customerName = customerName;
        this.computerId = computerId;
        this.activated = activated;
        this.active = active;
        this.createdAt = createdAt;
        this.activatedAt = activatedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getComputerId() {
        return computerId;
    }

    public void setComputerId(String computerId) {
        this.computerId = computerId;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(String activatedAt) {
        this.activatedAt = activatedAt;
    }
}
