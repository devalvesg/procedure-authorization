package br.com.procedureauthorization.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = AppConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar application.properties", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}