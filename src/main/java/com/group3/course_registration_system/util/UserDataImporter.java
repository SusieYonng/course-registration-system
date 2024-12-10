package com.group3.course_registration_system.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class UserDataImporter {
    private static final Logger logger = LoggerFactory.getLogger(UserDataImporter.class);
    private static final int BATCH_SIZE = 100;
    private static final String CSV_FILE_PATH = "user_data.csv";

    private final String jdbcUrl;
    private final String dbUsername;
    private final String dbPassword;

    // Statistics counters
    private int successCount = 0;
    private int skipCount = 0;
    private int errorCount = 0;

    public UserDataImporter(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.dbUsername = username;
        this.dbPassword = password;
    }

    public void importData() {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            try {
                processCSVFile(connection);
                connection.commit();
                logImportSummary();
            } catch (Exception e) {
                connection.rollback();
                logger.error("Import failed, rolling back transaction", e);
                throw e;
            }
        } catch (Exception e) {
            logger.error("Database connection error", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
    }

    private void processCSVFile(Connection connection) {
        String checkUserSql = "SELECT id FROM user WHERE username = ?";
        String insertUserSql = "INSERT INTO user (role, username, password) VALUES (?, ?, ?)";
        String insertStudentSql = "INSERT INTO student (name, user_id) VALUES (?, ?)";

        try (BufferedReader lineReader = new BufferedReader(new FileReader(CSV_FILE_PATH));
             PreparedStatement checkUserStmt = connection.prepareStatement(checkUserSql);
             PreparedStatement insertUserStmt = connection.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertStudentStmt = connection.prepareStatement(insertStudentSql)) {

            // Skip CSV header
            lineReader.readLine();
            String lineText;

            while ((lineText = lineReader.readLine()) != null) {
                try {
                    processLine(lineText, checkUserStmt, insertUserStmt, insertStudentStmt);
                } catch (Exception e) {
                    errorCount++;
                    logger.error("Error processing line: " + lineText, e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV file", e);
        }
    }

    private void processLine(String lineText, PreparedStatement checkUserStmt,
                           PreparedStatement insertUserStmt, PreparedStatement insertStudentStmt) 
                           throws SQLException {
        String[] data = lineText.split(",");
        String role = data[0];
        String username = data[1];
        String password = data[2];
        String name = data.length > 4 ? data[4] : null;

        // Check if user exists
        checkUserStmt.setString(1, username);
        ResultSet rs = checkUserStmt.executeQuery();

        if (!rs.next()) {
            // Insert user
            insertUserStmt.setString(1, role);
            insertUserStmt.setString(2, username);
            insertUserStmt.setString(3, password);
            insertUserStmt.executeUpdate();

            // If role is STUDENT, insert into student table
            if ("STUDENT".equals(role) && name != null) {
                ResultSet generatedKeys = insertUserStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long userId = generatedKeys.getLong(1);
                    insertStudentStmt.setString(1, name);
                    insertStudentStmt.setLong(2, userId);
                    insertStudentStmt.executeUpdate();
                }
            }
            successCount++;
            logger.info("Successfully imported user: {}", username);
        } else {
            skipCount++;
            logger.info("Skipping existing user: {}", username);
        }
    }

    private void logImportSummary() {
        logger.info("Import completed: {} succeeded, {} skipped, {} failed", 
            successCount, skipCount, errorCount);
    }

    public static void main(String[] args) {
        UserDataImporter importer = new UserDataImporter(
            DatabaseConfig.getJdbcUrl(),
            DatabaseConfig.getUsername(),
            DatabaseConfig.getPassword()
        );
        importer.importData();
    }
}