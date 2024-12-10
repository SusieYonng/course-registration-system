package com.group3.course_registration_system.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.group3.course_registration_system.service.TokenBlacklistService;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Override
    protected boolean shouldNotFilter(@SuppressWarnings("null") HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // The Swagger and OpenAPI document paths are allowed
        return path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/api/auth/login")
                || path.startsWith("/api/auth/logout");

    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String requestPath = request.getRequestURI();
            logger.info("Processing request path:  " + requestPath);

            final String authHeader = request.getHeader("Authorization");

            String username = null;
            String jwt = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);
                username = jwtTokenUtil.getUsernameFromToken(jwt);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
                    // logger.info("Blacklisted JWT token attempted: " + jwt);
                    // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    // response.getWriter().write("Token is blacklisted");
                    // return;
                    throw new SecurityException("Blacklisted token");
                }
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                System.out.println("Loaded user details: " + userDetails);

                if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("JWT validated and user authenticated.");
                } else {
                    // System.out.println("JWT validation failed.");
                    // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    // response.getWriter().write("JWT validation failed");
                    // return;
                    throw new SecurityException("JWT validation failed");
                }

            }
            filterChain.doFilter(request, response);
        } catch (SecurityException e) {
            logger.error("Security exception: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: " + e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred during authentication");
        }

    }
}