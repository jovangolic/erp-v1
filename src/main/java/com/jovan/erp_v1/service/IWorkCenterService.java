package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.request.WorkCenterRequest;
import com.jovan.erp_v1.response.CountWorkCenterCapacityResponse;
import com.jovan.erp_v1.response.CountWorkCenterResultResponse;
import com.jovan.erp_v1.response.CountWorkCentersByStorageStatusResponse;
import com.jovan.erp_v1.response.CountWorkCentersByStorageTypeResponse;
import com.jovan.erp_v1.response.WorkCenterResponse;

public interface IWorkCenterService {

    WorkCenterResponse create(WorkCenterRequest request);
    WorkCenterResponse update(Long id, WorkCenterRequest request);
    void delete(Long id);
    WorkCenterResponse findOne(Long id);
    List<WorkCenterResponse> findAll();
    List<WorkCenterResponse> findByName(String name);
    List<WorkCenterResponse> findByCapacity(BigDecimal capacity);
    List<WorkCenterResponse> findByLocation(String location);
    List<WorkCenterResponse> findByNameAndLocation(String name, String location);
    List<WorkCenterResponse> findByCapacityGreaterThan(BigDecimal capacity);
    List<WorkCenterResponse> findByCapacityLessThan(BigDecimal capacity);
    List<WorkCenterResponse> findByNameContainingIgnoreCase(String name);
    List<WorkCenterResponse> findByLocationContainingIgnoreCase(String location);
    List<WorkCenterResponse> findByCapacityBetween(BigDecimal min, BigDecimal max);
    List<WorkCenterResponse> findByLocationOrderByCapacityDesc(String location);
    List<WorkCenterResponse> findByLocalStorage_Id(Long localStorageId);
    List<WorkCenterResponse> findByLocalStorage_NameContainingIgnoreCase(String localStorageName);
    List<WorkCenterResponse> findByLocalStorage_LocationContainingIgnoreCase(String localStorageLocation);
    List<WorkCenterResponse> findByLocalStorage_Capacity(BigDecimal capacity);
    List<WorkCenterResponse> findByLocalStorage_CapacityLessThan(BigDecimal capacity);
    List<WorkCenterResponse> findByLocalStorage_CapacityGreaterThan(BigDecimal capacity);
    List<WorkCenterResponse> findByLocalStorage_Type(StorageType localStorageType);
    
    //nove metode
    List<WorkCenterResponse> filterWorkCenters(String name, String location,BigDecimal capacityMin, BigDecimal capacityMax,StorageType type, StorageStatus status);
    List<WorkCenterResponse> findByTypeProduction();
    List<WorkCenterResponse> findByTypeDistribution();
    List<WorkCenterResponse> findByTypeOpen();
    List<WorkCenterResponse> findByTypeClosed();
    List<WorkCenterResponse> findByTypeInterim();
    List<WorkCenterResponse> findByTypeAvailable();
    List<WorkCenterResponse> findByLocalStorage_Status(StorageStatus status);
    List<WorkCenterResponse> findByStatusActive();
    List<WorkCenterResponse> findByStatusUnder_Maintenance();
    List<WorkCenterResponse> findByStatusDecommissioned();
    List<WorkCenterResponse> findByStatusReserved();
    List<WorkCenterResponse> findByStatusTemporary();
    List<WorkCenterResponse> findByStatusFull();
    List<WorkCenterResponse> findByLocationAndCapacityGreaterThan(String location, BigDecimal capacity);
    List<WorkCenterResponse> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String name, String location);
    List<WorkCenterResponse> findByLocalStorage_TypeAndLocalStorage_Status(StorageType type, StorageStatus status);
    List<WorkCenterResponse> findByIdBetween(Long startId, Long endId);
    List<WorkCenterResponse> findByLocalStorageIsNull();
    List<WorkCenterResponse> findByLocalStorageIsNotNull();
    List<WorkCenterResponse> findAllByOrderByCapacityAsc();
    List<WorkCenterResponse> findAllByOrderByCapacityDesc();
    List<WorkCenterResponse> findByLocationIn(List<String> locations);
    List<WorkCenterResponse> findByNameContainingIgnoreCaseAndLocalStorage_Status(String name, StorageStatus status);
    List<WorkCenterResponse> findByLocationContainingIgnoreCaseAndLocalStorage_Type(String location, StorageType type);
    List<CountWorkCenterCapacityResponse>  countWorkCentersByCapacity();
    Long countWorkCentersByCapacityLessThan( BigDecimal capacity);
    Long countWorkCentersByCapacityGreaterThan( BigDecimal capacity);
    List<CountWorkCenterResultResponse> countWorkCentersByLocation();
    List<CountWorkCentersByStorageStatusResponse> countWorkCentersByStorageStatus();
    List<CountWorkCentersByStorageTypeResponse> countWorkCentersByStorageType();
}
