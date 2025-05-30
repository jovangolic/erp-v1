package com.jovan.erp_v1.request;

import org.springframework.web.multipart.MultipartFile;

public record ConfirmationDocumentUploadRequest(
        MultipartFile file,
        Long userId,
        Long shiftId) {

}
