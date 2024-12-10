package com.group3.course_registration_system.util;

import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties properties = new Properties();
    
    static {
        try {
            properties.load(new ClassPathResource("application.properties").getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }
    
    public static String getJdbcUrl() {
        return properties.getProperty("spring.datasource.url");
    }
    
    public static String getUsername() {
        return properties.getProperty("spring.datasource.username");
    }
    
    public static String getPassword() {
        return properties.getProperty("spring.datasource.password");
    }
}