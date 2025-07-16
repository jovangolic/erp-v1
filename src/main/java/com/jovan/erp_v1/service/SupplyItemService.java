package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.jovan.erp_v1.util.DateValidator;

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
		Procurement procurement = fetchProcurementId(request.procurementId());
		Vendor vendor = fetchVendorId(request.vendorId());
		validateBigDecimal(request.cost());
		/*if (request.id() != null) {
	        throw new ValidationException("ID must be null when creating a new SupplyItem");
	    }*/
		SupplyItem supplyItem = supplyItemMapper.toEntity(new SupplyItem(), request, procurement, vendor);
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
		Procurement procurement = fetchProcurementId(request.procurementId());
		Vendor vendor = fetchVendorId(request.vendorId());
		validateBigDecimal(request.cost());
		supplyItemMapper.toUpdateEntity(supplyItem, request, procurement, vendor);
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
		List<SupplyItem> items = supplyItemRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("No data for supply items found");
		}
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getByProcurementId(Long procurementId) {
		fetchProcurementId(procurementId);
		List<SupplyItem> items = supplyItemRepository.findByProcurementId(procurementId);
		if(items.isEmpty()) {
			String msg = String.format("No data for procurement found equal to %d", procurementId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getBySupplierId(Long supplierId) {
		fetchVendorId(supplierId);
		List<SupplyItem> items = supplyItemRepository.findBySupplierId(supplierId);
		if(items.isEmpty()) {
			String msg = String.format("No data for supplier found equal to %d", supplierId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getByCostBetween(BigDecimal min, BigDecimal max) {
		validateBigDecimalNonNegative(min);
		validateBigDecimal(max);
		List<SupplyItem> items = supplyItemRepository.findByCostBetween(min, max);
		if(items.isEmpty()) {
			String msg = String.format("No data for supply items cost between %s and %s found",
					min,max);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getByProcurementDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
		DateValidator.validateRange(startDate, endDate);
		List<SupplyItem> items = supplyItemRepository.findByProcurementDateBetween(startDate, endDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss");
			String msg = String.format("On data for procurement date between %s and %s found",
					startDate.format(formatter),endDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getByProcurementDateAndCostBetween(LocalDateTime startDate, LocalDateTime endDate,
			BigDecimal min, BigDecimal max) {
		DateValidator.validateRange(startDate, endDate);
		validateBigDecimalNonNegative(min);
		validateBigDecimal(max);
		List<SupplyItem> items = supplyItemRepository.findByProcurementDateAndCostBetween(startDate, endDate, min, max);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No dataa for procurement date range and cost range found");
		}
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getByProcurementAndVendor(Long procurementId, Long vendorId) {
		fetchProcurementId(procurementId);
		fetchVendorId(vendorId);
		List<SupplyItem> items = supplyItemRepository.findByProcurementAndVendor(procurementId, vendorId);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No data for prcurement and vendor found");
		}
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getByVendorAndProcurementAndCost(Long supplierId, Long procurementId,
			BigDecimal minCost) {
		validateBigDecimal(minCost);
		fetchProcurementId(procurementId);
		fetchVendorId(procurementId);
		List<SupplyItem> items = supplyItemRepository.findByVendorAndProcurementAndCost(supplierId, procurementId, minCost);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No data for vendor, procurement and cost found");
		}
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getByDateAndCost(LocalDateTime startDate, LocalDateTime endDate, BigDecimal min,
			BigDecimal max) {
		DateValidator.validateRange(startDate, endDate);
		validateBigDecimalNonNegative(min);
		validateBigDecimal(max);
		if(!supplyItemRepository.existsByDateAndCost(startDate, endDate, min, max)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss");
			String msg = String.format("Supplier with date between %s and %s and cost between %s and %s not found",
					startDate.format(formatter),endDate.format(formatter),min,max);
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findByDateAndCost(startDate, endDate, min, max);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> getBySupplierNameAndProcurementDateAndMaxCost(String supplierName,
			LocalDateTime startDate, LocalDateTime endDate, BigDecimal maxCost) {
		validateString(supplierName);
		DateValidator.validateRange(startDate, endDate);
		validateBigDecimal(maxCost);
		if(!supplyItemRepository.existsBySupplierNameAndProcurementDateAndMaxCost(supplierName, startDate, endDate, maxCost)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss");
			String msg = String.format("Supplier with name %s and procurement date between %s and %s and max-cost %s not found",
					supplierName,startDate.format(formatter),endDate.format(formatter),maxCost);
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findBySupplierNameAndProcurementDateAndMaxCost(supplierName, startDate, endDate, maxCost);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}
	
	//nove metode

	@Override
	public List<SupplyItemResponse> findBySupplier_NameContainingIgnoreCase(String supplierName) {
		validateString(supplierName);
		if(!supplyItemRepository.existsBySupplier_NameContainingIgnoreCase(supplierName)) {
			String msg = String.format("Supplier name equal to %s not found", supplierName);
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findBySupplier_NameContainingIgnoreCase(supplierName);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> findBySupplier_PhoneNumberLikeIgnoreCase(String phoneNumber) {
		validateString(phoneNumber);
		if(!supplyItemRepository.existsBySupplier_PhoneNumberLikeIgnoreCase(phoneNumber)) {
			String msg = String.format("Supplier with phone-number not found %s", phoneNumber);
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findBySupplier_PhoneNumberLikeIgnoreCase(phoneNumber);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> findBySupplier_EmailLikeIgnoreCase(String email) {
		validateString(email);
		if(!supplyItemRepository.existsBySupplier_EmailLikeIgnoreCase(email)) {
			String msg = String.format("Supplier with email not found %s", email);
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findBySupplier_Address(email);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> findBySupplier_Address(String address) {
		validateString(address);
		if(!supplyItemRepository.existsBySupplier_Address(address)) {
			String msg = String.format("Supplier with address equal to not found %s", address);
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findBySupplier_Address(address);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> findBySupplierNameContainingAndCostBetween(String supplierName, BigDecimal minCost,
			BigDecimal maxCost) {
		validateString(supplierName);
		validateBigDecimalNonNegative(minCost);
		validateBigDecimal(maxCost);
		if(!supplyItemRepository.existsBySupplierNameContainingAndCostBetween(supplierName, minCost, maxCost)) {
			String msg = String.format("No supply items with name %s and cost between %s and %s found",
					supplierName,minCost,maxCost);
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findBySupplierNameContainingAndCostBetween(supplierName, minCost, maxCost);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> findBySupplierNameAndProcurementDateBetween(String supplierName,
			LocalDateTime start, LocalDateTime end) {
		validateString(supplierName);
		DateValidator.validateRange(start, end);
		if(!supplyItemRepository.existsBySupplierNameAndProcurementDateBetween(supplierName, start, end)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss");
			String msg = String.format("No supply items with name %s and procurement date between %s and %s found",
					supplierName,start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findBySupplierNameAndProcurementDateBetween(supplierName, start, end);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> findByAddressAndMinCost(String address, BigDecimal minCost) {
		validateString(address);
		validateBigDecimalNonNegative(minCost);
		if(!supplyItemRepository.existsByAddressAndMinCost(address, minCost)) {
			String msg = String.format("Supply items not found with address %s and min-cost %s",
					address, minCost);
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findByAddressAndMinCost(address, minCost);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> findByPhoneNumberAndCost(String phoneNumber, BigDecimal cost) {
		validateBigDecimalNonNegative(cost);
		validateString(phoneNumber);
		if(!supplyItemRepository.existsByPhoneNumberAndCost(phoneNumber, cost)) {
			String msg = String.format("Supply items with phone number %s and cost %s not found",
					phoneNumber,cost);
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findByPhoneNumberAndCost(phoneNumber, cost);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> findByProcurementSupplyItemCount(Integer count) {
		validateInteger(count);
		if(!supplyItemRepository.existsByProcurementSupplyItemCount(count)) {
			String msg = String.format("No procurement for supply items count found %d", count);
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findByProcurementSupplyItemCount(count);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> findByCost(BigDecimal cost) {
		validateBigDecimalNonNegative(cost);
		if(!supplyItemRepository.existsByCost(cost)) {
			String msg = String.format("No supply items found wuth cost equal to %s", cost);
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findByCost(cost);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> findByCostGreaterThan(BigDecimal cost) {
		validateBigDecimal(cost);
		if(!supplyItemRepository.existsByCostGreaterThan(cost)) {
			String msg = String.format("No supply items found with cost greater than %s", cost);
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findByCostGreaterThan(cost);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> findByCostLessThan(BigDecimal cost) {
		validateBigDecimalNonNegative(cost);
		if(!supplyItemRepository.existsByCostLessThan(cost)) {
			String msg = String.format("No supply items found with cost less than %s", cost);
			throw new NoDataFoundException(msg);
		}
		List<SupplyItem> items = supplyItemRepository.findByCostLessThan(cost);
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> findByProcurementTotalCostGreaterThan(BigDecimal minCost) {
		validateBigDecimal(minCost);
		List<SupplyItem> items = supplyItemRepository.findByProcurement_TotalCostGreaterThan(minCost);
		if(items.isEmpty()) {
			String msg = String.format("No procurement records found for total-cost greater than %s", minCost);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> findByProcurementDate(LocalDateTime date) {
		DateValidator.validateNotInFuture(date, "Procurement date");
		List<SupplyItem> items = supplyItemRepository.findByProcurementDate(date);
	    if (items.isEmpty()) {
	        String formatted = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm:ss"));
	        throw new NoDataFoundException("No procurement records found for date " + formatted);
	    }
	    return items.stream()
	                .map(supplyItemMapper::toResponse)
	                .collect(Collectors.toList());
	}

	@Override
	public List<SupplyItemResponse> findBySupplyAndSalesCountMismatch() {
		if(!supplyItemRepository.existsBySupplyAndSalesCountMismatch()) {
			throw new NoDataFoundException("No supply and sales for count mismatch found");
		}
		List<SupplyItem> items = supplyItemRepository.findBySupplyAndSalesCountMismatch();
		return items.stream()
				.map(supplyItemMapper::toResponse)
				.collect(Collectors.toList());
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
		if (!supplyItemRepository.existsByProcurementId(procurementId)) {
	        String msg = String.format("Procurement ID %d not found.", procurementId);
	        throw new NoDataFoundException(msg);
	    }
	    Long count = supplyItemRepository.countByProcurementId(procurementId);
	    if (count == null) {
	        count = 0L;
	    }
	    return SupplyItemStatsResponse.ofProcurIdAndCount(procurementId, count);
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
	public List<SupplyItemResponse> findBySupplierWithMoreThanNItems(BigDecimal minCount) {
		validateBigDecimalNonNegative(minCount);
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
	
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not be null nor empty");
		}
	}
	
	private void validateInteger(Integer num) {
		if(num == null || num < 0) {
			throw new ValidationException("Number must not be null nor negative");
		}
	}
	
	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
	
	private void validateSupplyItemRequest(SupplyItemRequest request) {
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
