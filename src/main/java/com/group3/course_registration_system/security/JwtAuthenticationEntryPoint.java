package com.group3.course_registration_system.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
 * This class implements the AuthenticationEntryPoint interface, which is used to handle unauthorized access attempts.
 * The commence method of this class is called when the user tries to access the protected resource without providing valid authentication credentials.
 * Main functions:
 * Set the HTTP response status code to 401 (Unauthorized).
 * Set the response content type to JSON.
 * Writes a JSON response with an error message.
 * This implementation provides a clear error message that helps the client understand the reason for the authentication failure without revealing sensitive system information.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, 
                         HttpServletResponse response, 
                         AuthenticationException authException) 
                         throws IOException, ServletException {
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("Unauthorized: " + authException.getMessage());
    }
}