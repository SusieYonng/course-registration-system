package com.group3.course_registration_system.config;

import com.group3.course_registration_system.security.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.group3.course_registration_system.security.JwtAuthenticationEntryPoint;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    // Constructor injection for JwtAuthenticationFilter
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("Configuring security chain...");
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Disable form login and HTTP basic authentication if needed for testing

                .exceptionHandling(
                        exceptionHandling -> exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(auth -> {
                    // Define the permitAll path
                    String[] permitPaths = { "/api/auth/**", "/", "/swagger-ui.html", // Swagger UI
                            "/swagger-ui/**", // Swagger static resources
                            "/v3/api-docs/**", // OpenAPI docs
                            "/webjars/**" // Swagger WebJar resources like JS, CSS
                    };

                    // Prints the path configured as permitAll
                    for (String path : permitPaths) {
                        System.out.println("Security filter chain configured: " + path + " is permitAll()");
                        auth.requestMatchers(path).permitAll();
                    }

                    // Administrator rights interface
                    auth.requestMatchers(HttpMethod.POST, "/api/courses").hasRole("ADMIN") // Add a course
                        .requestMatchers(HttpMethod.PUT, "/api/courses/{id}").hasRole("ADMIN") // Edit a course
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/{id}").hasRole("ADMIN") // Delete a course
                        .requestMatchers(HttpMethod.PUT, "/api/courses/{id}/publish").hasRole("ADMIN") // Publish a course
                        .requestMatchers(HttpMethod.PUT, "/api/courses/{id}/unpublish").hasRole("ADMIN") // Unpublish a course
                        .requestMatchers(HttpMethod.POST, "/api/courses/{id}/students").hasRole("ADMIN") // Add students to the course
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/{id}/students/{studentId}").hasRole("ADMIN") // Remove students from classes
                        // Student management
                        .requestMatchers(HttpMethod.GET, "/api/students").hasRole("ADMIN") // Obtain all students
                        .requestMatchers(HttpMethod.GET, "/api/students/{id}").hasRole("ADMIN") // Obtain one specific student's information
                        .requestMatchers(HttpMethod.GET, "/api/students/search").hasRole("ADMIN") // Search students
                        // Student rights interface
                        .requestMatchers(HttpMethod.GET, "/api/students/{id}/courses").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/students/{id}/courses/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/students/{id}/courses/**").hasRole("STUDENT")
                        // Public interface, accessible to all users
                        .requestMatchers(HttpMethod.GET, "/api/courses", "/api/courses/{id}").permitAll() // Get course listings and course details
                        .requestMatchers("/api/protected-resource").authenticated().anyRequest().authenticated();

                })
                // Add JwtAuthenticationFilter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
