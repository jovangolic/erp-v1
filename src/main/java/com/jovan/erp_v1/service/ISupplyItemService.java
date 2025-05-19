package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


import com.jovan.erp_v1.request.SupplyItemRequest;
import com.jovan.erp_v1.response.SupplyItemResponse;

public interface ISupplyItemService {

	SupplyItemResponse createSupplyItem(SupplyItemRequest request);
	SupplyItemResponse updateSupplyItem(Long id, SupplyItemRequest request);
	void deleteSupplyItem(Long id);
	SupplyItemResponse getOneById(Long supplyItemId);
	List<SupplyItemResponse> getAllSupplyItem();
	List<SupplyItemResponse> getByProcurementId(Long procurementId);
    List<SupplyItemResponse> getBySupplierId(Long supplierId);
    List<SupplyItemResponse> getByCostBetween(BigDecimal min, BigDecimal max);
	List<SupplyItemResponse> getByProcurementDateBetween(LocalDateTime startDate, LocalDateTime endDate);
	List<SupplyItemResponse> getByProcurementDateAndCostBetween(LocalDateTime startDate, LocalDateTime endDate, BigDecimal min, BigDecimal max);
	List<SupplyItemResponse> getByProcurementAndVendor(Long procurementId, Long vendorId);
	List<SupplyItemResponse> getByVendorAndProcurementAndCost(Long supplierId, Long procurementId, BigDecimal minCost);
	List<SupplyItemResponse> getByDateAndCost(LocalDateTime startDate, LocalDateTime endDate, BigDecimal min, BigDecimal max);
	List<SupplyItemResponse> getBySupplierNameAndProcurementDateAndMaxCost(String supplierName, LocalDateTime startDate, LocalDateTime endDate, BigDecimal maxCost);
	
}
