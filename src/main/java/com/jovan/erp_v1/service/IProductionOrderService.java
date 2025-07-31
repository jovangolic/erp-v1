package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.enumeration.ProductionOrderStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.ProductionOrderRequest;
import com.jovan.erp_v1.response.ProductionOrderResponse;

public interface IProductionOrderService {

    ProductionOrderResponse create(ProductionOrderRequest request);
    ProductionOrderResponse update(Long id, ProductionOrderRequest request);
    void delete(Long id);
    ProductionOrderResponse findOne(Long id);
    List<ProductionOrderResponse> findAll();
    ProductionOrderResponse findByOrderNumber(String orderNumber);
    List<ProductionOrderResponse> findByProduct_Id(Long productId);
    List<ProductionOrderResponse> findByProduct_NameContainingIgnoreCase(String name);
    List<ProductionOrderResponse> findByProduct_CurrentQuantity(BigDecimal currentQuantity);
    List<ProductionOrderResponse> findByStatus(ProductionOrderStatus status);
    List<ProductionOrderResponse> findByWorkCenter_Id(Long workCenterId);
    List<ProductionOrderResponse> findByWorkCenter_NameContainingIgnoreCase(String name);
    List<ProductionOrderResponse> findByWorkCenter_LocationContainingIgnoreCase(String location);
    List<ProductionOrderResponse> findByWorkCenter_Capacity(Integer capacity);
    List<ProductionOrderResponse> findByWorkCenter_CapacityGreaterThan(Integer capacity);
    List<ProductionOrderResponse> findByWorkCenter_CapacityLessThan(Integer capacity);
    List<ProductionOrderResponse> findByQuantityPlanned(Integer quantityPlanned);
    List<ProductionOrderResponse> findByQuantityProduced(Integer quantityProduced);
    List<ProductionOrderResponse> findByStartDateBetween(LocalDate start, LocalDate end);
    List<ProductionOrderResponse> findByStartDate(LocalDate startDate);
    List<ProductionOrderResponse> findByEndDate(LocalDate endDate);
    List<ProductionOrderResponse> searchOrders(String productName, String workCenterName, LocalDate startDateFrom,
            LocalDate startDateTo, ProductionOrderStatus status);
    List<ProductionOrderResponse> findByStartDateGreaterThanEqual(LocalDate startDate);
    List<ProductionOrderResponse> findOrdersWithStartDateAfterOrEqual(LocalDate startDate);
    
    //nove metode
    BigDecimal countAvailableCapacity(Long storageId);
    boolean hasCapacityFor(Long storageId, BigDecimal amount);
    void allocateCapacity(Long storageId, BigDecimal amount);
    void releaseCapacity(Long storageId, BigDecimal amount);
    List<ProductionOrderResponse> findByProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity);
    List<ProductionOrderResponse> findByProduct_CurrentQuantityLessThan(BigDecimal currentQuantity);
    List<ProductionOrderResponse> findByProduct_UnitMeasure(UnitMeasure unitMeasure);
    List<ProductionOrderResponse> findByProduct_SupplierType(SupplierType supplierType);
    List<ProductionOrderResponse> findByProduct_StorageType(StorageType storageType);
    List<ProductionOrderResponse> findByProduct_StorageId( Long storageId);
    List<ProductionOrderResponse> findByProduct_StorageNameContainingIgnoreCase( String storageId);
    List<ProductionOrderResponse> findByProduct_StorageLocationContainingIgnoreCase( String storageId);
    List<ProductionOrderResponse> findByProduct_StorageCapacity( BigDecimal capacity);
    List<ProductionOrderResponse> findByProduct_StorageCapacityGreaterThan( BigDecimal capacity);
    List<ProductionOrderResponse> findByProduct_StorageCapacityLessThan( BigDecimal capacity);
    List<ProductionOrderResponse> findByProduct_SupplyId( Long supplyId);
    List<ProductionOrderResponse> findByProduct_SupplyQuantity(BigDecimal quantity);
    List<ProductionOrderResponse> findByProduct_SupplyQuantityGreaterThan( BigDecimal quantity);
    List<ProductionOrderResponse> findByProduct_SupplyQuantityLessThan( BigDecimal quantity);
    List<ProductionOrderResponse> findByProduct_SupplyUpdates( LocalDateTime updates);
    List<ProductionOrderResponse> findByProduct_SupplyUpdatesBetween( LocalDateTime updatesStart, LocalDateTime updatesEnd);
    List<ProductionOrderResponse> findByProduct_ShelfId(Long shelfId);
    List<ProductionOrderResponse> findByProduct_ShelfRowCount( Integer rowCount);
    List<ProductionOrderResponse> findByProduct_ShelfCols( Integer cols);
    List<ProductionOrderResponse> findByProduct_ShelfIsNull();
}
