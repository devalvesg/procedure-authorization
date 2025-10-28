package br.com.procedureauthorization.config;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String DEFAULT_JDBC_URL =
            "jdbc:postgresql://localhost:5432/procedure_authorization";
    private static final String DEFAULT_USER = "admin";
    private static final String DEFAULT_PASS = "admin123";

    private final String jdbcUrl;
    private final String jdbcUser;
    private final String jdbcPass;

    public DatabaseConfig() {
        this.jdbcUrl = getEnvOrDefault("JDBC_URL", DEFAULT_JDBC_URL);
        this.jdbcUser = getEnvOrDefault("JDBC_USER", DEFAULT_USER);
        this.jdbcPass = getEnvOrDefault("JDBC_PASS", DEFAULT_PASS);
        loadDriver();
    }

    private void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver PostgreSQL not found", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
    }

    private String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
}
