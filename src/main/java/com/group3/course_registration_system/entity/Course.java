package com.group3.course_registration_system.entity;
import java.util.Set;

import com.group3.course_registration_system.enums.CourseStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    // 如果不指定 @Column(name = "xxx")，JPA 会自动将驼峰命名转换为下划线命名
    @Column(name = "title", nullable = false)
    private String courseName;

    @Column
    private String description;

    @Column(nullable = false)
    private String schedule;

    @Enumerated(EnumType.STRING)
    private CourseStatus status;
   
    // @Column(nullable = false)
    // private Integer capacity;

    // @Column(nullable = false)
    // private Integer availableSeats;

    @OneToMany(mappedBy = "course")
    private Set<StudentCourse> enrolledStudents; 
    // 选择 Set 而不是 List 是因为学生选课记录应该是唯一的，不允许重复（Set 自动确保了一个学生不能多次选择同一门课程）
    // StudentCourse 是一个关联实体，包含了选课的详细信息（如选课时间）


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

    // public Integer getCapacity() {
    //     return capacity;
    // }

    // public void setCapacity(Integer capacity) {
    //     this.capacity = capacity;
    // }

    // public Integer getAvailableSeats() {
    //     return availableSeats;
    // }

    // public void setAvailableSeats(Integer availableSeats) {
    //     this.availableSeats = availableSeats;
    // }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    public Set<StudentCourse> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(Set<StudentCourse> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }
}

