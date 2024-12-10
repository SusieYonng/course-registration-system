package com.group3.course_registration_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CourseDTO {
    @Schema(description = "Unique id of the course", example = "1")
    private Long courseId;
    @Schema(description = "Name of the course, required", example = "Java Basics")
    private String courseName;
    @Schema(description = "Description of the course, optional", example = "Introduction to Java")
    private String description;
    @Schema(description = "Course schedule, required", example = "MW 9:30 AM - 11:00 AM")
    private String schedule;
    @Schema(description = "Course status, required. Allow publishing on creating the course; allow to be removed after being created", example = "PUBLISHED/UNPUBLISHED/REMOVED")
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
