package com.jovan.erp_v1.service;

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

    List<StorageResponse> getByCapacity(Double capacity);

    List<StorageResponse> getByNameAndLocation(String name, String location);

    List<StorageResponse> getByTypeAndCapacityGreaterThan(StorageType type, Double capacity);

    List<StorageResponse> getStoragesWithMinGoods(int minCount);

    List<StorageResponse> getByNameContainingIgnoreCase(String name);

    List<StorageResponse> getAllStorage();
}
