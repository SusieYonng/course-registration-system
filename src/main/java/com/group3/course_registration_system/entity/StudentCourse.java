package com.group3.course_registration_system.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class StudentCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne //一个学生可以有多个选课记录（StudentCourse），但每个选课记录只对应一个学生
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne //一门课程可以有多条选课记录，但每条选课记录只对应一门课程
    @JoinColumn(name = "course_id") //指定在 student_courses 表中创建的外键列名
    private Course course;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registrationAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationAt;
    }

    public void setRegistrationTime(LocalDateTime registrationAt) {
        this.registrationAt = registrationAt;
    }
}