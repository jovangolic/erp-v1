package com.jovan.erp_v1.response;

import com.jovan.erp_v1.dto.StorageIncomingMovementCountDTO;

public record StorageIncomingMovementCountResponse(String storageName, Long incomingMovementCount) {
	public StorageIncomingMovementCountResponse(StorageIncomingMovementCountDTO dto) {
		this(dto.storageName(), dto.incomingMovementCount());
	}

}
