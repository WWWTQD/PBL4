package com.pbl4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PBL4 Spring Boot Application
 * Main entry point for the PBL4 backend system
 *
 * Architecture:
 * - REST API on port 8080
 * - PostgreSQL database integration
 * - Integration with Python FastAPI AI service (port 8000)
 */
@Slf4j
@SpringBootApplication
public class PBL4Application {

    public static void main(String[] args) {
        SpringApplication.run(PBL4Application.class, args);
        log.info("PBL4 Backend Application started successfully");
    }
}

