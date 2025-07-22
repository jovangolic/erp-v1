package com.jovan.erp_v1.response;


import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.request.CountWorkCentersByStorageTypeRequest;

public record CountWorkCentersByStorageTypeResponse(StorageType type, Long count) {

	public CountWorkCentersByStorageTypeResponse(CountWorkCentersByStorageTypeRequest dto) {
		this(dto.type(), dto.count());
	}
}
