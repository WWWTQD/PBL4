package com.pbl4.controller;

import com.pbl4.model.Document;
import com.pbl4.model.DocumentStatus;
import com.pbl4.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Document Controller
 * REST API endpoints for document management and processing
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/documents")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8000"})
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // ============================================
    // GET Endpoints
    // ============================================

    /**
     * GET /api/v1/documents
     * Retrieve all documents
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDocuments() {
        log.info("GET /api/v1/documents - Retrieving all documents");
        try {
            List<Document> documents = documentService.getAllDocuments();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Documents retrieved successfully");
            response.put("data", documents);
            response.put("count", documents.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving documents", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * GET /api/v1/documents/recent
     * Retrieve recent documents (last 10)
     */
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentDocuments() {
        log.info("GET /api/v1/documents/recent - Retrieving recent documents");
        try {
            List<Document> documents = documentService.getRecentDocuments();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Recent documents retrieved successfully");
            response.put("data", documents);
            response.put("count", documents.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving recent documents", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * GET /api/v1/documents/{id}
     * Retrieve document by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDocumentById(@PathVariable Long id) {
        log.info("GET /api/v1/documents/{} - Retrieving document by ID", id);
        try {
            return documentService.getDocumentById(id)
                    .map(document -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("message", "Document retrieved successfully");
                        response.put("data", document);
                        return ResponseEntity.ok(response);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("success", false, "error", "Document not found")));
        } catch (Exception e) {
            log.error("Error retrieving document by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * GET /api/v1/documents/status/{status}
     * Retrieve documents by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getDocumentsByStatus(@PathVariable String status) {
        log.info("GET /api/v1/documents/status/{} - Retrieving documents by status", status);
        try {
            DocumentStatus docStatus = DocumentStatus.valueOf(status.toUpperCase());
            List<Document> documents = documentService.getDocumentsByStatus(docStatus);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Documents retrieved successfully");
            response.put("data", documents);
            response.put("count", documents.size());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid status provided: {}", status);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "error", "Invalid status: " + status));
        } catch (Exception e) {
            log.error("Error retrieving documents by status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // ============================================
    // POST Endpoints
    // ============================================

    /**
     * POST /api/v1/documents/upload
     * Upload and process a new document
     *
     * Integration Point: Calls AI service to process the image
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "notes", required = false) String notes) {

        log.info("POST /api/v1/documents/upload - Uploading and processing document: {}", file.getOriginalFilename());

        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("success", false, "error", "File is empty"));
            }

            // Upload and process document
            Document document = documentService.uploadAndProcessDocument(file, notes);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Document uploaded and processed successfully");
            response.put("data", document);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IOException e) {
            log.error("IO error during document upload", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "File upload error: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Error processing document upload", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // ============================================
    // PUT Endpoints
    // ============================================

    /**
     * PUT /api/v1/documents/{id}
     * Update document
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDocument(
            @PathVariable Long id,
            @RequestBody Document document) {

        log.info("PUT /api/v1/documents/{} - Updating document", id);

        try {
            Document updatedDocument = documentService.updateDocument(id, document);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Document updated successfully");
            response.put("data", updatedDocument);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.warn("Document not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating document", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // ============================================
    // DELETE Endpoints
    // ============================================

    /**
     * DELETE /api/v1/documents/{id}
     * Delete document
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDocument(@PathVariable Long id) {
        log.info("DELETE /api/v1/documents/{} - Deleting document", id);

        try {
            documentService.deleteDocument(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Document deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error deleting document", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // ============================================
    // Health Check Endpoints
    // ============================================

    /**
     * GET /api/v1/documents/health/system
     * Check system health and AI service connectivity
     */
    @GetMapping("/health/system")
    public ResponseEntity<Map<String, Object>> systemHealth() {
        log.info("GET /api/v1/documents/health/system - Checking system health");

        boolean aiServiceHealthy = documentService.isAiServiceHealthy();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("backend", "healthy");
        response.put("aiService", aiServiceHealthy ? "healthy" : "unreachable");
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }
}

