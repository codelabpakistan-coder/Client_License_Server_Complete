package org.example.clinic.licenseserver.service;

import org.example.clinic.licenseserver.model.License;
import org.example.clinic.licenseserver.repository.LicenseRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class LicenseService {

    private static final String ALPHANUMERIC = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private final SecureRandom random = new SecureRandom();
    private final LicenseRepository repository;

    public LicenseService(LicenseRepository repository) {
        this.repository = repository;
    }

    public License generateLicense(String customerName) {
        String key;

        do {
            key = generateKey();
        } while (repository.findByKey(key).isPresent());

        return repository.saveNew(
                key,
                customerName.trim(),
                Instant.now().toString()
        );
    }

    public License activate(String licenseKey, String computerId) {
        String normalizedKey = licenseKey.trim().toUpperCase(Locale.ROOT);
        String normalizedComputerId = computerId.trim();

        Optional<License> optional = repository.findByKey(normalizedKey);

        if (optional.isEmpty()) {
            throw new LicenseException("INVALID_LICENSE", "License key does not exist.");
        }

        License license = optional.get();

        if (!license.isActive()) {
            throw new LicenseException("LICENSE_DISABLED", "This license has been disabled.");
        }

        if (!license.isActivated()) {
            boolean activated = repository.activate(
                    license.getId(),
                    normalizedComputerId,
                    Instant.now().toString()
            );

            if (!activated) {
                throw new LicenseException(
                        "ACTIVATION_FAILED",
                        "The license could not be activated."
                );
            }

            return repository.findById(license.getId()).orElseThrow();
        }

        if (normalizedComputerId.equals(license.getComputerId())) {
            return license;
        }

        throw new LicenseException(
                "ALREADY_ACTIVATED",
                "This license is already activated on another computer."
        );
    }

    public License validate(String licenseKey, String computerId) {
        String normalizedKey = licenseKey.trim().toUpperCase(Locale.ROOT);
        String normalizedComputerId = computerId.trim();

        Optional<License> optional = repository.findByKey(normalizedKey);

        if (optional.isEmpty()) {
            throw new LicenseException("INVALID_LICENSE", "License key does not exist.");
        }

        License license = optional.get();

        if (!license.isActive()) {
            throw new LicenseException("LICENSE_DISABLED", "This license has been disabled.");
        }

        if (!license.isActivated()) {
            throw new LicenseException("NOT_ACTIVATED", "This license has not been activated yet.");
        }

        if (!normalizedComputerId.equals(license.getComputerId())) {
            throw new LicenseException(
                    "WRONG_COMPUTER",
                    "This license belongs to another computer."
            );
        }

        return license;
    }

    public List<License> getAllLicenses() {
        return repository.findAll();
    }

    public void deactivate(long id) {
        if (!repository.deactivate(id)) {
            throw new LicenseException("NOT_FOUND", "License not found.");
        }
    }

    public void reactivate(long id) {
        if (!repository.reactivate(id)) {
            throw new LicenseException("NOT_FOUND", "License not found.");
        }
    }

    private String generateKey() {
        return group() + "-" + group() + "-" + group() + "-" + group();
    }

    private String group() {
        StringBuilder result = new StringBuilder(5);

        for (int i = 0; i < 5; i++) {
            result.append(ALPHANUMERIC.charAt(
                    random.nextInt(ALPHANUMERIC.length())
            ));
        }

        return result.toString();
    }

    public static class LicenseException extends RuntimeException {
        private final String code;

        public LicenseException(String code, String message) {
            super(message);
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
