package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.WorkCenter;

@Repository
public interface WorkCenterRepository extends JpaRepository<WorkCenter, Long> {

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

}
