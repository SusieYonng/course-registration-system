package com.group3.course_registration_system.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/* 
 * This function is for generating encoded password by using BCryptPasswordEncoder,
 * Note: Make sure that BCryptPasswordEncoder is the only encryption method. 
 * If the encryption method used in the database to store passwords is not consistent with BCryptPasswordEncoder, authentication will fail.
 */
public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "student666";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
    }
}

// ('student1', 'student123', 'STUDENT'),
// UPDATE user SET password = '$2a$10$...（encoded password）' WHERE username = 'admin';

// INSERT INTO user (username, password, role) 
// VALUES
//     ('student2', '$2a$10$0q40FMLJ5djXrN/WPWk1huzQzJ7Cy.nS0QZGoGgu65xSuYaADMbMi', 'STUDENT'),
//     ('student3', '$2a$10$Nz9YFkDdGTy9ff3phJ0lben1FbNybZ76T6eaxFOswnRdgynhQ79/S', 'STUDENT'); //student666 -- student555

// INSERT INTO student (user_id, name)
// VALUES
//     (5, 'Alice Johnson'), 
//     (6, 'Bob Smith'),
//     (7, 'Sam Wong'); 