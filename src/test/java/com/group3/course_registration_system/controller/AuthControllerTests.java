package com.group3.course_registration_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.course_registration_system.dto.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLoginEndpoint() throws Exception {
        // Create LoginRequest Object
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin123");
        loginRequest.setRole("ADMIN"); 
        System.out.println("Test case login with username: " + loginRequest.getUsername());
        System.out.println("Test case login with password: " + loginRequest.getPassword());
        System.out.println("Test case login with role: " + loginRequest.getRole());
        

        // Use the ObjectMapper to convert objects to JSON.
        String jsonRequest = objectMapper.writeValueAsString(loginRequest);
        System.out.println("Request JSON: " + jsonRequest);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andDo(result -> {
                    System.out.println("Response status: " + result.getResponse().getStatus());
                    System.out.println("Response content: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}