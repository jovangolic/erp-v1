package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.ProcurementNotFoundException;

import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.model.SupplyItem;
import com.jovan.erp_v1.model.Vendor;
import com.jovan.erp_v1.repository.ProcurementRepository;
import com.jovan.erp_v1.repository.VendorRepository;
import com.jovan.erp_v1.request.SupplyItemRequest;
import com.jovan.erp_v1.response.SupplyItemResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SupplyItemMapper extends AbstractMapper<SupplyItemRequest> {

	private final ProcurementRepository procurementRepository;
	private final VendorRepository vendorRepository;
	
	public SupplyItem toEntity(SupplyItemRequest request) {
		Objects.requireNonNull(request, "SupplyItemRequest must not be null");
		validateIdForCreate(request, SupplyItemRequest::id);
		return buildSupplyItemFromRequest(new SupplyItem(), request);
	}
	
	public SupplyItem toUpdateEntity(SupplyItem item, SupplyItemRequest request) {
		Objects.requireNonNull(item, "SupplyItem must not be null");
		Objects.requireNonNull(request, "SupplyItemRequest must not be null");
		validateIdForUpdate(request, SupplyItemRequest::id);
		return buildSupplyItemFromRequest(item, request);
	}
	
	private SupplyItem buildSupplyItemFromRequest(SupplyItem item, SupplyItemRequest request) {
		item.setProcurement(fetchProcurementId(request.procurementId()));
		item.setSupplier(fetchVendorId(request.vendorId()));
		item.setCost(request.cost());
		return item;
	}
	
	public SupplyItemResponse toResponse(SupplyItem supplyItem) {
		Objects.requireNonNull(supplyItem, "SupplyItem must not be null");
		return new SupplyItemResponse(supplyItem);
	}
	
	public List<SupplyItemResponse> toResponseList(List<SupplyItem> items){
		if(items == null || items.isEmpty()) {
			return Collections.emptyList();
		}
		return items.stream().map(this::toResponse).toList();
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
}
