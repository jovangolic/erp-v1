package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.request.StorageRequest;
import com.jovan.erp_v1.response.StorageResponse;

public interface IStorageService {

    StorageResponse createStorage(StorageRequest request);
    StorageResponse updateStorage(Long id, StorageRequest request);
    void deleteStorage(Long id);
    List<StorageResponse> getByStorageType(StorageType type);
    StorageResponse getByStorageId(Long id);
    List<StorageResponse> getByName(String name);
    List<StorageResponse> getByLocation(String location);
    List<StorageResponse> getByCapacity(BigDecimal capacity);
    List<StorageResponse> getByNameAndLocation(String name, String location);
    List<StorageResponse> getByTypeAndCapacityGreaterThan(StorageType type, BigDecimal capacity);
    List<StorageResponse> getStoragesWithMinGoods(Integer minCount);
    List<StorageResponse> getByNameContainingIgnoreCase(String name);
    List<StorageResponse> getAllStorage();
    
    //nove metode
    List<StorageResponse> findByTypeAndCapacityLessThan(StorageType type, BigDecimal capacity);
    List<StorageResponse> findByCapacityGreaterThan(BigDecimal capacity);
    List<StorageResponse> findByCapacityLessThan(BigDecimal capacity);
    List<StorageResponse> findByNameAndLocationAndCapacity(String name, String location, BigDecimal capacity);
    List<StorageResponse> findByTypeAndLocation(StorageType type, String location);
    List<StorageResponse> findByTypeAndName(StorageType type, String name);
    List<StorageResponse> findByLocationAndCapacity(String location, BigDecimal capacity);
    List<StorageResponse> findByTypeAndCapacity(StorageType type, BigDecimal capacity);
    List<StorageResponse> findByTypeAndLocationAndCapacity(StorageType type, String location, BigDecimal capacity);
    List<StorageResponse> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String name, String location);
    List<StorageResponse> findByCapacityBetween(BigDecimal min, BigDecimal max);
    List<StorageResponse> findByTypeOrderByCapacityDesc(StorageType type);
    List<StorageResponse> findByLocationOrderByNameAsc(String location);
    List<StorageResponse> findStoragesWithoutGoods();
    List<StorageResponse> findByExactShelfCount(Integer shelfCount);
    List<StorageResponse> findByLocationContainingIgnoreCaseAndType(String location, StorageType type);
    List<StorageResponse> findStoragesWithMaterials();
    List<StorageResponse> findStoragesWithWorkCenters();
    List<StorageResponse> findStoragesWithoutShelves();
    List<StorageResponse> findAvailableStorages();
    List<StorageResponse> findSuitableStoragesForShipment( BigDecimal minCapacity);
    List<StorageResponse> findEmptyStorages();
    List<StorageResponse> findStorageWithoutGoodsAndMaterialsByType( StorageType type);
    List<StorageResponse> findStorageWithGoodsAndMaterialsByType( StorageType type);
    List<StorageResponse> findStorageWithGoodsOrMaterialsByType( StorageType type);
    List<StorageResponse> findAllByType(StorageType type);
    List<StorageResponse> findEmptyStorageByType( StorageType type);
    List<StorageResponse> findProductionStorage();
    List<StorageResponse> findDistributionStorage();
    List<StorageResponse> findOpenStorage();
    List<StorageResponse> findClosedStorage();
    List<StorageResponse> findInterimStorage();
    List<StorageResponse> findAvailableStorage();
    
    Boolean findFirstByTypeAndHasShelvesForIsNotNull(StorageType type);
    
    //dodato
    BigDecimal getAvailableCapacity(Long storageId);
    void allocateCapacity(Long storageId, BigDecimal amount);
    void releaseCapacity(Long storageId, BigDecimal amount);
    boolean hasCapacity(Long storageId, BigDecimal amount);
}
