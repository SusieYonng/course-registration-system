package com.group3.course_registration_system.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCheckUtil {
    private static final Logger logger = LoggerFactory.getLogger(DataCheckUtil.class);
    private static final int MIN_USER_COUNT = 20;

    public static boolean needsDataGeneration(Connection connection) {
        String countSql = "SELECT COUNT(*) FROM user";
        try (PreparedStatement stmt = connection.prepareStatement(countSql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                logger.info("Current user count in database: {}", count);
                return count < MIN_USER_COUNT;
            }
        } catch (SQLException e) {
            logger.error("Error checking user count", e);
        }
        return true;
    }
}