package com.jovan.erp_v1.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.jovan.erp_v1.enumeration.ConfirmationDocumentStatus;
import com.jovan.erp_v1.model.ConfirmationDocument;
import com.jovan.erp_v1.request.ConfirmationDocumentRequest;
import com.jovan.erp_v1.response.ConfirmationDocumentResponse;
import com.jovan.erp_v1.save_as.ConfirmationDocumentSaveAsRequest;
import com.jovan.erp_v1.search_request.ConfirmationDocumentSearchRequest;

public interface IConfirmationDocumentService {

	ConfirmationDocument saveDocument(ConfirmationDocumentRequest request);
	Optional<ConfirmationDocument> getDocumentById(Long id);
	List<ConfirmationDocument> getAllDocuments();
	List<ConfirmationDocument> getDocumentsByUserId(Long userId);
	List<ConfirmationDocument> getDocumentsCreatedAfter(LocalDateTime date);
	void deleteDocument(Long id);
	ConfirmationDocument uploadDocument(MultipartFile file, Long userId, Long shiftId) throws IOException;
	ConfirmationDocument update(Long id, ConfirmationDocumentRequest request);
	//nove metode
	
	ConfirmationDocumentResponse trackConfirmationDoc(Long id);
	ConfirmationDocumentResponse confirmConfDoc(Long id);
	ConfirmationDocumentResponse cancelConfirmationDoc(Long id);
	ConfirmationDocumentResponse closeConfirmationDoc(Long id);
	ConfirmationDocumentResponse changeStatus(Long id, ConfirmationDocumentStatus status);
	ConfirmationDocumentResponse saveConfirmationDoc(ConfirmationDocumentRequest request);
	ConfirmationDocumentResponse saveAs(ConfirmationDocumentSaveAsRequest request);
	List<ConfirmationDocumentResponse> saveAll(List<ConfirmationDocumentRequest> requests);
	List<ConfirmationDocumentResponse> generalSearch(ConfirmationDocumentSearchRequest request);
}
