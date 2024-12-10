package com.group3.course_registration_system.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * @SecurityScheme annotation defines an authentication scheme:
* name: defines the name of the security scheme
* type: specifies the use of HTTP authentication
* bearerFormat: specifies the use of JWT format
* scheme: specifies the use of bearer authentication scheme
*/
@Configuration
@OpenAPIDefinition(info = @Info(title = "Course Registration API", version = "1.0"))
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class SwaggerConfig {
}