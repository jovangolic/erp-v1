package com.jovan.erp_v1.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.jovan.erp_v1.model.ConfirmationDocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationDocumentResponse {

    private Long id;
    private String filePath;
    private LocalDateTime createdAt;
    private Long userId;
    private String username;
    private Long shiftId;
    private String shiftName;

    public ConfirmationDocumentResponse(ConfirmationDocument doc) {
        this.id = doc.getId();
        this.filePath = doc.getFilePath();
        this.createdAt = doc.getCreatedAt();
        this.userId = doc.getCreatedBy().getId();
        this.username = doc.getCreatedBy().getUsername();
        this.shiftId = doc.getShift().getId();
        // Kreiranje "naziva" smene kao string, npr. "08:00 - 16:00"
        this.shiftName = formatShiftName(doc.getShift().getStartTime(), doc.getShift().getEndTime());
    }

    private String formatShiftName(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return start.format(formatter) + " - " + end.format(formatter);
    }
}
