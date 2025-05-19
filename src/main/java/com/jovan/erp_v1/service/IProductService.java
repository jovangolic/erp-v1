package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.request.ProductRequest;
import com.jovan.erp_v1.response.ProductResponse;

public interface IProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);

    ProductResponse findById(Long id);

    List<ProductResponse> findAll();

    ProductResponse findByBarCode(String code);

    List<ProductResponse> findByCurrentQuantityLessThan(Double quantity);

    List<ProductResponse> findByName(String name);

    List<ProductResponse> findByStorageId(Long storageId);

    List<ProductResponse> findBySupplierType(SupplierType supplierType);

    List<ProductResponse> findByStorageType(StorageType storageType);

    List<ProductResponse> findByGoodsType(GoodsType goodsType);
}