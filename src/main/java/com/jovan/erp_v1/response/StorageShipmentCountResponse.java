package com.jovan.erp_v1.response;

import com.jovan.erp_v1.dto.StorageShipmentCountDTO;

public record StorageShipmentCountResponse(String storageName, Long shipmentCount) {

	public StorageShipmentCountResponse(StorageShipmentCountDTO dto) {
		this(dto.storageName(), dto.shipmentCount());
	}
}

