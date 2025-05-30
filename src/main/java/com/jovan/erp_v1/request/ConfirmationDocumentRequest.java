package com.jovan.erp_v1.request;

import java.time.LocalDateTime;

public record ConfirmationDocumentRequest(
        Long id,
        String filePath,
        LocalDateTime createdAt,
        Long userId,
        Long shiftId) {

}
