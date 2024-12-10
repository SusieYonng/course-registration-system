package com.group3.course_registration_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import com.group3.course_registration_system.util.JwtKeyGenerator;

@Configuration
public class JwtConfig {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @PostConstruct
    public void init() {
        // If no key is configured, a new key will be generated.
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            jwtSecret = JwtKeyGenerator.generateSecretKey();
            // Recommendation: Save the generated key to a secure configuration store.
            System.out.println("Generated new JWT secret key: " + jwtSecret);
        }
    }
}