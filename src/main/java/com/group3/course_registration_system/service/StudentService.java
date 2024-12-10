package com.group3.course_registration_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.group3.course_registration_system.dto.StudentDTO;
import com.group3.course_registration_system.dto.CourseDTO;
import com.group3.course_registration_system.entity.Student;
import com.group3.course_registration_system.entity.StudentCourse;

import org.springframework.stereotype.Service;

import com.group3.course_registration_system.entity.Student;
import com.group3.course_registration_system.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Get all students and map to StudentDTOs
     */
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToStudentDTO) // Convert each Student entity to a StudentDTO
                .collect(Collectors.toList());
    }

    /**
     * Get a single student by ID and map to StudentDTO
     */
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        return mapToStudentDTO(student);
    }

    /**
     * Search students by name and map to StudentDTOs
     */
    public List<StudentDTO> searchStudents(String name) {
        return studentRepository.findByNameContaining(name).stream()
                .map(this::mapToStudentDTO)
                .collect(Collectors.toList());
    }

    /**
     * Save a student (this method still returns the original Student entity for internal use)
     */

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    /**
     * Delete a student by ID
     */
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    /**
     * Helper method to map Student entity to StudentDTO
     */
    private StudentDTO mapToStudentDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setStudentId(student.getStudentId());
        dto.setName(student.getName());
    
        // Map enrolled courses
        if (student.getEnrolledCourses() != null) {
            dto.setRegisteredCourses(
                student.getEnrolledCourses().stream()
                    .map(StudentCourse::getCourse)
                    .map(course -> {
                        CourseDTO courseDTO = new CourseDTO();
                        courseDTO.setCourseId(course.getCourseId());
                        courseDTO.setCourseName(course.getCourseName());
                        courseDTO.setSchedule(course.getSchedule());
                        return courseDTO;
                    })
                    .collect(Collectors.toList())
            );
        }
        return dto;
    }    
}
