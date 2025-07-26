package com.jovan.erp_v1.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.FileOpt;
import com.jovan.erp_v1.request.FileOptRequest;
import com.jovan.erp_v1.response.FileOptResponse;

@Component
public class FileOptMapper {

    public FileOpt toEntity(FileOptRequest request) {
    	Objects.requireNonNull(request, "FileOptRequest must not be null");
        FileOpt fileOpt = new FileOpt();
        fileOpt.setExtension(request.extension());
        fileOpt.setMimeType(request.mimeType());
        fileOpt.setMaxSizeInBytes(request.maxSizeInBytes());
        fileOpt.setUploadEnabled(request.uploadEnabled());
        fileOpt.setPreviewEnabled(request.previewEnabled());
        fileOpt.setAvailableActions(request.availableActions());
        return fileOpt;
    }

    public FileOptResponse toResponse(FileOpt fileOpt) {
        return new FileOptResponse(
                fileOpt.getId(),
                fileOpt.getExtension(),
                fileOpt.getMimeType(),
                fileOpt.getMaxSizeInBytes(),
                fileOpt.isUploadEnabled(),
                fileOpt.isPreviewEnabled(),
                fileOpt.getAvailableActions());
    }

}
