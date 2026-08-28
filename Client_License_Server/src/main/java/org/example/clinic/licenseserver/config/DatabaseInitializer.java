package org.example.clinic.licenseserver.config;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Component
public class DatabaseInitializer {

    private static final String DB_URL = "jdbc:sqlite:licenses.db";

    @PostConstruct
    public void initialize() {
        String sql = """
                CREATE TABLE IF NOT EXISTS licenses (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    license_key TEXT NOT NULL UNIQUE,
                    customer_name TEXT NOT NULL,
                    computer_id TEXT,
                    activated INTEGER NOT NULL DEFAULT 0,
                    active INTEGER NOT NULL DEFAULT 1,
                    created_at TEXT NOT NULL,
                    activated_at TEXT
                )
                """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            statement.execute(sql);
            System.out.println("License database initialized.");

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Could not initialize license database: " + e.getMessage(), e
            );
        }
    }
}
