package com.jovan.erp_v1.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.ConfirmationDocument;

@Repository
public interface ConfirmationDocumentRepository extends JpaRepository<ConfirmationDocument, Long> {

	 List<ConfirmationDocument> findByCreatedById(Long userId);

	 List<ConfirmationDocument> findByCreatedAtAfter(LocalDateTime date);
}
