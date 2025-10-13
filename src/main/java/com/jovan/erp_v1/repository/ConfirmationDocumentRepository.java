package com.jovan.erp_v1.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.ConfirmationDocument;

@Repository
public interface ConfirmationDocumentRepository extends JpaRepository<ConfirmationDocument, Long>, JpaSpecificationExecutor<ConfirmationDocument> {

	 List<ConfirmationDocument> findByCreatedById(Long userId);

	 List<ConfirmationDocument> findByCreatedAtAfter(LocalDateTime date);
	 
	 //nove metode
	 @Query("SELECT cd FROM ConfirmationDocument cd WHERE cd.id = :id")
	 Optional<ConfirmationDocument> trackConfirmationDoc(@Param("id") Long id);
}
