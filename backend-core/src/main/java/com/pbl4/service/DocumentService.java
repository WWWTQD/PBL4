package com.pbl4.service;

import com.pbl4.model.Document;
import com.pbl4.model.DocumentStatus;
import com.pbl4.repository.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Document Service
 * Handles business logic for document operations including upload, processing, and retrieval
 */
@Slf4j
@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final AiIntegrationService aiIntegrationService;

    public DocumentService(DocumentRepository documentRepository, AiIntegrationService aiIntegrationService) {
        this.documentRepository = documentRepository;
        this.aiIntegrationService = aiIntegrationService;
    }

    /**
     * Get all documents
     * @return List of all documents
     */
    public List<Document> getAllDocuments() {
        log.info("Retrieving all documents");
        return documentRepository.findAll();
    }

    /**
     * Get recent documents (last 10)
     * @return List of recent documents
     */
    public List<Document> getRecentDocuments() {
        log.info("Retrieving recent documents");
        return documentRepository.findRecent();
    }

    /**
     * Get document by ID
     * @param id Document ID
     * @return Optional containing Document if found
     */
    public Optional<Document> getDocumentById(Long id) {
        log.info("Retrieving document with ID: {}", id);
        return documentRepository.findById(id);
    }

    /**
     * Get documents by status
     * @param status DocumentStatus filter
     * @return List of documents with specified status
     */
    public List<Document> getDocumentsByStatus(DocumentStatus status) {
        log.info("Retrieving documents with status: {}", status);
        return documentRepository.findByStatusOrdered(status);
    }

    /**
     * Upload and process a new document
     * This is the main integration point with the AI service
     *
     * @param file Uploaded image file
     * @param notes Optional notes about the document
     * @return Created Document entity
     * @throws IOException if file processing fails
     */
    public Document uploadAndProcessDocument(MultipartFile file, String notes) throws IOException {
        try {
            log.info("Starting document upload and processing: {}", file.getOriginalFilename());

            // Create new document entity
            Document document = Document.builder()
                    .fileName(file.getOriginalFilename())
                    .scanDate(LocalDateTime.now())
                    .status(DocumentStatus.PROCESSING)
                    .notes(notes)
                    .build();

            // Save initial document (PROCESSING status)
            Document savedDocument = documentRepository.save(document);
            log.info("Document saved with ID: {} and status: PROCESSING", savedDocument.getId());

            // Call AI service to process image
            Map<String, Object> aiResponse = aiIntegrationService.processImage(file);

            // Extract results from AI response
            if (aiResponse != null && aiResponse.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) aiResponse.get("data");

                // Update document with AI processing results
                savedDocument.setProcessedImage((String) data.get("processedImage"));
                savedDocument.setExtractedText((String) data.get("extractedText"));
                savedDocument.setStatus(DocumentStatus.COMPLETED);

                // Save processed document
                Document processedDocument = documentRepository.save(savedDocument);
                log.info("Document processing completed successfully. ID: {}", processedDocument.getId());
                return processedDocument;
            } else {
                throw new RuntimeException("Invalid response from AI service");
            }

        } catch (Exception e) {
            log.error("Error during document upload and processing: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing document: " + e.getMessage(), e);
        }
    }

    /**
     * Update document
     * @param id Document ID
     * @param document Document data to update
     * @return Updated Document
     */
    public Document updateDocument(Long id, Document document) {
        log.info("Updating document with ID: {}", id);
        return documentRepository.findById(id)
                .map(existingDoc -> {
                    if (document.getNotes() != null) {
                        existingDoc.setNotes(document.getNotes());
                    }
                    if (document.getStatus() != null) {
                        existingDoc.setStatus(document.getStatus());
                    }
                    return documentRepository.save(existingDoc);
                })
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + id));
    }

    /**
     * Delete document
     * @param id Document ID
     */
    public void deleteDocument(Long id) {
        log.info("Deleting document with ID: {}", id);
        documentRepository.deleteById(id);
    }

    /**
     * Check if AI service is healthy
     * @return true if AI service is up
     */
    public boolean isAiServiceHealthy() {
        return aiIntegrationService.isAiServiceHealthy();
    }
}

