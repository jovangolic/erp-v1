package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.BillOfMaterialsRequest;
import com.jovan.erp_v1.response.BillOfMaterialsResponse;

public interface IBillOfMaterialsService {

    BillOfMaterialsResponse create(BillOfMaterialsRequest request);
    BillOfMaterialsResponse update(Long id, BillOfMaterialsRequest request);
    void delete(Long id);
    BillOfMaterialsResponse findOne(Long id);
    List<BillOfMaterialsResponse> findAll();
    List<BillOfMaterialsResponse> findByParentProductId(Long parentProductId);
    List<BillOfMaterialsResponse> findByComponentId(Long componentId);
    List<BillOfMaterialsResponse> findByQuantityGreaterThan(BigDecimal quantity);
    List<BillOfMaterialsResponse> findByQuantity(BigDecimal quantity);
    List<BillOfMaterialsResponse> findByQuantityLessThan(BigDecimal quantity);
    
    //nove metode
    List<BillOfMaterialsResponse> filterBOMs(Long parentProductId, Long componentId,BigDecimal minQuantity, BigDecimal maxQuantity);
    List<BillOfMaterialsResponse> findByParentProduct_CurrentQuantity(BigDecimal currentQuantity);
    List<BillOfMaterialsResponse> findByParentProduct_CurrentQuantityLessThan(BigDecimal currentQuantity);
    List<BillOfMaterialsResponse> findByParentProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity);
    List<BillOfMaterialsResponse> findByComponent_CurrentQuantity(BigDecimal currentQuantity);
    List<BillOfMaterialsResponse> findByComponent_CurrentQuantityLessThan(BigDecimal currentQuantity);
    List<BillOfMaterialsResponse> findByComponent_CurrentQuantityGreaterThan(BigDecimal currentQuantity);
    List<BillOfMaterialsResponse> findByParentProductIdAndQuantityGreaterThan(Long parentProductId, BigDecimal quantity);
    List<BillOfMaterialsResponse> findByParentProductIdAndQuantityLessThan(Long parentProductId, BigDecimal quantity);
    BillOfMaterialsResponse findByParentProductIdAndComponentId(Long parentProductId, Long componentId);
    Boolean existsByParentProductIdAndComponentId(Long parentProductId, Long componentId);
    void deleteByParentProductId(Long parentProductId);
    List<BillOfMaterialsResponse> findByParentProductIdOrderByQuantityDesc(Long parentProductId);
    List<BillOfMaterialsResponse> findByParentProductIdOrderByQuantityAsc(Long parentProductId);
    List<BillOfMaterialsResponse> findByQuantityBetween(BigDecimal min, BigDecimal max);
    List<BillOfMaterialsResponse> findComponentsByProductIdAndComponentNameContaining( Long productId,  String name);
    List<BillOfMaterialsResponse> findByComponent_NameContainingIgnoreCase(String name);
    List<BillOfMaterialsResponse> findByParentProduct_NameContainingIgnoreCase(String name);
    List<BillOfMaterialsResponse> findByComponent_GoodsType(GoodsType goodsType);
    List<BillOfMaterialsResponse> findByComponent_SupplierType(SupplierType supplierType);
    List<BillOfMaterialsResponse> findByParentProduct_UnitMeasure(UnitMeasure unitMeasure);
    List<BillOfMaterialsResponse> findByComponent_Shelf_Id( Long shelfId);
    List<BillOfMaterialsResponse> findByParentProduct_Storage_Id(Long storageId);
    List<BillOfMaterialsResponse> findByParentProduct_Shelf_Id( Long shelfId);
    List<BillOfMaterialsResponse> findByComponent_Storage_Id( Long storageId);
    List<BillOfMaterialsResponse> findByParentProduct_GoodsType(GoodsType goodsType);
    List<BillOfMaterialsResponse> findByParentProduct_SupplierType(SupplierType supplierType);
    List<BillOfMaterialsResponse> findByComponent_UnitMeasure(UnitMeasure unitMeasure);
    List<BillOfMaterialsResponse> findByParentProduct_Supply_Id( Long supplyId);
    List<BillOfMaterialsResponse> findByComponent_Supply_Id( Long supplyId);
    List<BillOfMaterialsResponse> findByComponent_StorageType(StorageType type);
    List<BillOfMaterialsResponse> findByParentProduct_StorageType(StorageType type);
    List<BillOfMaterialsResponse> findByParentProduct_Storage_IdAndComponent_GoodsType( Long storageId, GoodsType goodsType);
    List<BillOfMaterialsResponse> findWhereParentAndComponentShareSameStorage();
    List<BillOfMaterialsResponse> findByMinQuantityAndComponentGoodsType(BigDecimal minQuantity, GoodsType goodsType);
    List<BillOfMaterialsResponse> findByComponentStorageAndUnitMeasure( Long storageId, UnitMeasure unitMeasure);
    List<BillOfMaterialsResponse> findAllOrderByQuantityDesc();
    List<BillOfMaterialsResponse> findAllOrderByQuantityAsc();
}
