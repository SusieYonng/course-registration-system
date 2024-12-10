package com.group3.course_registration_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@Hidden
public class HomeController {

    /**
     * Handle requests to the root path
     *
     * @return Welcome message
     */
    @GetMapping("/")
    public String home() {
        return "Welcome to the Course Registration System!";
    }
}
