package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.enumeration.ProductionOrderStatus;
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

    List<ProductionOrderResponse> findByProduct_CurrentQuantity(Double currentQuantity);

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
}
