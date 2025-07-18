package com.jovan.erp_v1.response;

import com.jovan.erp_v1.dto.StorageMaterialCountDTO;

public record StorageMaterialCountResponse(String storageName, Long materialCount) {

	public StorageMaterialCountResponse(StorageMaterialCountDTO dto) {
		this(dto.storageName(), dto.materialCount());
	}
}
