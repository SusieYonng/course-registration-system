package com.group3.course_registration_system.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group3.course_registration_system.dto.StudentDTO;
import com.group3.course_registration_system.service.StudentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/students")
@SecurityRequirement(name = "Bearer Authentication") 
@Tag(name = "Student Controller", description = "Manage student information and perform student-related operations.")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(
        summary = "Get all students",
        description = "Retrieve a list of all students currently registered in the system."
    )
    @GetMapping
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents(); // Return StudentDTOs
    }

    @Operation(
        summary = "Get a student by ID",
        description = "Retrieve the details of a specific student by providing their unique ID."
    )
    @GetMapping("/{id}")
    public StudentDTO getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id); // Return StudentDTO
    }

    @Operation(
        summary = "Search students by name",
        description = "Search for students by their name. The name parameter can be a partial match."
    )
    @GetMapping("/search")
    public List<StudentDTO> searchStudents(@RequestParam String name) {
        return studentService.searchStudents(name); // Return StudentDTOs
    }

    //@PostMapping
        //public Student createStudent(@RequestBody Student student) {
            //return studentService.saveStudent(student);
    //}

    //@DeleteMapping("/{id}")
    //public void deleteStudent(@PathVariable Long id) {
        //studentService.deleteStudent(id);
    //}
}
