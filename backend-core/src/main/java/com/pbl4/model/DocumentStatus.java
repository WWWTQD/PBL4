package com.pbl4.model;

/**
 * Enumeration for document processing status
 */
public enum DocumentStatus {
    PENDING("Pending processing"),
    PROCESSING("Currently being processed"),
    COMPLETED("Processing completed successfully"),
    FAILED("Processing failed"),
    ARCHIVED("Document archived");

    private final String description;

    DocumentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

