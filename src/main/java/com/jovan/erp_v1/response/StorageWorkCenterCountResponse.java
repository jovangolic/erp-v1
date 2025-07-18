package com.jovan.erp_v1.response;

import com.jovan.erp_v1.dto.StorageWorkCenterCountDTO;

public record StorageWorkCenterCountResponse(String storageName, Long workCenterCount) {
    public StorageWorkCenterCountResponse(StorageWorkCenterCountDTO dto) {
        this(dto.storageName(), dto.workCenterCount());
    }
}
