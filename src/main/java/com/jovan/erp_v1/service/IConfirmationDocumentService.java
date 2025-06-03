package com.jovan.erp_v1.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.jovan.erp_v1.model.ConfirmationDocument;
import com.jovan.erp_v1.request.ConfirmationDocumentRequest;

public interface IConfirmationDocumentService {

	// ConfirmationDocument saveDocument(ConfirmationDocument document);
	ConfirmationDocument saveDocument(ConfirmationDocumentRequest request);

	Optional<ConfirmationDocument> getDocumentById(Long id);

	List<ConfirmationDocument> getAllDocuments();

	List<ConfirmationDocument> getDocumentsByUserId(Long userId);

	List<ConfirmationDocument> getDocumentsCreatedAfter(LocalDateTime date);

	void deleteDocument(Long id);

	ConfirmationDocument uploadDocument(MultipartFile file, Long userId, Long shiftId) throws IOException;

	ConfirmationDocument update(Long id, ConfirmationDocumentRequest request);
}
