package com.jovan.erp_v1.request;


import com.jovan.erp_v1.enumeration.StorageType;

public record CountWorkCentersByStorageTypeRequest(StorageType type, Long count) {
}
