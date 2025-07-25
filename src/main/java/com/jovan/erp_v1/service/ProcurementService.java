package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.ProcurementNotFoundException;
import com.jovan.erp_v1.mapper.ProcurementMapper;
import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.model.SupplyItem;
import com.jovan.erp_v1.repository.ItemSalesRepository;
import com.jovan.erp_v1.repository.ProcurementRepository;
import com.jovan.erp_v1.repository.SupplyItemRepository;
import com.jovan.erp_v1.request.ProcurementRequest;
import com.jovan.erp_v1.response.ProcurementResponse;
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcurementService implements IProcurementService {

	private final ProcurementRepository procurementRepository;
	private final ItemSalesRepository itemSalesRepository;
	private final SupplyItemRepository supplyItemRepository;
	private final ProcurementMapper procurementMapper;

	@Transactional
	@Override
	public ProcurementResponse createProcurement(ProcurementRequest request) {
		DateValidator.validatePastOrPresent(request.date(), "Procurement date");
		validateBigDecimal(request.totalCost());
		Procurement procurement = procurementMapper.toEntity(request);
		Procurement savedProcurement = procurementRepository.save(procurement);
		updateItemSalesAndSupplies(savedProcurement, request);
		Procurement updatedProcurement = procurementRepository.save(savedProcurement);
		return procurementMapper.toResponse(updatedProcurement);
	}

	@Transactional
	@Override
	public ProcurementResponse updateProcurement(Long id, ProcurementRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		Procurement existingProcurement = procurementRepository.findById(id)
				.orElseThrow(() -> new ProcurementNotFoundException("Procurement not found with id: " + id));
		DateValidator.validatePastOrPresent(request.date(), "Procurement date");
		validateBigDecimal(request.totalCost());
		existingProcurement.getItemSales().clear();
		existingProcurement.getSupplies().clear();
		updateItemSalesAndSupplies(existingProcurement, request);
		Procurement updatedProcurement = procurementRepository.save(existingProcurement);
		return procurementMapper.toResponse(updatedProcurement);
	}

	@Transactional
	@Override
	public void deleteProcurement(Long id) {
		if (!procurementRepository.existsById(id)) {
			throw new ProcurementNotFoundException("Procurement not found with id: " + id);
		}
		procurementRepository.deleteById(id);
	}

	@Override
	public ProcurementResponse getByProcurementId(Long id) {
		Procurement procurement = procurementRepository.findById(id)
				.orElseThrow(() -> new ProcurementNotFoundException("Procurement not found with id: " + id));
		return procurementMapper.toResponse(procurement);
	}

	@Override
	public List<ProcurementResponse> getAllProcurement() {
		return procurementRepository.findAll().stream()
				.map(procurementMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProcurementResponse> getByTotalCost(BigDecimal totalCost) {
		validateBigDecimal(totalCost);
		return procurementRepository.findByTotalCost(totalCost).stream()
				.map(procurementMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProcurementResponse> getByDateBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		return procurementRepository.findByDateBetween(start, end).stream()
				.map(procurementMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProcurementResponse> getByTotalCostBetween(BigDecimal min, BigDecimal max) {
		validateBigDecimal(max);
		validateBigDecimal(min);
		return procurementRepository.findByTotalCostBetween(min, max).stream()
				.map(procurementMapper::toResponse)
				.collect(Collectors.toList());
	}

	private void updateItemSalesAndSupplies(Procurement procurement, ProcurementRequest request) {
		// Postavljanje ItemSales relacija
		if (request.itemSalesIds() != null) {
			List<ItemSales> itemSalesList = itemSalesRepository.findAllById(request.itemSalesIds());
			if (itemSalesList.size() != request.itemSalesIds().size()) {
	            throw new IllegalArgumentException("Neki od itemSales ID-jeva nisu pronađeni.");
	        }
			itemSalesList.forEach(item -> item.setProcurement(procurement));
			procurement.setItemSales(itemSalesList);
		}

		// Postavljanje SupplyItem relacija
		if (request.supplyItemIds() != null) {
			List<SupplyItem> supplyItemList = supplyItemRepository.findAllById(request.supplyItemIds());
			if (supplyItemList.size() != request.supplyItemIds().size()) {
	            throw new IllegalArgumentException("Neki od supplyItem ID-jeva nisu pronađeni.");
	        }
			supplyItemList.forEach(supply -> supply.setProcurement(procurement));
			procurement.setSupplies(supplyItemList);
		}
	}

	@Override
	public List<ProcurementResponse> getByTotalCostGreaterThan(BigDecimal totalCost) {
		validateBigDecimal(totalCost);
		return procurementRepository.findByTotalCostGreaterThan(totalCost).stream()
				.map(ProcurementResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProcurementResponse> getByTotalCostLessThan(BigDecimal totalCost) {
		validateBigDecimal(totalCost);
		return procurementRepository.findByTotalCostLessThan(totalCost).stream()
				.map(ProcurementResponse::new)
				.collect(Collectors.toList());
	}
	
	private void validateBigDecimal(BigDecimal num) {
		if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Number must be positive");
		}
	}

}
