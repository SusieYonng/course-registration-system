package com.group3.course_registration_system.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UserDataGenerator {
    // Constants for generating data
    private static final String ALPHA_NUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Random random = new Random();
    private static final Set<String> usedUsernames = new HashSet<>();
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        try (FileWriter writer = new FileWriter("user_data.csv")) {
            // Write CSV header
            writer.write("role,username,password,raw_password,name\n");
            
            // Generate admin data (3 records)
            for (int i = 1; i <= 3; i++) {
                generateUserData("ADMIN", writer);
            }
            
            // Generate student data (50 records)
            for (int i = 1; i <= 50; i++) {
                generateUserData("STUDENT", writer);
            }
            
            System.out.println("User data has been generated and saved to user_data.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Generate user data for each record
    private static void generateUserData(String role, FileWriter writer) throws IOException {
        String username = generateUniqueUsername(role);
        String rawPassword = generateRandomPassword();
        String encodedPassword = encoder.encode(rawPassword);
        String name = generateFullName();
        
        writer.write(String.format("%s,%s,%s,%s,%s\n", 
            role, username, encodedPassword, rawPassword, name));
    }

    // Generate unique username based on role
    private static String generateUniqueUsername(String role) {
        String prefix = role.equals("ADMIN") ? "M" : "S";
        String username;
        do {
            int year = 2024;
            int sequence = random.nextInt(10000);
            username = String.format("%s%d%04d", prefix, year, sequence);
        } while (usedUsernames.contains(username));
        
        usedUsernames.add(username);
        return username;
    }

    // Generate random 8-character password
    private static String generateRandomPassword() {
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(ALPHA_NUMERIC.length());
            sb.append(ALPHA_NUMERIC.charAt(index));
        }
        return sb.toString();
    }

    // Static inner class for name constants
    private static class UserNameConstants {
        // First names for generating realistic user names
        private static final String[] FIRST_NAMES = {
            "James", "John", "Robert", "Michael", "William", "David", "Richard", "Joseph", "Thomas", "Charles",
            "Christopher", "Daniel", "Matthew", "Anthony", "Donald", "Mark", "Paul", "Steven", "Andrew", "Kenneth",
            "Joshua", "Kevin", "Brian", "George", "Edward", "Ronald", "Timothy", "Jason", "Jeffrey", "Ryan",
            "Jacob", "Gary", "Nicholas", "Eric", "Jonathan", "Stephen", "Larry", "Justin", "Scott", "Brandon",
            "Benjamin", "Samuel", "Gregory", "Frank", "Alexander", "Raymond", "Patrick", "Jack", "Dennis", "Jerry",
            "Tyler", "Aaron", "Henry"
        };

        // Last names for generating realistic user names
        private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
            "Anderson", "Taylor", "Thomas", "Moore", "Jackson", "Martin", "Lee", "Thompson", "White", "Harris",
            "Clark", "Lewis", "Robinson", "Walker", "Hall", "Allen", "Young", "King", "Wright", "Lopez",
            "Hill", "Scott", "Green", "Adams", "Baker", "Nelson", "Carter", "Mitchell", "Roberts", "Turner",
            "Phillips", "Campbell", "Parker", "Evans", "Edwards", "Collins", "Stewart", "Morris", "Murphy", "Cook",
            "Rogers", "Morgan", "Peterson"
        };
    }

    // Generate full name using inner class constants
    private static String generateFullName() {
        String firstName = UserNameConstants.FIRST_NAMES[random.nextInt(UserNameConstants.FIRST_NAMES.length)];
        String lastName = UserNameConstants.LAST_NAMES[random.nextInt(UserNameConstants.LAST_NAMES.length)];
        return firstName + " " + lastName;
    }
    
}
