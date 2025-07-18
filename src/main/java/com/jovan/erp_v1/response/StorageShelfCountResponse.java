package com.jovan.erp_v1.response;

import com.jovan.erp_v1.dto.StorageShelfCountDTO;

public record StorageShelfCountResponse(String storageName, Long shelfCount) {

	public StorageShelfCountResponse(StorageShelfCountDTO dto) {
		this(dto.storageName(), dto.shelfCount());
	}
}
