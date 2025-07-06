package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.request.ProductRequest;
import com.jovan.erp_v1.response.ProductResponse;

public interface IProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);

    ProductResponse findById(Long id);

    List<ProductResponse> findAll();

    ProductResponse findByBarCode(String code);

    List<ProductResponse> findByCurrentQuantityLessThan(BigDecimal quantity);

    List<ProductResponse> findByName(String name);

    List<ProductResponse> findByStorageId(Long storageId);

    List<ProductResponse> findBySupplierType(SupplierType supplierType);

    List<ProductResponse> findByStorageType(StorageType storageType);

    List<ProductResponse> findByGoodsType(GoodsType goodsType);
    //nove metode
    
    List<ProductResponse> findByUnitMeasure(UnitMeasure unitMeasure);
    List<ProductResponse> findByShelfRowColAndStorage( Integer row,Integer col, Long storageId);
	List<ProductResponse> findByShelfRow(Integer row);

	List<ProductResponse> findByShelfColumn(Integer col);
	
	List<ProductResponse> findBySupplyMinQuantity(BigDecimal quantity);
	
	List<ProductResponse> findBySupplyUpdateRange(LocalDateTime from,LocalDateTime to);

	List<ProductResponse> findBySupplyStorageId(Long storageId);
	Long countByShelfRowCount(Integer rowCount);
	Long countByShelfCols(Integer cols);
}