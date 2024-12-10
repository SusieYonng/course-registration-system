package com.group3.course_registration_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.course_registration_system.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByNameContaining(String name);
    boolean existsByName(String name);
    Optional<Student> findByName(String name);
    Optional<Student> findByUser_Username(String username);
}

