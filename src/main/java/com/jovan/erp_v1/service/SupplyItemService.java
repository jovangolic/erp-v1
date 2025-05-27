package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
}
