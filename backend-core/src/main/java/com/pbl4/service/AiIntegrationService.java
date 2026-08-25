package com.pbl4.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * AI Integration Service
 * Handles communication with the Python FastAPI AI service for image processing
 *
 * Integration Point: This service calls the Python FastAPI backend at
 * http://localhost:8000/api/v1/process-image to process document images
 */
@Slf4j
@Service
public class AiIntegrationService {

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Temporary directory for storing uploaded files
     */
    private static final String TEMP_DIR = "temp_uploads";

    public AiIntegrationService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        // Create temp directory if it doesn't exist
        File tempDir = new File(TEMP_DIR);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
    }

    /**
     * Process an image file by sending it to the AI service
     *
     * @param file MultipartFile containing the image
     * @return Map containing processedImage (base64), extractedText, and metadata
     * @throws IOException if file processing fails
     */
    public Map<String, Object> processImage(MultipartFile file) throws IOException {
        try {
            log.info("Starting image processing for file: {}", file.getOriginalFilename());

            // Validate file
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Uploaded file is empty");
            }

            if (!isValidImageFile(file)) {
                throw new IllegalArgumentException("Uploaded file is not a valid image format");
            }

            // Save file temporarily
            String tempFilePath = saveTempFile(file);

            try {
                // Call AI service endpoint
                Map<String, Object> aiResponse = callAiService(tempFilePath);
                log.info("Image processing completed successfully for: {}", file.getOriginalFilename());
                return aiResponse;
            } finally {
                // Clean up temporary file
                deleteTempFile(tempFilePath);
            }

        } catch (Exception e) {
            log.error("Error processing image: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("Error processing image: " + e.getMessage(), e);
        }
    }

    /**
     * Call the AI service to process an image file
     *
     * Integration Point: Communicates with Python FastAPI backend
     * Endpoint: POST http://localhost:8000/api/v1/process-image
     *
     * @param filePath Path to the image file
     * @return Response from AI service containing processed image and extracted text
     */
    private Map<String, Object> callAiService(String filePath) {
        try {
            log.debug("Calling AI service at: {}/api/v1/process-image", aiServiceUrl);

            // Create multipart request body
            File file = new File(filePath);
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(file));

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Create HTTP entity
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

            // Make POST request to AI service
            String url = aiServiceUrl + "/api/v1/process-image";
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("AI service processing completed successfully");
                return response.getBody();
            } else {
                throw new RuntimeException("AI service returned non-success status: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Error calling AI service: {}", e.getMessage(), e);
            throw new RuntimeException("AI service error: " + e.getMessage(), e);
        }
    }

    /**
     * Save uploaded file temporarily
     *
     * @param file MultipartFile to save
     * @return Path to the saved file
     * @throws IOException if file save fails
     */
    private String saveTempFile(MultipartFile file) throws IOException {
        try {
            Path tempPath = Paths.get(TEMP_DIR, System.currentTimeMillis() + "_" + file.getOriginalFilename());
            Files.write(tempPath, file.getBytes());
            log.debug("File saved temporarily at: {}", tempPath.toAbsolutePath());
            return tempPath.toString();
        } catch (IOException e) {
            log.error("Failed to save temporary file", e);
            throw e;
        }
    }

    /**
     * Delete temporary file
     *
     * @param filePath Path to file to delete
     */
    private void deleteTempFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
            log.debug("Temporary file deleted: {}", filePath);
        } catch (IOException e) {
            log.warn("Failed to delete temporary file: {}", filePath, e);
        }
    }

    /**
     * Validate that uploaded file is an image
     *
     * @param file MultipartFile to validate
     * @return true if file is a valid image format
     */
    private boolean isValidImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
                contentType.startsWith("image/")
        );
    }

    /**
     * Check AI service health
     *
     * @return true if AI service is up and running
     */
    public boolean isAiServiceHealthy() {
        try {
            String healthUrl = aiServiceUrl + "/health";
            ResponseEntity<Map> response = restTemplate.getForEntity(healthUrl, Map.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.warn("AI service health check failed: {}", e.getMessage());
            return false;
        }
    }
}

