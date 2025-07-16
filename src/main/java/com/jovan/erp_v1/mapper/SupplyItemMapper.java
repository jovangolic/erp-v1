package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.model.SupplyItem;
import com.jovan.erp_v1.model.Vendor;
import com.jovan.erp_v1.request.SupplyItemRequest;
import com.jovan.erp_v1.response.SupplyItemResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class SupplyItemMapper extends AbstractMapper<SupplyItemRequest> {
	
	public SupplyItem toEntity(SupplyItem item, SupplyItemRequest request,Procurement procurement, Vendor vendor) {
		Objects.requireNonNull(request, "SupplyItemRequest must not be null");
		validateIdForCreate(request, SupplyItemRequest::id);
		return applyRequestData(item, request, procurement, vendor);
	}
	
	public SupplyItem toUpdateEntity(SupplyItem item, SupplyItemRequest request,Procurement procurement, Vendor vendor) {
		Objects.requireNonNull(item, "SupplyItem must not be null");
		Objects.requireNonNull(request, "SupplyItemRequest must not be null");
		validateIdForUpdate(request, SupplyItemRequest::id);
		return applyRequestData(item, request, procurement, vendor);
	}
	
	private SupplyItem applyRequestData(SupplyItem item, SupplyItemRequest request, Procurement procurement, Vendor vendor) {
	    item.setProcurement(procurement);
	    item.setSupplier(vendor);
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
	
}
