package com.group3.course_registration_system.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import com.group3.course_registration_system.security.JwtTokenUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentCourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private String baseURL = "/api/students/student1/courses";

    private String generateStudentToken(String username) {
        UserDetails userDetails = User.builder()
                .username(username) // 模拟的 username
                .password("password") // 模拟密码
                .roles("STUDENT") // 角色为 STUDENT
                .build();
        return jwtTokenUtil.generateTokenWithRole(userDetails, "ROLE_STUDENT");
    }

    /**
     * 测试获取学生的已选课程
     */
    @Test
    public void testGetCourses() throws Exception {
        String token = generateStudentToken("student1");
        mockMvc.perform(get(baseURL)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    /**
     * 测试学生注册课程
     */
    @Test
    public void testRegisterCourse() throws Exception {
        String token = generateStudentToken("student1");
        mockMvc.perform(post(baseURL + "/1")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * 测试学生退选课程
     */
    @Test
    public void testDropCourse() throws Exception {
        String token = generateStudentToken("student1");
        mockMvc.perform(delete(baseURL + "/1")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void testInvalidStudentAccess() throws Exception {
        // JWT 中的 student1 与路径中的 studentId 不匹配
        String token = generateStudentToken("student2");

        mockMvc.perform(get(baseURL)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
