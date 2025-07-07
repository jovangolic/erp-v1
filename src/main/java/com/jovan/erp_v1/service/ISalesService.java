package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.request.SalesRequest;
import com.jovan.erp_v1.response.SalesResponse;

public interface ISalesService {

	SalesResponse createSales(SalesRequest request);

	SalesResponse updateSales(Long id, SalesRequest request);

	void deleteSales(Long id);

	List<SalesResponse> getByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

	//List<SalesResponse> getByBuyer(Buyer buyer);

	List<SalesResponse> getByTotalPrice(BigDecimal totalPrice);

	SalesResponse getBySalesId(Long id);

	List<SalesResponse> getSalesByDate(LocalDate date);

	List<SalesResponse> getAllSales();
	//nove metode
	List<SalesResponse> findByBuyer_Id(Long buyerId);
	List<SalesResponse> findByBuyer_CompanyNameContainingIgnoreCase(String buyerCompanyName);
	List<SalesResponse> findByBuyer_PibContainingIgnoreCase(String buyerPib);
	List<SalesResponse> findByBuyer_AddressContainingIgnoreCase(String buyerAddress);
	List<SalesResponse> findByBuyer_ContactPerson(String contactPerson);
	List<SalesResponse> findByBuyer_EmailContainingIgnoreCase(String buyerEmail);
	List<SalesResponse> findByBuyer_PhoneNumber(String buyerPhoneNumber);
	List<SalesResponse> findByTotalPriceGreaterThan(BigDecimal totalPrice);
	List<SalesResponse> findByTotalPriceLessThan(BigDecimal totalPrice);
}
