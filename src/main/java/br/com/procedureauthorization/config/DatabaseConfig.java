package br.com.procedureauthorization.config;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private final String jdbcUrl;
    private final String jdbcUser;
    private final String jdbcPass;

    public DatabaseConfig() {
        this.jdbcUrl = AppConfig.get("jdbc.url");
        this.jdbcUser = AppConfig.get("jdbc.user");
        this.jdbcPass = AppConfig.get("jdbc.password");
        loadDriver();
    }

    private void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver PostgreSQL não encontrado", e);
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
