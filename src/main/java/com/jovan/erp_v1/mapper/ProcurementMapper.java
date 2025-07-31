package com.jovan.erp_v1.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.request.ProcurementRequest;
import com.jovan.erp_v1.response.ProcurementResponse;


@Component
public class ProcurementMapper {
	
	public Procurement toEntity(ProcurementRequest request) {
		Procurement procurement = new Procurement();
		procurement.setId(request.id());
		procurement.setTotalCost(request.totalCost());
		procurement.setItemSales(new ArrayList<>());
		procurement.setSupplies(new ArrayList<>());
		return procurement;
	}
	
	public ProcurementResponse toResponse(Procurement procurement) {
		Objects.requireNonNull(procurement, "Procurement must not be null");
	    return new ProcurementResponse(procurement);
	}

    public List<ProcurementResponse> toResponseList(List<Procurement> procurementList) {
    	if(procurementList == null || procurementList.isEmpty()) {
    		return Collections.emptyList();
    	}
        return procurementList.stream()
                .map(this::toResponse)
                .toList();
    }
}
