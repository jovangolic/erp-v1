package com.jovan.erp_v1.request;


import com.jovan.erp_v1.enumeration.StorageStatus;

public record CountWorkCentersByStorageStatusRequest(StorageStatus status, Long count) {
}
