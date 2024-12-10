package com.group3.course_registration_system.controller;

import com.group3.course_registration_system.dto.LoginRequest;
import com.group3.course_registration_system.dto.LoginResponse;
import com.group3.course_registration_system.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Authentication Controller", description = "Manage user authentication, including login and logout.")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Handle user login
     */
    @Operation(
        summary = "User login", 
        description = "Authenticate a user by verifying their credentials and return a JWT token upon successful login."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("Login endpoint called with username: " + loginRequest.getUsername());
        LoginResponse response = authService.login(loginRequest);
        System.out.println("Generated token: " + response.getToken());
        return ResponseEntity.ok(response);
    }

    /**
     * Handle user logout
     */
    @Operation(
        summary = "User logout", 
        description = "Invalidate the current user's JWT token and log them out."
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        try {
            authService.logout(token);
        } catch (Exception e) {
            // Still return 200 OK regardless of the exception, but log
            System.out.println("Logout error: " + e.getMessage());
        }
        return ResponseEntity.ok("Logged out successfully");
    }
}