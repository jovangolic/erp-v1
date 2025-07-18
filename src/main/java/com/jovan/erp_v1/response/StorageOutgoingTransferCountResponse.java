package com.jovan.erp_v1.response;

import com.jovan.erp_v1.dto.StorageOutgoingTransferCountDTO;

public record StorageOutgoingTransferCountResponse(String storageName, Long outgoingTransferCount) {

	public StorageOutgoingTransferCountResponse(StorageOutgoingTransferCountDTO dto) {
		this(dto.storageName(), dto.outgoingTransferCount());
	}
}
