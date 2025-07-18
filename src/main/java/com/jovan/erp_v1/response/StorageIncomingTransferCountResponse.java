package com.jovan.erp_v1.response;

import com.jovan.erp_v1.dto.StorageIncomingTransferCountDTO;

public record StorageIncomingTransferCountResponse(String storageName, Long incomingTransferCount) {

	public StorageIncomingTransferCountResponse(StorageIncomingTransferCountDTO dto) {
		this(dto.storageName(), dto.incomingTransferCount());
	}
}
