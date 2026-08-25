package com.pbl4.repository;

import com.pbl4.model.Document;
import com.pbl4.model.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Document entity
 * Provides database operations for documents
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    /**
     * Find documents by filename
     */
    List<Document> findByFileName(String fileName);

    /**
     * Find documents by status
     */
    List<Document> findByStatus(DocumentStatus status);

    /**
     * Find documents scanned after a specific date
     */
    List<Document> findByScanDateAfter(LocalDateTime scanDate);

    /**
     * Find documents by status and order by scan date (newest first)
     */
    List<Document> findByStatusOrderByScanDateDesc(DocumentStatus status);

    /**
     * Custom query - Find recent documents (last 10)
     */
    @Query(value = "SELECT * FROM documents ORDER BY scan_date DESC LIMIT 10", nativeQuery = true)
    List<Document> findRecent();

    /**
     * Custom query - Find documents by status with pagination
     */
    @Query("SELECT d FROM Document d WHERE d.status = :status ORDER BY d.scanDate DESC")
    List<Document> findByStatusOrdered(@Param("status") DocumentStatus status);

    /**
     * Check if document exists by filename
     */
    boolean existsByFileName(String fileName);
}

