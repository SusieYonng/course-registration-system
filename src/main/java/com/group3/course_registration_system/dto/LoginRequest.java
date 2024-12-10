package com.group3.course_registration_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Schema(description = "unique username for logging in", example = "string")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Schema(description = "password for logging in", example = "string")
    @NotBlank(message = "Password cannot be empty")
    private String password;

    @Schema(description = "user role for logging in", example = "ADMIN or STUDENT")
    @NotBlank(message = "Role cannot be empty")
    private String role; // role parameter: "ADMIN" or "STUDENT"

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}