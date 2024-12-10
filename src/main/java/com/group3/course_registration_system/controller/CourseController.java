package com.group3.course_registration_system.controller;

import com.group3.course_registration_system.dto.CourseDTO;
import com.group3.course_registration_system.dto.CreateCourseRequest;
import com.group3.course_registration_system.dto.UpdateCourseRequest;
import com.group3.course_registration_system.entity.Course;
import com.group3.course_registration_system.entity.Student;
import com.group3.course_registration_system.enums.OperationType;
import com.group3.course_registration_system.repository.StudentRepository;
import com.group3.course_registration_system.service.CourseManagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@SecurityRequirement(name = "Bearer Authentication") 
@Tag(name = "Course Management Controller", description = "Manage courses, including creation, updates, and publishing.")
public class CourseController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseManagementService courseManagementService;

    // Add a Course
    @Operation(
        summary = "Add a new course",
        description = "Create a new course by providing course details such as name, description, schedule, and status."
    )
    @PostMapping
    public ResponseEntity<CourseDTO> addCourse(@RequestBody @Valid CreateCourseRequest request) {
        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setSchedule(request.getSchedule());
        course.setStatus(request.getStatus());

        Course createdCourse = courseManagementService.addCourse(course);
        return ResponseEntity.ok(convertToDTO(createdCourse));
    }

    @Operation(
        summary = "Update course details",
        description = "Update an existing course by its ID. You can also add or remove a student from the course by specifying the operation type."
    )
    // Update a Course
    @PutMapping("/{courseId}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long courseId,
            @RequestBody @Valid UpdateCourseRequest request) {

        // Validate the path parameter matches the courseId of the request body or not
        if (!courseId.equals(request.getCourseId())) {
            throw new IllegalArgumentException("Path variable courseId does not match request body courseId");
        }

        // Convert UpdateCourseRequest to Course
        Course course = new Course();
        course.setCourseId(request.getCourseId());
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setSchedule(request.getSchedule());
        course.setStatus(request.getStatus());

        // 1. Update the course information
        Course updatedCourse = courseManagementService.updateCourse(courseId, course);

        // 2. Process student registration if operation is specified
        if (request.getOperation() != null && request.getUsername() != null) {
            // Get studentId from username
            Student student = studentRepository.findByUser_Username(request.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("Student not found with username: " + request.getUsername()));
            
            Long studentId = student.getStudentId();

            if (request.getOperation() == OperationType.ADD_STUDENT) {
                // Add a specified student to the current course to register
                courseManagementService.addStudentToCourse(courseId, studentId);
            } else if (request.getOperation() == OperationType.REMOVE_STUDENT) {
                // Removes a student from a course so that the student changes from registered to unregistered
                courseManagementService.removeStudentFromCourse(courseId, studentId);
            }
        }

        // Convert to DTO and return the result
        return ResponseEntity.ok(convertToDTO(updatedCourse));
    }

    // Delete a Course
    @Operation(
        summary = "Delete a course",
        description = "Delete an existing course by providing its ID. This action is irreversible."
    )
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseManagementService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    // Publish a Course
    @Operation(
        summary = "Publish a course",
        description = "Publish an existing course to make it visible and available for students."
    )
    @PutMapping("/{courseId}/publish")
    public ResponseEntity<Void> publishCourse(@PathVariable Long courseId) {
        courseManagementService.publishCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    // Unpublish a Course
    @Operation(
        summary = "Unpublish a course",
        description = "Unpublish an existing course to make it invisible and unavailable for students."
    )
    @PutMapping("/{courseId}/unpublish")
    public ResponseEntity<Void> unpublishCourse(@PathVariable Long courseId) {
        courseManagementService.unpublishCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    // Search courses (support keyword searching)
    @Operation(
        summary = "Search for courses",
        description = "Search for courses by keyword. If no keyword is provided, all courses will be returned."
    )
    @GetMapping
    public ResponseEntity<List<CourseDTO>> searchCourses(@RequestParam(required = false) String keyword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Course> courses = courseManagementService.searchCourses(keyword, username);
        List<CourseDTO> courseDTOs = courses.stream().map(this::convertToDTO) // Convert the entity class to a DTO
                .collect(Collectors.toList());
        return ResponseEntity.ok(courseDTOs);
    }

    // Obtain the course details by courseId
    @Operation(
        summary = "Get course details by ID",
        description = "Retrieve the details of a specific course by its unique ID."
    )
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long courseId) {
        return courseManagementService.getCourseById(courseId).map(course -> ResponseEntity.ok(convertToDTO(course))) // 将实体类转换为
                                                                                                                      // DTO
                .orElse(ResponseEntity.notFound().build());
    }

    // Auxiliary method: Convert Course to CourseDTO
    private CourseDTO convertToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setCourseId(course.getCourseId());
        dto.setCourseName(course.getCourseName());
        dto.setDescription(course.getDescription());
        dto.setSchedule(course.getSchedule());
        dto.setStatus(course.getStatus().toString());
        return dto;
    }
}
