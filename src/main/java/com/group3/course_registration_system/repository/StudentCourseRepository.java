package com.group3.course_registration_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group3.course_registration_system.entity.Course;
import com.group3.course_registration_system.entity.Student;
import com.group3.course_registration_system.entity.StudentCourse;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    /**
     * 根据学生查找所有选课记录
     */
    List<StudentCourse> findAllByStudent(Student student);

    /**
     * 根据学生和课程查找特定选课记录
     */
    Optional<StudentCourse> findByStudentAndCourse(Student student, Course course);

    /**
     * 判断学生是否已注册某课程
     */
    boolean existsByStudentAndCourse(Student student, Course course);
}
