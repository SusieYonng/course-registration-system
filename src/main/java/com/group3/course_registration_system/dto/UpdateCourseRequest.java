package com.group3.course_registration_system.dto;

import com.group3.course_registration_system.enums.CourseStatus;
import com.group3.course_registration_system.enums.OperationType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for updating a course. It includes course information and optional operations for managing students.
 */
public class UpdateCourseRequest {

    /**
     * The unique identifier of the course.
     */
    @Schema(description = "Unique id of the course", example = "1")
    @NotNull(message = "Course ID cannot be null")
    private Long courseId;

    /**
     * The name of the course.
     */
    @Schema(description = "Name of the course, required", example = "Java Basics")
    @NotBlank(message = "Course name cannot be blank")
    private String courseName;

    /**
     * A brief description of the course.
     */
    @Schema(description = "Description of the course, optional", example = "Introduction to Java")
    private String description;

    /**
     * The schedule of the course, specifying the day and time.
     */    
    @Schema(description = "Course schedule, required", example = "MW 9:30 AM - 11:00 AM")
    @NotBlank(message = "Schedule cannot be blank")
    private String schedule;

    /**
     * The status of the course, either PUBLISHED or UNPUBLISHED.
     */
    @NotNull(message = "Status cannot be null")
    @Schema(description = "Course status. Allow to be removed after being created", example = "PUBLISHED/UNPUBLISHED/REMOVED")
    private CourseStatus status;

    /**
     * Optional operation type: 
     * - ADD_STUDENT for adding a student to the course.
     * - REMOVE_STUDENT for removing a student from the course.
     * - NO_OPERATION or null means no operation.
     */
    @Schema(description = "Operation on the course: add a student or remove a student from the course", example = "ADD_STUDENT/REMOVE_STUDENT/NO_OPERATION")
    private OperationType operation;

    /**
     * The username of the student involved in the operation, required if operation is 1 or 2.
     */
    @Schema(description = "Username of the student to add/remove from the course", example = "S20240001")
    private String username;

    // Getters and Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
