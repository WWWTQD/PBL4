package com.pbl4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Application Configuration
 * Provides beans for RestTemplate and other global configurations
 */
@Configuration
public class AppConfig {

    /**
     * RestTemplate bean for making HTTP requests to external services
     * Used by AiIntegrationService to call the Python FastAPI backend
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

