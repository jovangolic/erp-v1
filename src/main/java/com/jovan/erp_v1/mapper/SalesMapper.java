package com.jovan.erp_v1.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.request.SalesRequest;
import com.jovan.erp_v1.response.SalesResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class SalesMapper extends AbstractMapper<SalesRequest> {
	
	public Sales toEntity(SalesRequest request, Buyer buyer) {
		Objects.requireNonNull(request, "SalesRequest must not be null");
		Objects.requireNonNull(buyer, "Buyer must not be null");
		validateIdForCreate(request, SalesRequest::id);
		Sales sales = new Sales();
		sales.setId(request.id());
		sales.setBuyer(buyer);
		sales.setItemSales(new ArrayList<>());
		sales.setTotalPrice(request.totalPrice());
		sales.setSalesDescription(request.salesDescription());
		return sales;
	}
	
	public Sales toEntityUpdate(Sales sales, SalesRequest request, Buyer buyer) {
		Objects.requireNonNull(sales, "Sales must not be null");
		Objects.requireNonNull(request, "SalesRequest must not be null");
		Objects.requireNonNull(buyer, "Buyer must not be null");
		sales.setBuyer(buyer);
		sales.setItemSales(new ArrayList<>());
		sales.setTotalPrice(request.totalPrice());
		sales.setSalesDescription(request.salesDescription());
		return sales;
	}
	
	/*public SalesResponse toResponse(Sales sales) {
		Objects.requireNonNull(sales, "Sales must not be null");
		SalesResponse response = new SalesResponse();
		response.setId(sales.getId());
		
		if(sales.getBuyer() != null) {
			response.setBuyerCompanyName(sales.getBuyer().getCompanyName());
		}
		response.setCreatedAt(sales.getCreatedAt());
		response.setTotalPrice(sales.getTotalPrice());
		if(sales.getItemSales() != null) {
			List<ItemSalesResponse> itemSales = sales.getItemSales().stream()
					.map(itemSalesMapper::toResponse)
					.collect(Collectors.toList());
			response.setItemSales(itemSales);
		}
		response.setSalesDescription(sales.getSalesDescription());
		return response;
	}*/
	
	public SalesResponse toResponse(Sales sales) {
		Objects.requireNonNull(sales, "Sales must not be null");
		return new SalesResponse(sales);
	}
	
	public List<SalesResponse> toResponseList(List<Sales> sales){
		if(sales == null || sales.isEmpty()) {
			return Collections.emptyList();
		}
		return sales.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
	
}
