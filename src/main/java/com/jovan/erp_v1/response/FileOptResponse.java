package com.jovan.erp_v1.response;

import java.util.Set;

import com.jovan.erp_v1.enumeration.FileAction;
import com.jovan.erp_v1.enumeration.FileExtension;

public record FileOptResponse(

        Long id,
        FileExtension extension,
        String mimeType,
        Long maxSizeInBytes,
        boolean uploadEnabled,
        boolean previewEnabled,
        Set<FileAction> availableActions) {

}
