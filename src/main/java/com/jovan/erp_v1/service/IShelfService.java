package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.request.ShelfRequest;
import com.jovan.erp_v1.response.ShelfResponse;
import com.jovan.erp_v1.response.ShelfResponseWithGoods;

public interface IShelfService {
    // Osnovne CRUD operacije
    ShelfResponse createShelf(ShelfRequest request);

    ShelfResponse updateShelf(Long id, ShelfRequest request);

    void deleteShelf(Long id);

    ShelfResponse findOne(Long id);

    List<ShelfResponse> findAll();

    // Provere postojanja
    boolean existsByRowCountAndStorageId(Integer rows, Long storageId);

    boolean existsByColsAndStorageId(Integer cols, Long storageId);

    boolean existsByRowCountAndColsAndStorageId(Integer rows, Integer cols, Long storageId);

    // Pretrage
    List<ShelfResponse> findByStorageId(Long storageId);

    Optional<ShelfResponse> findByRowCountAndColsAndStorageId(Integer rows, Integer cols, Long storageId);

    List<ShelfResponse> findByRowCountAndStorageId(Integer rows, Long storageId);

    List<ShelfResponse> findByColsAndStorageId(Integer cols, Long storageId);

    ShelfResponseWithGoods getShelfWithGoods(Long shelfId);
    
    //nove metode
    List<ShelfResponse> findByStorage_Name(String name);
	List<ShelfResponse> findByStorage_Location(String location);
	List<ShelfResponse> findByStorage_Type(StorageType type);
	List<ShelfResponse> findByStorage_CapacityGreaterThan(BigDecimal capacity);
	List<ShelfResponse> findByStorage_NameAndStorage_Type(String name, StorageType type);
}
