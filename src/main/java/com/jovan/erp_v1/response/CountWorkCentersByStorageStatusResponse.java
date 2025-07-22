package com.jovan.erp_v1.response;



import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.request.CountWorkCentersByStorageStatusRequest;

public record CountWorkCentersByStorageStatusResponse(StorageStatus status, Long count) {
	
	public CountWorkCentersByStorageStatusResponse(CountWorkCentersByStorageStatusRequest dto) {
		this(dto.status(), dto.count());
	}
}
