package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.request.SalesRequest;
import com.jovan.erp_v1.response.SalesResponse;

public interface ISalesService {

	SalesResponse createSales(SalesRequest request);
	SalesResponse updateSales(Long id, SalesRequest request);
	void deleteSales(Long id);
	List<SalesResponse> getByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
	List<SalesResponse> getByBuyer(Buyer buyer);
	List<SalesResponse> getByTotalPrice(BigDecimal totalPrice);
	SalesResponse getBySalesId(Long id);
	List<SalesResponse> getSalesByDate(LocalDate date);
	
}
