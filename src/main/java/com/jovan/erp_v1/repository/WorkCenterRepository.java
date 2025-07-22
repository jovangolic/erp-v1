package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.request.CountWorkCenterCapacityRequest;
import com.jovan.erp_v1.request.CountWorkCenterResultRequest;
import com.jovan.erp_v1.request.CountWorkCentersByStorageStatusRequest;
import com.jovan.erp_v1.request.CountWorkCentersByStorageTypeRequest;

@Repository
public interface WorkCenterRepository extends JpaRepository<WorkCenter, Long>, JpaSpecificationExecutor<WorkCenter> {

    List<WorkCenter> findByName(String name);
    List<WorkCenter> findByCapacity(BigDecimal capacity);
    List<WorkCenter> findByLocation(String location);
    List<WorkCenter> findByNameAndLocation(String name, String location);
    List<WorkCenter> findByCapacityGreaterThan(BigDecimal capacity);
    List<WorkCenter> findByCapacityLessThan(BigDecimal capacity);
    List<WorkCenter> findByNameContainingIgnoreCase(String name);
    List<WorkCenter> findByLocationContainingIgnoreCase(String location);
    List<WorkCenter> findByCapacityBetween(BigDecimal min, BigDecimal max);
    List<WorkCenter> findByLocationOrderByCapacityDesc(String location);
    List<WorkCenter> findByLocalStorage_Id(Long localStorageId);
    List<WorkCenter> findByLocalStorage_NameContainingIgnoreCase(String localStorageName);
    List<WorkCenter> findByLocalStorage_LocationContainingIgnoreCase(String localStorageLocation);
    List<WorkCenter> findByLocalStorage_Capacity(BigDecimal capacity);
    List<WorkCenter> findByLocalStorage_CapacityLessThan(BigDecimal capacity);
    List<WorkCenter> findByLocalStorage_CapacityGreaterThan(BigDecimal capacity);
    List<WorkCenter> findByLocalStorage_Type(StorageType localStorageType);

    //nove metode
    @Query("SELECT wc FROM WorkCenter wc WHERE wc.localStorage.type =  'PRODUCTION'")
    List<WorkCenter> findByTypeProduction();
    @Query("SELECT wc FROM WorkCenter wc WHERE wc.localStorage.type =  'DISTRIBUTION'")
    List<WorkCenter> findByTypeDistribution();
    @Query("SELECT wc FROM WorkCenter wc WHERE wc.localStorage.type =  'OPEN'")
    List<WorkCenter> findByTypeOpen();
    @Query("SELECT wc FROM WorkCenter wc WHERE wc.localStorage.type =  'CLOSED'")
    List<WorkCenter> findByTypeClosed();
    @Query("SELECT wc FROM WorkCenter wc WHERE wc.localStorage.type =  'INTERIM'")
    List<WorkCenter> findByTypeInterim();
    @Query("SELECT wc FROM WorkCenter wc WHERE wc.localStorage.type =  'AVAILABLE'")
    List<WorkCenter> findByTypeAvailable();
    List<WorkCenter> findByLocalStorage_Status(StorageStatus status);
    @Query("SELECT wc FROM WorkCenter  wc WHERE wc.localStorage.status  = 'ACTIVE'")
    List<WorkCenter> findByStatusActive();
    @Query("SELECT wc FROM WorkCenter  wc WHERE wc.localStorage.status  = 'UNDER_MAINTENANCE'")
    List<WorkCenter> findByStatusUnder_Maintenance();
    @Query("SELECT wc FROM WorkCenter  wc WHERE wc.localStorage.status  = 'DECOMMISSIONED'")
    List<WorkCenter> findByStatusDecommissioned();
    @Query("SELECT wc FROM WorkCenter  wc WHERE wc.localStorage.status  = 'RESERVED'")
    List<WorkCenter> findByStatusReserved();
    @Query("SELECT wc FROM WorkCenter  wc WHERE wc.localStorage.status  = 'TEMPORARY'")
    List<WorkCenter> findByStatusTemporary();
    @Query("SELECT wc FROM WorkCenter  wc WHERE wc.localStorage.status  = 'FULL'")
    List<WorkCenter> findByStatusFull();
    List<WorkCenter> findByLocationAndCapacityGreaterThan(String location, BigDecimal capacity);
    List<WorkCenter> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String name, String location);
    List<WorkCenter> findByLocalStorage_TypeAndLocalStorage_Status(StorageType type, StorageStatus status);
    List<WorkCenter> findByIdBetween(Long startId, Long endId);
    List<WorkCenter> findByLocalStorageIsNull();
    List<WorkCenter> findByLocalStorageIsNotNull();
    List<WorkCenter> findAllByOrderByCapacityAsc();
    List<WorkCenter> findAllByOrderByCapacityDesc();
    List<WorkCenter> findByLocationIn(List<String> locations);
    List<WorkCenter> findByNameContainingIgnoreCaseAndLocalStorage_Status(String name, StorageStatus status);
    List<WorkCenter> findByLocationContainingIgnoreCaseAndLocalStorage_Type(String location, StorageType type);
    @Query("SELECT new com.jovan.erp_v1.request.CountWorkCenterCapacityRequest(wc.capacity, COUNT(wc)) FROM WorkCenter wc GROUP BY wc.capacity")
    List<CountWorkCenterCapacityRequest> countWorkCentersByCapacity();
    @Query("SELECT COUNT(wc) FROM WorkCenter wc WHERE wc.capacity <= :capacity")
    Long countWorkCentersByCapacityLessThan(@Param("capacity") BigDecimal capacity);
    @Query("SELECT COUNT(wc) FROM WorkCenter wc WHERE wc.capacity >= :capacity")
    Long countWorkCentersByCapacityGreaterThan(@Param("capacity") BigDecimal capacity);
    @Query("SELECT new com.jovan.erp_v1.request.CountWorkCenterResultRequest(wc.location, COUNT(wc)) FROM WorkCenter wc GROUP BY wc.location")
    List<CountWorkCenterResultRequest> countWorkCentersByLocation();
    @Query("""
    	    SELECT new com.jovan.erp_v1.request.CountWorkCentersByStorageStatusRequest(
    	        wc.localStorage.status, COUNT(wc)
    	    ) 
    	    FROM WorkCenter wc 
    	    GROUP BY wc.localStorage.status
    	""")
    List<CountWorkCentersByStorageStatusRequest> countWorkCentersByStorageStatus();
    @Query("SELECT new com.jovan.erp_v1.request.CountWorkCentersByStorageTypeRequest(wc.localStorage.type, COUNT(wc)) FROM WorkCenter wc GROUP BY wc.localStorage.type")
    List<CountWorkCentersByStorageTypeRequest> countWorkCentersByStorageType();
    
}
