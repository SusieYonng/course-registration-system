package com.group3.course_registration_system.service;

import com.group3.course_registration_system.dto.LoginRequest;
import com.group3.course_registration_system.dto.LoginResponse;
import com.group3.course_registration_system.entity.User;
import com.group3.course_registration_system.repository.UserRepository;

import com.group3.course_registration_system.security.JwtTokenUtil;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // After successful authentication, get the user details
        System.out.println("Authentication successful for user: " + loginRequest.getUsername());
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Find the user by username and check if the role matches the provided role
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getRole().toString().equalsIgnoreCase(loginRequest.getRole())) {
            throw new RuntimeException("Role mismatch");
        }

        // Generate the JWT token with the user details and the role
        String token = jwtTokenUtil.generateTokenWithRole(userDetails, user.getRole().toString());

        // Return the token as part of the LoginResponse
        return new LoginResponse(token, user.getId(), user.getUsername(), user.getRole().toString());
    }

    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);

            // Add the token to the blacklist
            tokenBlacklistService.addToBlacklist(token, expirationDate);

            System.out.println("User " + username + " logged out successfully");
        } catch (Exception e) {
            // The token may have expired, but we are still trying to blacklist it
            // tokenBlacklistService.addToBlacklist(token, new Date());

            // Catch an invalid token and throw an exception
            System.out.println("Invalid JWT during logout: " + e.getMessage());
            throw new IllegalArgumentException("Invalid token");
        }
    }
}