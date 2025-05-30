package com.jovan.erp_v1.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.ConfirmationDocument;
import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.request.ConfirmationDocumentRequest;
import com.jovan.erp_v1.response.ConfirmationDocumentResponse;

@Component
public class ConfirmationDocumentMapper {

    public ConfirmationDocumentResponse toResponse(ConfirmationDocument doc) {
        ConfirmationDocumentResponse res = new ConfirmationDocumentResponse();
        res.setId(doc.getId());
        res.setFilePath(doc.getFilePath());
        res.setCreatedAt(doc.getCreatedAt());
        res.setUserId(doc.getCreatedBy().getId());
        res.setUsername(doc.getCreatedBy().getUsername());
        res.setShiftId(doc.getShift().getId());
        // Kreiranje "naziva" smene kao string, npr. "08:00 - 16:00"
        res.setShiftName(formatShiftName(doc.getShift().getStartTime(), doc.getShift().getEndTime()));
        return res;
    }

    public ConfirmationDocument toEntity(ConfirmationDocumentRequest req, User user, Shift shift) {
        ConfirmationDocument doc = new ConfirmationDocument();
        doc.setFilePath(req.filePath());
        doc.setCreatedAt(LocalDateTime.now());
        doc.setCreatedBy(user);
        doc.setShift(shift);
        return doc;
    }

    private String formatShiftName(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return start.format(formatter) + " - " + end.format(formatter);
    }
}
