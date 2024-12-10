package com.group3.course_registration_system.controller;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group3.course_registration_system.dto.CourseDTO;
import com.group3.course_registration_system.entity.Student;
//import com.group3.course_registration_system.entity.Course;
import com.group3.course_registration_system.service.StudentCourseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.group3.course_registration_system.repository.StudentRepository;

@RestController
@RequestMapping("/api/students/{username}/courses")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Student-Course Controller", description = "Manage courses selected by students, including registration and dropping.")
public class StudentCourseController {

    private final StudentRepository studentRepository;
    private final StudentCourseService studentCourseService;

    public StudentCourseController(StudentCourseService studentCourseService, StudentRepository studentRepository) {
        this.studentCourseService = studentCourseService;
        this.studentRepository = studentRepository;
    }

    private Student validateStudentAccess(String username) {
        // Get the username (sub field) from the JWT
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // Verify if the username in the request path matches the username in the JWT
        if (!currentUsername.equals(username)) {
            throw new AccessDeniedException("Access denied: You can only access your own data.");
        }

        // Query the student entity using the username
        return studentRepository.findByUser_Username(username)
                .orElseThrow(() -> new AccessDeniedException("Access denied: Invalid student."));
    }

    /**
     * Retrieve the courses selected by a student
     */
    @Operation(summary = "Retrieve selected courses", description = "Retrieve the list of courses selected by the student identified by the username.")
    @GetMapping
    public List<CourseDTO> getCourses(@PathVariable String username) {
        Student student = validateStudentAccess(username);
        return studentCourseService.getCoursesByStudentId(student.getStudentId());
    }

    /**
     * Register a student for a course
     */
    @Operation(summary = "Register for a course", description = "Register the student identified by the username for the course identified by the courseId.")
    @PostMapping("/{courseId}")
    public void registerCourse(@PathVariable String username, @PathVariable Long courseId) {
        Student student = validateStudentAccess(username);
        studentCourseService.registerCourse(student.getStudentId(), courseId);
    }

    /**
     * Drop a course for a student
     */
    @Operation(summary = "Drop a course", description = "Drop the course identified by the courseId for the student identified by the username.")
    @DeleteMapping("/{courseId}")
    public void dropCourse(@PathVariable String username, @PathVariable Long courseId) {
        Student student = validateStudentAccess(username);
        studentCourseService.dropCourse(student.getStudentId(), courseId);
    }
}
