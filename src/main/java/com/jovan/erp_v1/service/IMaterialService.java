package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.request.MaterialRequest;
import com.jovan.erp_v1.response.MaterialResponse;

public interface IMaterialService {

    MaterialResponse create(MaterialRequest request);

    MaterialResponse update(Long id, MaterialRequest request);

    void delete(Long id);

    MaterialResponse findOne(Long id);

    List<MaterialResponse> findAll();

    List<MaterialResponse> searchMaterials(String name, String code, UnitOfMeasure unit,
            BigDecimal currentStock, Long storageId, BigDecimal reorderLevel);

    List<MaterialResponse> findByStorage_Id(Long storageId);

    List<MaterialResponse> findByCode(String code);

    List<MaterialResponse> findByNameContainingIgnoreCase(String name);

    List<MaterialResponse> search(String name, String code);

    List<MaterialResponse> findByUnit(UnitOfMeasure unit);

    List<MaterialResponse> findByStorage_Name(String storageName);

    List<MaterialResponse> findByStorage_Capacity(BigDecimal capacity);

    List<MaterialResponse> findByStorage_Type(StorageType type);

    List<MaterialResponse> findByCurrentStock(BigDecimal currentStock);

    List<MaterialResponse> findByReorderLevel(BigDecimal reorderLevel);
    
    List<MaterialResponse> findByCurrentStockGreaterThan(BigDecimal currentStock);
    List<MaterialResponse> findByCurrentStockLessThan(BigDecimal currentStock);
}
