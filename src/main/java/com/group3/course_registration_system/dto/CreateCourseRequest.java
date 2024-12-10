package com.group3.course_registration_system.dto;

import com.group3.course_registration_system.enums.CourseStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCourseRequest {
    @NotBlank(message = "Course name cannot be blank")
    @Schema(description = "Name of the course, required", example = "Java Basics")
    private String courseName;

    @Schema(description = "Description of the course, optional", example = "Introduction to Java")
    private String description;

    @NotBlank(message = "Schedule cannot be blank")
    @Schema(description = "Course schedule, required", example = "MW 9:30 AM - 11:00 AM")
    private String schedule;

    @NotNull(message = "Status cannot be null")
    @Schema(description = "Course status. Allow publishing on creating the course", example = "Only UNPUBLISHED or PUBLISHED")
    private CourseStatus status; // Only UNPUBLISHED or PUBLISHED

    // Getters and Setters
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
}
