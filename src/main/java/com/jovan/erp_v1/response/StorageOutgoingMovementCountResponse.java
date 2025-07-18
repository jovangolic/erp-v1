package com.jovan.erp_v1.response;

import com.jovan.erp_v1.dto.StorageOutgoingMovementCountDTO;

public record StorageOutgoingMovementCountResponse(String storageName, Long outgoingMovementCount) {
	public StorageOutgoingMovementCountResponse(StorageOutgoingMovementCountDTO dto) {
		this(dto.storageName(), dto.outgoingMovementCount());
	}

}
