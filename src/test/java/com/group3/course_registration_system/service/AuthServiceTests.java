package com.group3.course_registration_system.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import com.group3.course_registration_system.dto.LoginRequest;
import com.group3.course_registration_system.dto.LoginResponse;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthServiceTests {

    @Autowired
    private AuthService authService;

    @Test
    public void testLoginSuccess() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");
        request.setRole("ADMIN"); 
        
        LoginResponse response = authService.login(request);
        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @Test
    public void testLoginFailureWithWrongPassword() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrongpassword");
        request.setRole("ADMIN"); 

        assertThrows(BadCredentialsException.class, () -> {
            authService.login(request);
        });
    }

    @Test
    public void testLoginFailureWithNonexistentUser() {
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistent");
        request.setPassword("password");
        request.setRole("STUDENT"); 

        assertThrows(BadCredentialsException.class, () -> {
            authService.login(request);
        });
    }
}