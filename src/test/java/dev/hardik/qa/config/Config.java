package dev.hardik.qa.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public final class Config {
    private static final Properties VALUES = new Properties();
    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) throw new IllegalStateException("config.properties is missing");
            VALUES.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    private Config() {}
    public static String get(String key) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) value = VALUES.getProperty(key);
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Missing configuration: " + key);
        return value;
    }
    public static Duration waitTime() { return Duration.ofSeconds(Long.parseLong(get("wait.seconds"))); }
    public static Duration pageLoadTime() { return Duration.ofSeconds(Long.parseLong(get("page.load.seconds"))); }
}
