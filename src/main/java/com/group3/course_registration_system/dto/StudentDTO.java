package com.group3.course_registration_system.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class StudentDTO {
    @Schema(description = "Unique identity of the student", example = "1")
    private Long studentId;

    @Schema(description = "Full name of the student", example = "Alice Wang")
    private String name;
    
    private List<CourseDTO> registeredCourses; 

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseDTO> getRegisteredCourses() {
        return registeredCourses;
    }

    public void setRegisteredCourses(List<CourseDTO> registeredCourses) {
        this.registeredCourses = registeredCourses;
    }
}
