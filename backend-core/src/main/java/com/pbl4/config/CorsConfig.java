package com.pbl4.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration for Spring Boot Backend
 * Allows requests from Next.js frontend and AI service
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configure CORS to accept requests from frontend and AI service
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Allow frontend (Next.js) and other services
                .allowedOrigins(
                        "http://localhost:3000",      // Next.js Frontend
                        "http://localhost:8000",      // AI Service
                        "http://127.0.0.1:3000",
                        "http://127.0.0.1:8000"
                )
                // Allow all HTTP methods
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                // Allow all headers
                .allowedHeaders("*")
                // Allow credentials (cookies, authorization headers)
                .allowCredentials(true)
                // Cache CORS preflight response for 3600 seconds (1 hour)
                .maxAge(3600);
    }
}

