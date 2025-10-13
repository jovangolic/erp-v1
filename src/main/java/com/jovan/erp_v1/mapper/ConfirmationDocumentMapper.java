package com.jovan.erp_v1.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.ConfirmationDocument;
import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.request.ConfirmationDocumentRequest;
import com.jovan.erp_v1.response.ConfirmationDocumentResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class ConfirmationDocumentMapper extends AbstractMapper<ConfirmationDocumentRequest> {

    public ConfirmationDocumentResponse toResponse(ConfirmationDocument doc) {
    	Objects.requireNonNull(doc, "ConfirmationDocument must not be null");
    	return new ConfirmationDocumentResponse(doc);
    }
    
    public List<ConfirmationDocumentResponse> toResponseList(List<ConfirmationDocument> req){
    	if(req == null || req.isEmpty()) {
    		return Collections.emptyList();
    	}
    	return req.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ConfirmationDocument toEntity(ConfirmationDocumentRequest req, User user, Shift shift) {
    	Objects.requireNonNull(req, "ConfirmationDocumentRequest must not be null");
    	Objects.requireNonNull(user, "User must not be null");
    	Objects.requireNonNull(shift, "Shift must not be null");
    	validateIdForCreate(req, ConfirmationDocumentRequest::id);
        ConfirmationDocument doc = new ConfirmationDocument();
        doc.setId(req.id());
        doc.setFilePath(req.filePath());
        doc.setCreatedBy(user);
        doc.setShift(shift);
        return doc;
    }
    
    public ConfirmationDocument toEntityUpdate(ConfirmationDocumentRequest req, ConfirmationDocument doc, User user, Shift shift) {
    	Objects.requireNonNull(doc, "ConfirmationDocument must not be null");
    	Objects.requireNonNull(req, "ConfirmationDocumentRequest must not be null");
    	Objects.requireNonNull(user, "User must not be null");
    	Objects.requireNonNull(shift, "Shift must not be null");
    	validateIdForUpdate(req, ConfirmationDocumentRequest::id);
    	doc.setFilePath(req.filePath());
        doc.setCreatedBy(user);
        doc.setShift(shift);
    	return doc;
    }

    private String formatShiftName(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return start.format(formatter) + " - " + end.format(formatter);
    }
}
