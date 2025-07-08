package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.RawMaterial;
import com.jovan.erp_v1.request.RawMaterialRequest;
import com.jovan.erp_v1.response.ProductResponse;
import com.jovan.erp_v1.response.RawMaterialResponse;

public interface IRawMaterialService {

    List<RawMaterialResponse> findAll();

    RawMaterialResponse findById(Long id);

    RawMaterialResponse save(RawMaterialRequest request);

    RawMaterialResponse update(Long id, RawMaterialRequest request);

    void delete(Long id);

    List<RawMaterialResponse> findByName(String name);

    List<RawMaterial> filterRawMaterial(Long shelfId, BigDecimal minQty, BigDecimal maxQty, Long productId);

    // nove metode
    List<RawMaterialResponse> findByCurrentQuantity(BigDecimal currentQuantity);

    List<RawMaterialResponse> findByCurrentQuantityLessThan(BigDecimal currentQuantity);

    List<RawMaterialResponse> findByCurrentQuantityGreaterThan(BigDecimal currentQuantity);

    List<RawMaterialResponse> findByProduct_CurrentQuantity(BigDecimal currentQuantity);

    List<RawMaterialResponse> findByProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity);

    List<RawMaterialResponse> findByProduct_CurrentQuantityLessThan(BigDecimal currentQuantity);

    List<RawMaterialResponse> findByShelf_Id(Long shelfId);

    Long countByShelf_RowCount(Integer rowCount);

    Long countByShelf_Cols(Integer cols);

    List<RawMaterialResponse> findByShelfAndQuantityGreaterThan(Long shelfId, BigDecimal quantity);

    List<RawMaterialResponse> findByShelfAndQuantityLessThan(Long shelfId, BigDecimal quantity);

    List<RawMaterialResponse> findByShelfAndExactQuantity(Long shelfId, BigDecimal quantity);

    List<RawMaterialResponse> findByShelf_IdAndCurrentQuantityGreaterThan(Long shelfId, BigDecimal quantity);

    List<RawMaterialResponse> findByShelf_IdAndCurrentQuantityLessThan(Long shelfId, BigDecimal quantity);

    List<RawMaterialResponse> findBySupplierType(SupplierType supplierType);

    List<RawMaterialResponse> findByStorageType(StorageType storageType);

    List<RawMaterialResponse> findByGoodsType(GoodsType goodsType);

    List<RawMaterialResponse> findByUnitMeasure(UnitMeasure unitMeasure);
}
