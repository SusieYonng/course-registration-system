package com.group3.course_registration_system.repository;

import com.group3.course_registration_system.entity.Course;
import com.group3.course_registration_system.enums.CourseStatus;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Fuzzy query by course name
     *
     * @param courseName part of or exact course name
     * @return List of matching courses
     */
    List<Course> findByCourseNameContainingIgnoreCase(String courseName);
    Optional<Course> findByCourseName(String title);
    List<Course> findByCourseNameContainingIgnoreCaseAndStatusNot(String keyword, CourseStatus status);
    List<Course> findByCourseNameContainingIgnoreCaseAndStatus(String keyword, CourseStatus status);
    List<Course> findByStatusNot(CourseStatus status);
    List<Course> findByStatus(CourseStatus status);
}
