package com.group3.course_registration_system.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.group3.course_registration_system.security.JwtTokenUtil;

// @ExtendWith(SpringExtension.class) // Spring 测试扩展
// @ContextConfiguration(classes = JwtTokenUtil.class) // 指定测试上下文
// @TestPropertySource(properties = {
//     "jwt.secret=6YswvU1rQlZrRbrBWcmL5N92kRt9Gx7Oq4Hw4I3j9Vk=",
//     "jwt.expiration=3600"
// })
class JwtTokenUtilTest {

    private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @BeforeEach
    void setUp() {
        jwtTokenUtil.setSecret("6YswvU1rQlZrRbrBWcmL5N92kRt9Gx7Oq4Hw4I3j9Vk=");
        jwtTokenUtil.setExpiration(3600L); // Set the expiration time, in seconds
    }

    @Test
    void testGenerateTokenWithRole() {
        // Arrange: Mock UserDetails and role
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("admin");

        String role = "ADMIN";

        // Act: Generate token
        String token = jwtTokenUtil.generateTokenWithRole(userDetails, role);

        // Assert: Verify token contains expected role
        String extractedRole = jwtTokenUtil.getClaimFromToken(token, claims -> claims.get("role", String.class));
        assertEquals("ADMIN", extractedRole);
    }

    @Test
    void testGetUsernameFromToken() {
        // Arrange: Mock UserDetails
        UserDetails userDetails = new User("student", "password", Collections.emptyList());

        String token = jwtTokenUtil.generateTokenWithRole(userDetails, "STUDENT");

        // Act: Extract username
        String extractedUsername = jwtTokenUtil.getUsernameFromToken(token);

        // Assert: Verify extracted username matches the input
        assertEquals("student", extractedUsername);
    }

    @Test
    void testGetUsernameFromTokenThrowsExceptionForEmptyToken() {
        // Arrange: Empty token
        String token = "";

        // Act & Assert: Verify exception is thrown for empty token
        assertThrows(IllegalArgumentException.class, () -> jwtTokenUtil.getUsernameFromToken(token));
    }
}
