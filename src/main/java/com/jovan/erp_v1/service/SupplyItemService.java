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
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ProcurementNotFoundException;
import com.jovan.erp_v1.exception.SupplierNotFoundException;
import com.jovan.erp_v1.exception.SupplyItemNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.SupplyItemMapper;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.model.SupplyItem;
import com.jovan.erp_v1.model.Vendor;
import com.jovan.erp_v1.repository.ProcurementRepository;
import com.jovan.erp_v1.repository.SupplyItemRepository;
import com.jovan.erp_v1.repository.VendorRepository;
import com.jovan.erp_v1.request.AvgCostByProcurementRecord;
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
	    if (!supplyItemRepository.existsSupplyItems()) {
	        throw new NoDataFoundException("No supply items in database.");
	    }
	    Long count = supplyItemRepository.countAllSupplyItems();
	    if (count == null) {
	        count = 0L;
	    }
	    return SupplyItemStatsResponse.ofCount(count);
	}

	@Override
	public SupplyItemStatsResponse countByProcurementId(Long procurementId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SupplyItemStatsResponse sumAllCosts() {
	    if (!supplyItemRepository.existsSupplyItems()) {
	        throw new NoDataFoundException("No supply items in database.");
	    }
	    BigDecimal sum = supplyItemRepository.sumAllCosts();
	    if(sum == null) {
	    	sum = BigDecimal.ZERO;
	    }
	    return SupplyItemStatsResponse.ofSum(sum);
	}

	@Override
	public SupplyItemStatsResponse averageCost() {
	    if (!supplyItemRepository.existsSupplyItems()) {
	        throw new NoDataFoundException("No supply items in database.");
	    }
	    BigDecimal average = supplyItemRepository.averageCost();
	    if (average == null) {
	        average = BigDecimal.ZERO;
	    }
	    return SupplyItemStatsResponse.ofAvg(average);
	}

	@Override
	public SupplyItemStatsResponse minCost() {
		if (!supplyItemRepository.existsSupplyMinItems()) {
		    throw new NoDataFoundException("No supply items in database.");
		}
		BigDecimal total = supplyItemRepository.minCost();
		return SupplyItemStatsResponse.builder()
				.min(total != null ? total : BigDecimal.ZERO)
				.build();
	}

	@Override
	public SupplyItemStatsResponse maxCost() {
		if (!supplyItemRepository.existsSupplyMaxItems()) {
		    throw new NoDataFoundException("No supply items in database.");
		}
		BigDecimal total = supplyItemRepository.maxCost();
		return SupplyItemStatsResponse.builder()
			    .max(total != null ? total : BigDecimal.ZERO)
			    .build();
	}
	
	@Override
	public List<SumCostGroupedByProcurementResponse> sumCostGroupedByProcurement() {
		List<Object[]> items = supplyItemRepository.sumCostGroupedByProcurement();
		return items.stream()
				.map(row -> new SumCostGroupedByProcurementResponse((Long)row[0], (BigDecimal)row[1]))
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplierItemCountResponse> countBySupplier() {
		List<Object[]> items = supplyItemRepository.countBySupplier();
		return items.stream()
				.map(row -> new SupplierItemCountResponse((Long)row[0], (Long)row[1]))
				.collect(Collectors.toList());
	}

	@Override
	public List<AvgCostByProcurementResponse> avgCostByProcurement() {
		List<AvgCostByProcurementRecord> items = supplyItemRepository.avgCostByProcurement();
		return items.stream()
				.filter(item -> item.avgCost().compareTo(BigDecimal.valueOf(100)) > 0)
				.map(item -> new AvgCostByProcurementResponse(item.procurementId(), item.avgCost()))
				.collect(Collectors.toList());
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
		if(!supplyItemRepository.existsCostSumGroupedByProcurement()) {
			throw new NoDataFoundException("Total cost summed for Procurement not found");
		}
		List<CostSumByProcurement> items = supplyItemRepository.findCostSumGroupedByProcurement();
		return items.stream()
				.filter(item -> item.totalCost().compareTo(BigDecimal.valueOf(100)) > 0)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<CostSumByProcurement> findCostSumGroupedByProcurementWithMinTotal(BigDecimal minTotal) {
		if (!supplyItemRepository.existsCostSumGroupedByProcurement()) {
			throw new NoDataFoundException("Total cost summed for Procurement not found");
		}
		return supplyItemRepository.findCostSumGroupedByProcurementWithMinTotal(minTotal);
	}

	@Override
	public List<SupplyItemResponse> findByProcurementWithSupplyCostOver(BigDecimal minTotal) {
		validateBigDecimalNonNegative(minTotal);
		if(!supplyItemRepository.existsProcurementWithSupplyCostOver(minTotal)) {
			String msg = String.format("Procurement with supply cost over not found %s", minTotal);
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findByProcurementWithSupplyCostOver(minTotal);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> findBySupplierWithMoreThanNItems(Long minCount) {
		validateLong(minCount);
		if(!supplyItemRepository.existsSupplierWithMoreThanNItems(minCount)) {
			String msg = String.format("Supplier with more than n items not found %d", minCount);
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findBySupplierWithMoreThanNItems(minCount);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}
	
	private void validateLong(Long num) {
		if(num == null || num < 0) {
			throw new ValidationException("Minimum count must be zero or a positive number");
		}
	}
	
	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
	}
	
	private void createSupplyItemRequest(SupplyItemRequest request) {
		fetchProcurementId(request.procurementId());
		fetchVendorId(request.vendorId());
		validateBigDecimal(request.cost());
	}
	
	private Vendor fetchVendorId(Long vendorId) {
		if(vendorId == null) {
			throw new ValidationException("Vendor ID must not be null");
		}
		return vendorRepository.findById(vendorId).orElseThrow(() -> new ValidationException("Vendor not found with id "+vendorId));
	}
	
	private Procurement fetchProcurementId(Long procurementId) {
		if(procurementId == null) {
			throw new ValidationException("Procurement ID must not be null");
		}
		return procurementRepository.findById(procurementId).orElseThrow(() -> new ProcurementNotFoundException("Procurement not found with id "+procurementId));
	}
	
	private void validateBigDecimal(BigDecimal num) {
		if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ValidationException("Number must be positive");
		}
	}

	
}
