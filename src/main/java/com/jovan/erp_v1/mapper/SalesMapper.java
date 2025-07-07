package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.request.SalesRequest;
import com.jovan.erp_v1.response.ItemSalesResponse;
import com.jovan.erp_v1.response.SalesResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SalesMapper {

	private final BuyerRepository buyerRepository;
	private final ItemSalesMapper itemSalesMapper;
	
	public Sales toEntity(SalesRequest request) {
		Sales sales = new Sales();
		sales.setId(request.id());
		Buyer buyer = buyerRepository.findById(request.buyerId()).orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: " + request.buyerId()));
		if (request.buyerId() == null) {
			throw new IllegalArgumentException("buyerId is required");
		}
		sales.setBuyer(buyer);
		if(request.itemSales() != null) {
			List<ItemSales> itemSales = request.itemSales().stream()
					.map(itemSalesMapper::toEntity)
					.collect(Collectors.toList());
			itemSales.forEach(item -> item.setSales(sales)); // ovo je važno
			sales.setItemSales(itemSales);
		}
		sales.setCreatedAt(request.createdAt());
		sales.setTotalPrice(request.totalPrice());
		sales.setSalesDescription(request.salesDescription());
		return sales;
	}
	
	public SalesResponse toResponse(Sales sales) {
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
