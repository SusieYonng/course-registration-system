package com.group3.course_registration_system.util;

import java.sql.Connection;
import java.sql.DriverManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class DataGenerationController {
    private static final Logger logger = LoggerFactory.getLogger(DataGenerationController.class);

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.getJdbcUrl(),
                DatabaseConfig.getUsername(),
                DatabaseConfig.getPassword())) {

            if (DataCheckUtil.needsDataGeneration(connection)) {
                // Generate and import user data
                logger.info("Starting user data generation...");
                UserDataGenerator.main(null);
                logger.info("User data generation completed.");

                logger.info("Starting user data import...");
                UserDataImporter importer = new UserDataImporter(
                    DatabaseConfig.getJdbcUrl(),
                    DatabaseConfig.getUsername(),
                    DatabaseConfig.getPassword()
                );
                importer.importData();
                logger.info("User data import completed.");
            } else {
                logger.info("Sufficient user data exists, skipping user data generation.");
            }

            // Always generate and import course data
            logger.info("Starting course data generation...");
            CourseDataGenerator.main(null);
            logger.info("Course data generation completed.");

            logger.info("Starting course data import...");
            CourseDataImporter courseImporter = new CourseDataImporter(
                DatabaseConfig.getJdbcUrl(),
                DatabaseConfig.getUsername(),
                DatabaseConfig.getPassword()
            );
            courseImporter.importData();
            logger.info("Course data import completed.");

        } catch (Exception e) {
            logger.error("Error during data generation and import process", e);
            System.exit(1);
        }
    }
}