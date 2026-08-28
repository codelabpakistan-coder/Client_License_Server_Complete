package org.example.clinic.licenseserver.repository;

import org.example.clinic.licenseserver.model.License;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LicenseRepository {

    private static final String DB_URL = "jdbc:sqlite:licenses.db";

    public LicenseRepository() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("SQLite JDBC driver not found.", e);
        }
    }

    public License saveNew(String licenseKey, String customerName, String createdAt) {
        String sql = """
                INSERT INTO licenses
                (license_key, customer_name, computer_id, activated, active, created_at, activated_at)
                VALUES (?, ?, NULL, 0, 1, ?, NULL)
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, licenseKey);
            ps.setString(2, customerName);
            ps.setString(3, createdAt);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return findById(keys.getLong(1)).orElseThrow();
                }
            }

            throw new SQLException("License was inserted but its ID could not be read.");

        } catch (SQLException e) {
            throw new IllegalStateException("Could not create license: " + e.getMessage(), e);
        }
    }

    public Optional<License> findByKey(String licenseKey) {
        String sql = "SELECT * FROM licenses WHERE license_key = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, licenseKey);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Could not find license: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    public Optional<License> findById(long id) {
        String sql = "SELECT * FROM licenses WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Could not find license: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    public List<License> findAll() {
        String sql = "SELECT * FROM licenses ORDER BY id DESC";
        List<License> licenses = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                licenses.add(map(rs));
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Could not load licenses: " + e.getMessage(), e);
        }

        return licenses;
    }

    public boolean activate(long id, String computerId, String activatedAt) {
        String sql = """
                UPDATE licenses
                SET computer_id = ?, activated = 1, activated_at = ?
                WHERE id = ? AND active = 1
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, computerId);
            ps.setString(2, activatedAt);
            ps.setLong(3, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new IllegalStateException("Could not activate license: " + e.getMessage(), e);
        }
    }

    public boolean deactivate(long id) {
        String sql = """
                UPDATE licenses
                SET active = 0
                WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new IllegalStateException("Could not deactivate license: " + e.getMessage(), e);
        }
    }

    public boolean reactivate(long id) {
        String sql = """
                UPDATE licenses
                SET active = 1
                WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new IllegalStateException("Could not reactivate license: " + e.getMessage(), e);
        }
    }

    private License map(ResultSet rs) throws SQLException {
        return new License(
                rs.getLong("id"),
                rs.getString("license_key"),
                rs.getString("customer_name"),
                rs.getString("computer_id"),
                rs.getInt("activated") == 1,
                rs.getInt("active") == 1,
                rs.getString("created_at"),
                rs.getString("activated_at")
        );
    }
}
