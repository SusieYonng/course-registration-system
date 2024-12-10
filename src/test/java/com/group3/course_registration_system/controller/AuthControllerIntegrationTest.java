package com.group3.course_registration_system.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.course_registration_system.dto.LoginRequest;
import com.group3.course_registration_system.dto.LoginResponse;
import com.group3.course_registration_system.service.TokenBlacklistService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @BeforeEach
    public void setup() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    public void testLoginLogoutProcess() throws Exception {
        try {
            // 1. The user logs in to obtain the JWT token
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername("admin");
            loginRequest.setPassword("admin123");
            loginRequest.setRole("ADMIN"); 

            MvcResult loginResult = mockMvc
                    .perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.token").exists()).andReturn();

            String content = loginResult.getResponse().getContentAsString();
            LoginResponse loginResponse = objectMapper.readValue(content, LoginResponse.class);
            String jwtToken = loginResponse.getToken();

            assertNotNull(jwtToken, "JWT token should not be null");

            // 2. Use a valid token to access the protected resource
            mockMvc.perform(get("/api/protected-resource").header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            // 3. The user logs out
            mockMvc.perform(post("/api/auth/logout").header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            // 4. Try to use the logged out token to access the protected resource
            mockMvc.perform(get("/api/protected-resource").header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isUnauthorized());

            // 5. Verify if the token has been added to the blacklist
            assertTrue(tokenBlacklistService.isTokenBlacklisted(jwtToken), "Token should be blacklisted");

            // 6. Attempt to log out using the expired token
            mockMvc.perform(post("/api/auth/logout").header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }

    }
}