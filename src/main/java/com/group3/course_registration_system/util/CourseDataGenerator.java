package com.group3.course_registration_system.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CourseDataGenerator {
    private static final List<String[]> COURSES = Arrays.asList(
        new String[]{"Introduction to Computer Science", "Fundamental concepts of programming and computer systems", "Mo 09:00 AM - 11:00 AM"},
        new String[]{"Data Structures and Algorithms", "Advanced programming concepts and algorithm analysis", "Tu 02:00 PM - 04:00 PM"},
        new String[]{"Database Management Systems", "Principles of database design and SQL", "We 01:30 PM - 04:30 PM"},
        new String[]{"Web Development", "HTML, CSS, and JavaScript fundamentals", "Th 10:00 AM - 01:00 PM"},
        new String[]{"Operating Systems", "Computer system organization and operating system concepts", "Fr 02:30 PM - 05:30 PM"},
        new String[]{"Software Engineering", "Software development lifecycle and project management", "Mo 02:00 PM - 05:00 PM"},
        new String[]{"Artificial Intelligence", "Machine learning and AI algorithms", "Sa 09:00 AM - 12:00 PM"},
        new String[]{"Network Security", "Network protocols and security principles", "Su 01:00 PM - 04:00 PM"},
        new String[]{"Mobile App Development", "iOS and Android application development", "Tu 09:30 AM - 12:30 PM"},
        new String[]{"Cloud Computing", "Cloud services and distributed systems", "We 03:00 PM - 06:00 PM"}
    );

    public static void main(String[] args) {
        try (FileWriter writer = new FileWriter("course_data.csv")) {
            // Write CSV header
            writer.write("title,description,schedule,status\n");
            
            // Write course data
            for (String[] course : COURSES) {
                writer.write(String.format("%s,%s,%s,%s\n",
                    course[0],
                    course[1],
                    course[2],
                    "UNPUBLISHED"
                ));
            }
            
            System.out.println("Course data has been generated and saved to course_data.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}