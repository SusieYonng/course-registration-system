package com.group3.course_registration_system.util;

import com.group3.course_registration_system.enums.CourseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class CourseDataImporter {
    private static final Logger logger = LoggerFactory.getLogger(CourseDataImporter.class);
    private static final String CSV_FILE_PATH = "course_data.csv";

    private final String jdbcUrl;
    private final String username;
    private final String password;

    // Statistics counters
    private int successCount = 0;
    private int skipCount = 0;
    private int errorCount = 0;

    public CourseDataImporter(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public void importData() throws Exception {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            String checkSql = "SELECT COUNT(*) FROM course WHERE title = ?";
            String insertSql = "INSERT INTO course (title, description, schedule, status) VALUES (?, ?, ?, ?)";

            try (BufferedReader lineReader = new BufferedReader(new FileReader(CSV_FILE_PATH));
                    PreparedStatement checkStmt = connection.prepareStatement(checkSql);
                    PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {

                // Skip header
                lineReader.readLine();
                String lineText;

                while ((lineText = lineReader.readLine()) != null) {
                    try {
                        processLine(lineText, checkStmt, insertStmt);
                    } catch (Exception e) {
                        errorCount++;
                        logger.error("Error processing line: {}", lineText, e);
                    }
                }

                connection.commit();
                logImportSummary();

            } catch (Exception e) {
                connection.rollback();
                logger.error("Import failed, rolling back transaction", e);
                throw e;
            }
        } catch (SQLException e) {
            logger.error("Database connection error", e);
        }
    }

    private void processLine(String lineText, PreparedStatement checkStmt, PreparedStatement insertStmt)
            throws SQLException {
        String[] data = lineText.split(",");
        String courseName = data[0];
        String description = data[1];
        String schedule = data[2];
        String status = data[3];

        // Check if course exists
        checkStmt.setString(1, courseName);
        ResultSet rs = checkStmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        if (count == 0) {
            insertStmt.setString(1, courseName);
            insertStmt.setString(2, description);
            insertStmt.setString(3, schedule);
            insertStmt.setString(4, CourseStatus.valueOf(status).name());
            insertStmt.executeUpdate();
            successCount++;
            logger.info("Inserted course: {}", courseName);
        } else {
            skipCount++;
            logger.info("Skipped existing course: {}", courseName);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    private void logImportSummary() {
        logger.info("Import completed: {} succeeded, {} skipped, {} failed", successCount, skipCount, errorCount);
    }

    // In CourseDataImporter
    public static void main(String[] args) throws Exception {
        CourseDataImporter importer = new CourseDataImporter(
            DatabaseConfig.getJdbcUrl(), 
            DatabaseConfig.getUsername(),
            DatabaseConfig.getPassword());
        importer.importData();
    }
}