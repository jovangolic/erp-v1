package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.response.GoodsResponse;
import com.jovan.erp_v1.search_request.GoodsSearchRequest;

public interface IGoodsService {

    List<GoodsResponse> findByName(String name);
    List<GoodsResponse> findByBarCodes(String barCode);
    List<GoodsResponse> findByUnitMeasure(UnitMeasure unitMeasure);
    List<GoodsResponse> findBySupplierType(SupplierType type);
    List<GoodsResponse> findByStorageType(StorageType type);
    List<GoodsResponse> findByGoodsType(GoodsType type);
    List<GoodsResponse> findByStorageName(String storageName);
    List<GoodsResponse> findBySupplyId(Long supplyId);
    List<GoodsResponse> findByBarCodeAndGoodsType(String barCode, GoodsType goodsType);
    List<GoodsResponse> findByBarCodeAndStorageId(String barCode, Long storageId);
    GoodsResponse findSingleByBarCode(String barCode);
    List<GoodsResponse> findByStorageId(Long storageId);
    
    //nove metode
    List<GoodsResponse> generalSearch(GoodsSearchRequest request);
}
