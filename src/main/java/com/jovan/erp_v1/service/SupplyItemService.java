package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.dto.AvgCostByProcurementResponse;
import com.jovan.erp_v1.dto.ProcurementGlobalStatsResponse;
import com.jovan.erp_v1.dto.ProcurementStatsPerEntityResponse;
import com.jovan.erp_v1.dto.SumCostGroupedByProcurementResponse;
import com.jovan.erp_v1.dto.SupplierItemCountResponse;
import com.jovan.erp_v1.dto.SupplyItemStatsResponse;
import com.jovan.erp_v1.exception.ProcurementNotFoundException;
import com.jovan.erp_v1.exception.SupplierNotFoundException;
import com.jovan.erp_v1.exception.SupplyItemNotFoundException;
import com.jovan.erp_v1.mapper.SupplyItemMapper;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.model.SupplyItem;
import com.jovan.erp_v1.model.Vendor;
import com.jovan.erp_v1.repository.ProcurementRepository;
import com.jovan.erp_v1.repository.SupplyItemRepository;
import com.jovan.erp_v1.repository.VendorRepository;
import com.jovan.erp_v1.request.CostSumByProcurement;
import com.jovan.erp_v1.request.SupplyItemRequest;
import com.jovan.erp_v1.response.SupplyItemResponse;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplyItemService implements ISupplyItemService {

	private final SupplyItemRepository supplyItemRepository;
	private final SupplyItemMapper supplyItemMapper;
	private final ProcurementRepository procurementRepository;
	private final VendorRepository vendorRepository;

	@Transactional
	@Override
	public SupplyItemResponse createSupplyItem(SupplyItemRequest request) {
		Procurement procurement = procurementRepository.findById(request.procurementId())
				.orElseThrow(() -> new ProcurementNotFoundException(
						"Procurement not found with id: " + request.procurementId()));

		Vendor vendor = vendorRepository.findById(request.vendorId())
				.orElseThrow(() -> new SupplierNotFoundException("Vendor not found with id: " + request.vendorId()));

		SupplyItem supplyItem = supplyItemMapper.toEntity(request);
		supplyItem.setProcurement(procurement);
		supplyItem.setSupplier(vendor);

		SupplyItem saved = supplyItemRepository.save(supplyItem);
		return supplyItemMapper.toResponse(saved);
	}

	@Transactional
	@Override
	public SupplyItemResponse updateSupplyItem(Long id, SupplyItemRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		SupplyItem supplyItem = supplyItemRepository.findById(id)
				.orElseThrow(() -> new SupplyItemNotFoundException("SupplyItem not found wityh id: " + id));
		Procurement procurement = procurementRepository.findById(request.procurementId()).orElseThrow(
				() -> new ProcurementNotFoundException("Procurement not found wiht id: " + request.procurementId()));
		Vendor vendor = vendorRepository.findById(request.vendorId())
				.orElseThrow(() -> new SupplierNotFoundException("Vendor not found with id: " + request.vendorId()));
		supplyItem.setProcurement(procurement);
		supplyItem.setSupplier(vendor);
		supplyItem.setCost(request.cost());
		SupplyItem saved = supplyItemRepository.save(supplyItem);
		return supplyItemMapper.toResponse(saved);
	}

	@Transactional
	@Override
	public void deleteSupplyItem(Long id) {
		if (!supplyItemRepository.existsById(id)) {
			throw new SupplyItemNotFoundException("Supply-Item not found with id: " + id);
		}
		supplyItemRepository.deleteById(id);
	}

	@Override
	public SupplyItemResponse getOneById(Long supplyItemId) {
		SupplyItem response = supplyItemRepository.findById(supplyItemId)
				.orElseThrow(() -> new SupplyItemNotFoundException("SupplyItem not found with id: " + supplyItemId));
		return supplyItemMapper.toResponse(response);
	}

	@Override
	public List<SupplyItemResponse> getAllSupplyItem() {
		return supplyItemRepository.findAll().stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getByProcurementId(Long procurementId) {
		return supplyItemRepository.findByProcurementId(procurementId).stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getBySupplierId(Long supplierId) {
		return supplyItemRepository.findBySupplierId(supplierId).stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getByCostBetween(BigDecimal min, BigDecimal max) {
		return supplyItemRepository.findByCostBetween(min, max).stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getByProcurementDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
		return supplyItemRepository.findByProcurementDateBetween(startDate, endDate).stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getByProcurementDateAndCostBetween(LocalDateTime startDate, LocalDateTime endDate,
			BigDecimal min, BigDecimal max) {
		return supplyItemRepository.findByProcurementDateAndCostBetween(startDate, endDate, min, max).stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getByProcurementAndVendor(Long procurementId, Long vendorId) {
		return supplyItemRepository.findByProcurementAndVendor(procurementId, vendorId).stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getByVendorAndProcurementAndCost(Long supplierId, Long procurementId,
			BigDecimal minCost) {
		return supplyItemRepository.findByVendorAndProcurementAndCost(supplierId, procurementId, minCost).stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getByDateAndCost(LocalDateTime startDate, LocalDateTime endDate, BigDecimal min,
			BigDecimal max) {
		return supplyItemRepository.findByDateAndCost(startDate, endDate, min, max).stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getBySupplierNameAndProcurementDateAndMaxCost(String supplierName,
			LocalDateTime startDate, LocalDateTime endDate, BigDecimal maxCost) {
		return supplyItemRepository
				.findBySupplierNameAndProcurementDateAndMaxCost(supplierName, startDate, endDate, maxCost).stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}
	
	//nove metode

	@Override
	public List<SupplyItemResponse> findBySupplier_NameContainingIgnoreCase(String supplierName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findBySupplier_PhoneNumberLikeIgnoreCase(String phoneNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findBySupplier_EmailLikeIgnoreCase(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findBySupplier_Address(String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findBySupplierNameContainingAndCostBetween(String supplierName, BigDecimal minCost,
			BigDecimal maxCost) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findBySupplierNameAndProcurementDateBetween(String supplierName,
			LocalDateTime start, LocalDateTime end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findByAddressAndMinCost(String address, BigDecimal minCost) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findByPhoneNumberAndCost(String phoneNumber, BigDecimal cost) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findByProcurementSupplyItemCount(Integer count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findByCost(BigDecimal cost) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findByCostGreaterThan(BigDecimal cost) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findByCostLessThan(BigDecimal cost) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findByProcurementTotalCostGreaterThan(BigDecimal minCost) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findByProcurementDate(LocalDateTime date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findBySupplyAndSalesCountMismatch() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SupplyItemStatsResponse countAllSupplyItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SupplyItemStatsResponse countByProcurementId(Long procurementId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SupplyItemStatsResponse sumAllCosts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SupplyItemStatsResponse averageCost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SupplyItemStatsResponse minCost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SupplyItemStatsResponse maxCost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SumCostGroupedByProcurementResponse> sumCostGroupedByProcurement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplierItemCountResponse> countBySupplier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AvgCostByProcurementResponse> avgCostByProcurement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcurementGlobalStatsResponse procurementStats() {
	    Object[] result = supplyItemRepository.procurementStats();
	    return new ProcurementGlobalStatsResponse(
	        ((Number) result[0]).longValue(),
	        (BigDecimal) result[1],
	        (BigDecimal) result[2],
	        (BigDecimal) result[3],
	        (BigDecimal) result[4]
	    );
	}

	@Override
	public List<ProcurementStatsPerEntityResponse> procurementPerEntityStats() {
	    List<Object[]> results = supplyItemRepository.procurementPerEntityStats();
	    return results.stream().map(obj -> new ProcurementStatsPerEntityResponse(
	        ((Number) obj[0]).longValue(),
	        ((Number) obj[1]).longValue(),
	        (BigDecimal) obj[2],
	        (BigDecimal) obj[3],
	        (BigDecimal) obj[4],
	        (BigDecimal) obj[5]
	    )).collect(Collectors.toList());
	}

	@Override
	public List<CostSumByProcurement> findCostSumGroupedByProcurement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findByProcurementWithSupplyCostOver(BigDecimal minTotal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SupplyItemResponse> findBySupplierWithMoreThanNItems(Long minCount) {
		// TODO Auto-generated method stub
		return null;
	}
}
