package com.jovan.erp_v1.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.ProcurementNotFoundException;
import com.jovan.erp_v1.exception.SupplierNotFoundException;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.model.SupplyItem;
import com.jovan.erp_v1.model.Vendor;
import com.jovan.erp_v1.repository.ProcurementRepository;
import com.jovan.erp_v1.repository.VendorRepository;
import com.jovan.erp_v1.request.SupplyItemRequest;
import com.jovan.erp_v1.response.SupplyItemResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SupplyItemMapper {

	private final ProcurementRepository procurementRepository;
	private final VendorRepository vendorRepository;
	
	public SupplyItem toEntity(SupplyItemRequest request) {
		SupplyItem supply = new SupplyItem();
		supply.setId(request.id());
		Procurement procurement = procurementRepository.findById(request.procurementId()).orElseThrow(() -> new ProcurementNotFoundException("Procurement not found"));
		supply.setProcurement(procurement);
		Vendor verdor = vendorRepository.findById(request.vendorId()).orElseThrow(() -> new SupplierNotFoundException("Vendor not found"));
		supply.setSupplier(verdor);
		supply.setCost(request.cost());
		return supply;
	}
	
	public SupplyItemResponse toResponse(SupplyItem supplyItem) {
		return new SupplyItemResponse(supplyItem);
	}
	
	public List<SupplyItemResponse> toResponseList(List<SupplyItem> items){
		return items.stream().map(this::toResponse).toList();
	}
}
