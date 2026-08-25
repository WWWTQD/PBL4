package com.pbl4.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Document Entity - Represents a scanned document in the system
 * Stores metadata about documents: filename, scan date, processing status, extracted text, etc.
 */
@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Document {

    /**
     * Unique identifier for the document
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Original scan filename
     */
    @Column(name = "file_name", nullable = false)
    private String fileName;

    /**
     * Date when the document was scanned
     */
    @Column(name = "scan_date", nullable = false)
    private LocalDateTime scanDate;

    /**
     * Current processing status (PENDING, PROCESSING, COMPLETED, FAILED)
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    /**
     * Text extracted from the document via OCR
     */
    @Column(name = "extracted_text", columnDefinition = "TEXT")
    private String extractedText;

    /**
     * File path or URL where the processed document is stored
     */
    @Column(name = "file_url")
    private String fileUrl;

    /**
     * Processed image (base64 encoded)
     */
    @Column(name = "processed_image", columnDefinition = "LONGTEXT")
    private String processedImage;

    /**
     * Timestamp when the record was created
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the record was last updated
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * User notes or comments about the document
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * PrePersist hook - set creation timestamp before first insert
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = DocumentStatus.PENDING;
        }
        if (scanDate == null) {
            scanDate = LocalDateTime.now();
        }
    }

    /**
     * PreUpdate hook - update the updated_at timestamp before updates
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

