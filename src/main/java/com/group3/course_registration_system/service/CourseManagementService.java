package com.group3.course_registration_system.service;

import com.group3.course_registration_system.entity.Course;
import com.group3.course_registration_system.entity.Student;
import com.group3.course_registration_system.entity.StudentCourse;
import com.group3.course_registration_system.entity.User;
import com.group3.course_registration_system.enums.CourseStatus;
import com.group3.course_registration_system.repository.CourseRepository;
import com.group3.course_registration_system.repository.StudentCourseRepository;
import com.group3.course_registration_system.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.group3.course_registration_system.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CourseManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Course addCourse(Course course) {
        if (course.getStatus() == null) {
            course.setStatus(CourseStatus.UNPUBLISHED); // default as UNPUBLISHED
        }
        return courseRepository.save(course);
    }

    public Course updateCourse(Long courseId, Course updatedCourse) {
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        existingCourse.setCourseName(updatedCourse.getCourseName());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setSchedule(updatedCourse.getSchedule());
        existingCourse.setStatus(updatedCourse.getStatus());

        return courseRepository.save(existingCourse);
    }

    public void addStudentToCourse(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        if (studentCourseRepository.existsByStudentAndCourse(student, course)) {
            throw new IllegalStateException("Student is already registered for this course");
        }

        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourse(course);
        studentCourse.setStudent(student);
        studentCourse.setRegistrationTime(LocalDateTime.now());
        studentCourseRepository.save(studentCourse);
    }

    public void removeStudentFromCourse(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        StudentCourse studentCourse = studentCourseRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new IllegalArgumentException("Student is not registered for this course"));

        studentCourseRepository.delete(studentCourse);
    }

    public void deleteCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new RuntimeException("Course not found with id: " + courseId);
        }
        courseRepository.deleteById(courseId);
    }

    @Transactional
    public void publishCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        course.setStatus(CourseStatus.PUBLISHED);
        courseRepository.save(course);
    }

    @Transactional
    public void unpublishCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        course.setStatus(CourseStatus.UNPUBLISHED);
        courseRepository.save(course);
    }

    public List<Course> searchCourses(String keyword, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("Current user who searches courses now is " + user.getRole().toString());
        boolean isAdmin = user.getRole().toString().contains("ADMIN");

        if (keyword != null && !keyword.trim().isEmpty()) {
            if (isAdmin) {
                return courseRepository.findByCourseNameContainingIgnoreCaseAndStatusNot(keyword, CourseStatus.REMOVED);
            } else {
                return courseRepository.findByCourseNameContainingIgnoreCaseAndStatus(keyword, CourseStatus.PUBLISHED);
            }
        } else {
            if (isAdmin) {
                return courseRepository.findByStatusNot(CourseStatus.REMOVED);
            } else {
                return courseRepository.findByStatus(CourseStatus.PUBLISHED);
            }
        }
    }

    public Optional<Course> getCourseById(Long courseId) {
        return courseRepository.findById(courseId);
    }
}
