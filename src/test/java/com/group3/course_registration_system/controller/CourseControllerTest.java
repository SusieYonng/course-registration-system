package com.group3.course_registration_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.course_registration_system.entity.Course;
import com.group3.course_registration_system.enums.CourseStatus;
import com.group3.course_registration_system.security.JwtTokenUtil;
import com.group3.course_registration_system.service.CourseManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseManagementService courseManagementService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String generateToken(String username, String role) {
        UserDetails userDetails = User.builder().username(username).password(passwordEncoder.encode("password"))
                .roles(role.replace("ROLE_", "")).build();
        return jwtTokenUtil.generateTokenWithRole(userDetails, role);
    }

    @Test
    void testAddCourseAsAdmin() throws Exception {
        Course course = new Course();
        course.setCourseName("Java Basics"); // Ensure this field is populated
        course.setSchedule("Mon 10:00-12:00");
        course.setStatus(CourseStatus.PUBLISHED); // Make sure status is set if required

        when(courseManagementService.addCourse(any(Course.class))).thenReturn(course);

        String token = generateToken("admin", "ROLE_ADMIN");

        mockMvc.perform(post("/api/courses").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.courseName").value("Java Basics")).andDo(MockMvcResultHandlers.print()); // To print the response body
                                                                                          // for debugging

        verify(courseManagementService, times(1)).addCourse(any(Course.class));
    }

    @Test
    void testAddCourseAsNonAdmin() throws Exception {
        Course course = new Course();
        course.setCourseName("Java Basics");

        String token = generateToken("student1", "ROLE_STUDENT");

        mockMvc.perform(post("/api/courses").header("Authorization", "Bearer " + token) // 非管理员的 JWT
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isForbidden()); // 验证没有权限的用户访问被拒绝

        verify(courseManagementService, never()).addCourse(any(Course.class));
    }

    @Test
    void testSearchCoursesAsAdminWithKeyword() throws Exception {
        Course course1 = new Course();
        course1.setCourseName("Java Basics");
        course1.setStatus(CourseStatus.PUBLISHED);
        Course course2 = new Course();
        course2.setCourseName("Advanced Java");
        course2.setStatus(CourseStatus.PUBLISHED);

        when(courseManagementService.searchCourses("Java", "admin")).thenReturn(Arrays.asList(course1, course2));

        String token = generateToken("admin", "ROLE_ADMIN");

        mockMvc.perform(get("/api/courses").header("Authorization", "Bearer " + token).param("keyword", "Java"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));

        verify(courseManagementService, times(1)).searchCourses("Java", "admin");
    }

    @Test
    void testSearchCoursesAsAdminWithNoKeyword() throws Exception {
        Course course1 = new Course();
        course1.setCourseName("Java Basics");
        course1.setStatus(CourseStatus.PUBLISHED);
        Course course2 = new Course();
        course2.setCourseName("Advanced Java");
        course2.setStatus(CourseStatus.PUBLISHED);

        when(courseManagementService.searchCourses(null, "admin")).thenReturn(Arrays.asList(course1, course2));

        String token = generateToken("admin", "ROLE_ADMIN");

        mockMvc.perform(get("/api/courses").header("Authorization", "Bearer " + token)).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(courseManagementService, times(1)).searchCourses(null, "admin");
    }

    @Test
    void testSearchCoursesAsStudentWithKeyword() throws Exception {
        Course course1 = new Course();
        course1.setCourseName("Java Basics");
        course1.setStatus(CourseStatus.PUBLISHED);
        Course course2 = new Course();
        course2.setCourseName("Advanced Java");
        course2.setStatus(CourseStatus.PUBLISHED);

        when(courseManagementService.searchCourses("Java", "student1")).thenReturn(Arrays.asList(course1, course2));

        String token = generateToken("student1", "ROLE_STUDENT");

        mockMvc.perform(get("/api/courses").header("Authorization", "Bearer " + token).param("keyword", "Java"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));

        verify(courseManagementService, times(1)).searchCourses("Java", "student1");
    }

    @Test
    void testSearchCoursesAsStudentWithNoKeyword() throws Exception {
        Course course1 = new Course();
        course1.setCourseName("Java Basics");
        course1.setStatus(CourseStatus.PUBLISHED);
        Course course2 = new Course();
        course2.setCourseName("Advanced Java");
        course2.setStatus(CourseStatus.PUBLISHED);

        when(courseManagementService.searchCourses(null, "student1")).thenReturn(Arrays.asList(course1, course2));

        String token = generateToken("student1", "ROLE_STUDENT");

        mockMvc.perform(get("/api/courses").header("Authorization", "Bearer " + token)).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(courseManagementService, times(1)).searchCourses(null, "student1");
    }
}
