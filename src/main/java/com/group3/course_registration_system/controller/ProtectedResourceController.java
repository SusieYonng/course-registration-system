package com.group3.course_registration_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@Hidden
@RequestMapping("/api")
public class ProtectedResourceController {

    @GetMapping("/protected-resource")
    public String getProtectedResource() {
        return "This is a protected resource. If you can see this, you are authenticated.";
    }
}