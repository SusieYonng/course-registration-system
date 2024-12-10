package com.group3.course_registration_system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.group3.course_registration_system.dto.CourseDTO;
import com.group3.course_registration_system.entity.Course;
import com.group3.course_registration_system.entity.Student;
import com.group3.course_registration_system.entity.StudentCourse;
import com.group3.course_registration_system.repository.CourseRepository;
import com.group3.course_registration_system.repository.StudentCourseRepository;
import com.group3.course_registration_system.repository.StudentRepository;

@Service
public class StudentCourseService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentCourseRepository studentCourseRepository;

    public StudentCourseService(StudentRepository studentRepository, CourseRepository courseRepository, StudentCourseRepository studentCourseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.studentCourseRepository = studentCourseRepository;
    }

    /**
     * Retrieve the list of courses selected by the student identified by the studentId
     */
    public List<CourseDTO> getCoursesByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));
        // Fetch courses via StudentCourseRepository, map to CourseDTO
        return studentCourseRepository.findAllByStudent(student)
                .stream()
                .map(studentCourse -> {
                        Course course = studentCourse.getCourse();
                        CourseDTO dto = new CourseDTO();
                        dto.setCourseId(course.getCourseId());
                        dto.setCourseName(course.getCourseName());
                        dto.setDescription(course.getDescription());
                        dto.setSchedule(course.getSchedule());
                        dto.setStatus(course.getStatus().toString());
                        return dto;
                })
                .toList();
    }

    /**
     * Register the student identified by the studentId for the course identified by the courseId.
     */
    public void registerCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));
        
        // Check if the student has already registered the course
        if (studentCourseRepository.existsByStudentAndCourse(student, course)) {
            throw new IllegalStateException("Student is already registered for this course");
        }

        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudent(student);
        studentCourse.setCourse(course);
        studentCourse.setRegistrationTime(java.time.LocalDateTime.now());
        studentCourseRepository.save(studentCourse);
    }

    /**
     * Drop the course identified by the courseId for the student identified by the studentId
     */
    public void dropCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));

        StudentCourse studentCourse = studentCourseRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new IllegalArgumentException("Student is not registered for this course"));
        studentCourseRepository.delete(studentCourse);
    }
}
