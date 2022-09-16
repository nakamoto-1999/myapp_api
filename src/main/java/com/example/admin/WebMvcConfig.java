package com.example.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private String crosOrigin = System.getenv("SPRING_CROS_ORIGIN");

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(crosOrigin)
                .allowedMethods("GET" , "POST" , "PUT" , "DELETE")
                .allowCredentials(true);
    }
}
