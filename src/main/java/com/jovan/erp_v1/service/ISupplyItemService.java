package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jovan.erp_v1.dto.AvgCostByProcurementResponse;
import com.jovan.erp_v1.dto.ProcurementGlobalStatsResponse;
import com.jovan.erp_v1.dto.ProcurementStatsPerEntityResponse;
import com.jovan.erp_v1.dto.ProcurementStatsResponse;
import com.jovan.erp_v1.dto.SumCostGroupedByProcurementResponse;
import com.jovan.erp_v1.dto.SupplierItemCountResponse;
import com.jovan.erp_v1.dto.SupplyItemStatsResponse;
import com.jovan.erp_v1.model.SupplyItem;
import com.jovan.erp_v1.request.CostSumByProcurement;
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
	//nove metode
	List<SupplyItemResponse> findBySupplier_NameContainingIgnoreCase(String supplierName);
	List<SupplyItemResponse> findBySupplier_PhoneNumberLikeIgnoreCase(@Param("phoneNumber") String phoneNumber);
	List<SupplyItemResponse> findBySupplier_EmailLikeIgnoreCase( String email);
	List<SupplyItemResponse> findBySupplier_Address(String address);
	List<SupplyItemResponse> findBySupplierNameContainingAndCostBetween(String supplierName,BigDecimal minCost,BigDecimal maxCost);
	List<SupplyItemResponse> findBySupplierNameAndProcurementDateBetween(String supplierName, LocalDateTime start, LocalDateTime end);
	List<SupplyItemResponse> findByAddressAndMinCost( String address, BigDecimal minCost);
	List<SupplyItemResponse> findByPhoneNumberAndCost(String phoneNumber, BigDecimal cost);
	List<SupplyItemResponse> findByProcurementSupplyItemCount(@Param("count") Integer count);
	List<SupplyItemResponse> findByCost(BigDecimal cost);
	List<SupplyItemResponse> findByCostGreaterThan(BigDecimal cost);
	List<SupplyItemResponse> findByCostLessThan(BigDecimal cost);
	List<SupplyItemResponse> findByProcurementTotalCostGreaterThan(BigDecimal minCost);
	List<SupplyItemResponse> findByProcurementDate( LocalDateTime date);
	List<SupplyItemResponse> findBySupplyAndSalesCountMismatch();
	SupplyItemStatsResponse countAllSupplyItems();
	SupplyItemStatsResponse countByProcurementId( Long procurementId);
	SupplyItemStatsResponse sumAllCosts();
	SupplyItemStatsResponse averageCost();
	SupplyItemStatsResponse minCost();
	SupplyItemStatsResponse maxCost();
	List<SumCostGroupedByProcurementResponse> sumCostGroupedByProcurement();
	List<SupplierItemCountResponse> countBySupplier();
	List<AvgCostByProcurementResponse> avgCostByProcurement();
	ProcurementGlobalStatsResponse procurementStats(); //global bez id
	List<ProcurementStatsPerEntityResponse> procurementPerEntityStats(); // ima u sebi id
	List<CostSumByProcurement> findCostSumGroupedByProcurement();
	List<SupplyItemResponse> findByProcurementWithSupplyCostOver( BigDecimal minTotal);
	List<SupplyItemResponse> findBySupplierWithMoreThanNItems( Long minCount);
	List<CostSumByProcurement> findCostSumGroupedByProcurementWithMinTotal( BigDecimal minTotal);
}
