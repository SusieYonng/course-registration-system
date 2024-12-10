package com.group3.course_registration_system.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.group3.course_registration_system.dto.StudentDTO;

import com.group3.course_registration_system.entity.Student;
import com.group3.course_registration_system.repository.StudentRepository;

public class StudentServiceTest {

    private final StudentRepository repository = mock(StudentRepository.class);
    private final StudentService service = new StudentService(repository);

    @Test
    void getAllStudents_shouldReturnAllStudents() {
        when(repository.findAll()).thenReturn(List.of(new Student()));
        List<StudentDTO> students = service.getAllStudents();
        assertEquals(1, students.size());
    }
}
