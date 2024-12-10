package com.group3.course_registration_system.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.group3.course_registration_system.security.JwtTokenUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentServiceTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc is used for simulating HTTP requests in tests.

   // @Autowired
    //private ObjectMapper objectMapper; // ObjectMapper is used to serialize and deserialize objects to/from JSON.
    @Autowired
    private JwtTokenUtil jwtTokenUtil; // 用于生成模拟的 JWT token

    private String generateAdminToken() {
        // 模拟一个具有 ADMIN 角色的用户的 JWT token
        UserDetails adminUser = User.builder()
                .username("admin")
                .password("password") // 密码只是占位，不实际验证
                .roles("ADMIN") // 模拟 ADMIN 角色
                .build();
        return jwtTokenUtil.generateTokenWithRole(adminUser, "ROLE_ADMIN");
    }
    /**
     * Test case: Fetch all students
     * This test simulates a GET request to /api/students and verifies the response.
     */
    @Test
    public void testGetAllStudents() throws Exception {
        String token = generateAdminToken();

        mockMvc.perform(get("/api/students") // Simulate GET request to /api/students
                .header("Authorization", "Bearer " + token) // add JWT token to header
                .contentType(MediaType.APPLICATION_JSON)) // Set the request content type to application/json
                .andDo(result -> {
                    // Print debug information
                    System.out.println("Response status: " + result.getResponse().getStatus());
                    System.out.println("Response content: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isOk()) // Expect HTTP status 200 (OK)
                .andExpect(jsonPath("$").isArray()) // Expect the response to be an array
                .andExpect(jsonPath("$[0].studentId").exists()) // Verify the first student has a studentId field
                .andExpect(jsonPath("$[0].name").exists()); // Verify the first student has a name field
    }

    /**
     * Test case: Fetch a student by ID
     * This test simulates a GET request to /api/students/{id} and verifies the response.
     */
    @Test
    public void testGetStudentById() throws Exception {
        String token = generateAdminToken();
        Long studentId = 1L; // Assume a student with ID 1 exists in the database

        mockMvc.perform(get("/api/students/{id}", studentId) // Simulate GET request to /api/students/1
                .header("Authorization", "Bearer " + token) // add JWT token to header
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    // Print debug information
                    System.out.println("Response status: " + result.getResponse().getStatus());
                    System.out.println("Response content: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isOk()) // Expect HTTP status 200 (OK)
                .andExpect(jsonPath("$.studentId").value(studentId)) // Verify the studentId matches
                .andExpect(jsonPath("$.name").exists()); // Verify the student has a name field
    }

    /**
     * Test case: Search for students by name
     * This test simulates a GET request to /api/students/search?name={name} and verifies the response.
     */
    @Test
    public void testSearchStudentsByName() throws Exception {
        String token = generateAdminToken();
        String searchName = "Alice"; // Name to search for

        mockMvc.perform(get("/api/students/search") // Simulate GET request to /api/students/search
                .param("name", searchName) // Add query parameter ?name=Alice
                .header("Authorization", "Bearer " + token) // add JWT token to header
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    // Print debug information
                    System.out.println("Response status: " + result.getResponse().getStatus());
                    System.out.println("Response content: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isOk()) // Expect HTTP status 200 (OK)
                .andExpect(jsonPath("$").isArray()) // Expect the response to be an array
                .andExpect(jsonPath("$[0].name").value(containsString(searchName))); // Verify the first student's name matches
    }
}

