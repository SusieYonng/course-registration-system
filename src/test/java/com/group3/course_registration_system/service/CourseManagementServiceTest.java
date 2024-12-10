package com.group3.course_registration_system.service;

import com.group3.course_registration_system.enums.Role;
import com.group3.course_registration_system.entity.Course;
import com.group3.course_registration_system.enums.CourseStatus;
import com.group3.course_registration_system.entity.User;
import com.group3.course_registration_system.repository.CourseRepository;
import com.group3.course_registration_system.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CourseManagementServiceTest {

    @MockBean
    private CourseRepository courseRepository;  // Mocking the courseRepository

    @MockBean
    private UserRepository userRepository; // Mocking the userRepository

    @Autowired
    private CourseManagementService courseManagementService;

    @BeforeEach
    void setUp() {
        // This ensures that the mocks are correctly initialized by Spring
    }

    @Test
    void testAddCourse() {
        Course course = new Course();
        course.setCourseName("Java Basics");
        course.setSchedule("Mon 10:00-12:00");

        when(courseRepository.save(course)).thenReturn(course);

        Course savedCourse = courseManagementService.addCourse(course);

        assertNotNull(savedCourse);
        assertEquals("Java Basics", savedCourse.getCourseName());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testUpdateCourse() {
        Course existingCourse = new Course();
        existingCourse.setCourseId(1L);
        existingCourse.setCourseName("Old Name");

        Course updatedCourse = new Course();
        updatedCourse.setCourseName("New Name");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(existingCourse)).thenReturn(existingCourse);

        Course result = courseManagementService.updateCourse(1L, updatedCourse);

        assertEquals("New Name", result.getCourseName());
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(existingCourse);
    }

    @Test
    void testDeleteCourse() {
        when(courseRepository.existsById(1L)).thenReturn(true);

        courseManagementService.deleteCourse(1L);

        verify(courseRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void testSearchCourses() {
        // Create mock courses
        Course course1 = new Course();
        course1.setCourseName("Java Basics");
        course1.setStatus(CourseStatus.PUBLISHED);
    
        Course course2 = new Course();
        course2.setCourseName("Advanced Java");
        course2.setStatus(CourseStatus.PUBLISHED);
    
        // Create mock user for student
        User mockStudent = new User();
        mockStudent.setUsername("student1");
        mockStudent.setRole(Role.STUDENT);
    
        // Create mock user for admin
        User mockAdmin = new User();
        mockAdmin.setUsername("admin");
        mockAdmin.setRole(Role.ADMIN);
    
        // Mock the behavior of the userRepository
        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(mockStudent));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(mockAdmin));
    
        // Mock behavior for courses when no keyword is provided
        when(courseRepository.findByStatus(CourseStatus.PUBLISHED)).thenReturn(Arrays.asList(course1, course2));
    
        // Test for a student with no keyword
        List<Course> courses = courseManagementService.searchCourses(null, "student1");
        assertEquals(2, courses.size());
        verify(courseRepository, times(1)).findByStatus(CourseStatus.PUBLISHED);
    
        // Mock behavior for courses when a keyword is provided
        when(courseRepository.findByCourseNameContainingIgnoreCaseAndStatus("Java", CourseStatus.PUBLISHED))
                .thenReturn(Arrays.asList(course1, course2));
    
        // Test for a student with a keyword
        courses = courseManagementService.searchCourses("Java", "student1");
        assertEquals(2, courses.size());
        verify(courseRepository, times(1)).findByCourseNameContainingIgnoreCaseAndStatus("Java", CourseStatus.PUBLISHED);
    
        // Mock behavior for admin with no keyword
        when(courseRepository.findByStatusNot(CourseStatus.REMOVED)).thenReturn(Arrays.asList(course1, course2));
    
        // Test for an admin with no keyword
        courses = courseManagementService.searchCourses(null, "admin");
        assertEquals(2, courses.size());
        verify(courseRepository, times(1)).findByStatusNot(CourseStatus.REMOVED);
    
        // Mock behavior for admin with a keyword
        when(courseRepository.findByCourseNameContainingIgnoreCaseAndStatusNot("Java", CourseStatus.REMOVED))
                .thenReturn(Arrays.asList(course1, course2));
    
        // Test for an admin with a keyword
        courses = courseManagementService.searchCourses("Java", "admin");
        assertEquals(2, courses.size());
        verify(courseRepository, times(1)).findByCourseNameContainingIgnoreCaseAndStatusNot("Java", CourseStatus.REMOVED);
    }
    

}
