package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.GoodsNotFoundException;
import com.jovan.erp_v1.exception.ItemSalesNotFoundException;
import com.jovan.erp_v1.exception.ProcurementNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.exception.SalesOrderNotFoundException;
import com.jovan.erp_v1.mapper.SalesMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.ItemSalesRepository;
import com.jovan.erp_v1.repository.SalesRepository;
import com.jovan.erp_v1.repository.specification.SalesSpecifications;
import com.jovan.erp_v1.request.ItemSalesRequest;
import com.jovan.erp_v1.request.SalesRequest;
import com.jovan.erp_v1.response.SalesResponse;
import com.jovan.erp_v1.util.DateValidator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesService implements ISalesService {

	private final SalesRepository salesRepository;
	private final SalesMapper salesMapper;
	private final BuyerRepository buyerRepository;
	private final ItemSalesRepository itemSalesRepository;
	private final GoodsRepository goodsRepository;

	@Transactional
	@Override
	public SalesResponse createSales(SalesRequest request) {
		Sales sales = new Sales();
		Buyer buyer = fetchBuyerId(request.buyerId());
		validateUpdateAndCreateMethod(request);
		sales.setBuyer(buyer);
		updateAndCreateSales(sales, request);
		sales.setCreatedAt(request.createdAt());
		sales.setTotalPrice(request.totalPrice());
		sales.setSalesDescription(request.salesDescription());
		return salesMapper.toResponse(sales);
	}

	@Transactional
	@Override
	public SalesResponse updateSales(Long id, SalesRequest request) {
		Sales sales = salesRepository.findById(id)
				.orElseThrow(() -> new SalesNotFoundException("Sales not found with id: " + id));
		Buyer buyer = fetchBuyerId(request.buyerId());
		validateUpdateAndCreateMethod(request);
		sales.setBuyer(buyer);
		updateAndCreateSales(sales, request);
		sales.setCreatedAt(request.createdAt());
		sales.setTotalPrice(request.totalPrice());
		sales.setSalesDescription(request.salesDescription());
		return salesMapper.toResponse(sales);
	}

	@Transactional
	@Override
	public void deleteSales(Long id) {
		if (!salesRepository.existsById(id)) {
			throw new SalesNotFoundException("Sales not found with id: " + id);
		}
		salesRepository.deleteById(id);

	}

	@Override
	public List<SalesResponse> getByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		return salesRepository.findByCreatedAtBetween(start, end).stream()
				.map(salesMapper::toResponse)
				.collect(Collectors.toList());
	}

	/*@Override
	public List<SalesResponse> getByBuyer(Buyer buyer) {
		return salesRepository.findByBuyer(buyer).stream()
				.map(salesMapper::toResponse)
				.collect(Collectors.toList());
	}*/

	@Override
	public List<SalesResponse> getByTotalPrice(BigDecimal totalPrice) {
		validateBigDecimal(totalPrice);
		return salesRepository.findByTotalPrice(totalPrice).stream()
				.map(salesMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public SalesResponse getBySalesId(Long id) {
		Sales sales = salesRepository.findById(id)
				.orElseThrow(() -> new SalesNotFoundException("Sales not found with id: " + id));
		return salesMapper.toResponse(sales);
	}

	private void updateAndCreateSales(Sales sales, SalesRequest request) {
		if (request.itemSales() != null) {
			List<ItemSales> itemSalesList = request.itemSales().stream()
					.map((ItemSalesRequest itemRequest) -> {
						ItemSales item = new ItemSales();
						Goods goods = goodsRepository.findById(itemRequest.goodsId())
								.orElseThrow(() -> new RuntimeException(
										"Goods not found with id: " + itemRequest.goodsId()));
						item.setGoods(goods);
						item.setSales(sales);

						item.setQuantity(itemRequest.quantity());
						item.setUnitPrice(itemRequest.unitPrice());
						// (opciono dodavanje procurement i salesOrder)
						return item;
					})
					.collect(Collectors.toList());
			sales.setItemSales(itemSalesList);
		}
	}

	@Override
	public List<SalesResponse> getSalesByDate(LocalDate date) {
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
		List<Sales> sales = salesRepository.findByCreatedAtBetween(startOfDay, endOfDay);
		return salesMapper.toResponseList(sales);
	}

	@Override
	public List<SalesResponse> getAllSales() {
		return salesRepository.findAll().stream()
				.map(salesMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByBuyer_Id(Long buyerId) {
		fetchBuyerId(buyerId);
		return salesRepository.findByBuyer_Id(buyerId).stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByBuyer_CompanyNameContainingIgnoreCase(String buyerCompanyName) {
		validateString(buyerCompanyName);
		return salesRepository.findByBuyer_CompanyNameContainingIgnoreCase(buyerCompanyName).stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByBuyer_PibContainingIgnoreCase(String buyerPib) {
		if(!salesRepository.existsByBuyer_Pib(buyerPib)) {
			throw new BuyerNotFoundException("Buyer PIB not found "+buyerPib);
		}
		return salesRepository.findByBuyer_PibContainingIgnoreCase(buyerPib).stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByBuyer_AddressContainingIgnoreCase(String buyerAddress) {
		validateString(buyerAddress);
		return salesRepository.findByBuyer_AddressContainingIgnoreCase(buyerAddress).stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByBuyer_ContactPerson(String contactPerson) {
		validateString(contactPerson);
		return salesRepository.findByBuyer_ContactPerson(contactPerson).stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByBuyer_EmailContainingIgnoreCase(String buyerEmail) {
		validateString(buyerEmail);
		return salesRepository.findByBuyer_EmailContainingIgnoreCase(buyerEmail).stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByBuyer_PhoneNumber(String buyerPhoneNumber) {
		validateString(buyerPhoneNumber);
		return salesRepository.findByBuyer_PhoneNumber(buyerPhoneNumber).stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByTotalPriceGreaterThan(BigDecimal totalPrice) {
		validateBigDecimal(totalPrice);
		return salesRepository.findByTotalPriceGreaterThan(totalPrice).stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByTotalPriceLessThan(BigDecimal totalPrice) {
		validateBigDecimal(totalPrice);
		return salesRepository.findByTotalPriceLessThan(totalPrice).stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<SalesResponse> searchSales(
			Long buyerId,
		    String companyName,
		    String pib,
		    String email,
		    String phoneNumber,
		    String address,
		    String contactPerson,
		    BigDecimal minTotalPrice,
		    BigDecimal maxTotalPrice){
		Specification<Sales> spec = Specification.where(SalesSpecifications.hasBuyerId(buyerId)
				.and(SalesSpecifications.hasCompanyName(companyName))
				.and(SalesSpecifications.hasPib(pib))
				.and(SalesSpecifications.hasEmail(email))
				.and(SalesSpecifications.hasAddress(address))
				.and(SalesSpecifications.hasContactPerson(contactPerson))
				.and(SalesSpecifications.hasPhoneNumber(phoneNumber))
		        .and(SalesSpecifications.minTotalPrice(minTotalPrice))
		        .and(SalesSpecifications.maxTotalPrice(maxTotalPrice)));
		List<Sales> sales = salesRepository.findAll(spec);
		return sales.stream().map(salesMapper::toResponse).collect(Collectors.toList());
	}
	
	private Buyer fetchBuyerId(Long buyerId) {
		if(buyerId == null) {
			throw new BuyerNotFoundException("Buyer ID must not be null");
		}
		return buyerRepository.findById(buyerId).orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id "+buyerId));
	}
	
	private void validateString(String str) {
		if(str == null  || str.trim().isEmpty()) {
			throw new IllegalArgumentException("String must not be null nor empty");
		}
	}
	
	private void validateBigDecimal(BigDecimal num) {
		if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Number must be positive");
		}
	}
	
	private void validateItemSalesRequest(List<ItemSalesRequest> items) {
		if(items == null || items.isEmpty()) {
			throw new ItemSalesNotFoundException("ItemSalesRequest must not be null");
		}
		for(ItemSalesRequest item : items) {
			validateItemSalesRequest(item);
		}
	}
	
	private void validateItemSalesRequest(ItemSalesRequest item) {
		if(item.goodsId() == null) {
			throw new GoodsNotFoundException("Goods ID must not be null");
		}
		if(item.procurementId() == null) {
			throw new ProcurementNotFoundException("Procurement ID must not be null");
		}
		if(item.salesOrderId() == null) {
			throw new SalesOrderNotFoundException("SalesOrder ID must not be null");
		}
		validateBigDecimal(item.quantity());
		validateBigDecimal(item.unitPrice());
	}
	
	private void validateUpdateAndCreateMethod(SalesRequest request) {
		validateItemSalesRequest(request.itemSales());
		DateValidator.validatePastOrPresent(request.createdAt(), "Created at");
		validateBigDecimal(request.totalPrice());
		validateString(request.salesDescription());
	}

}
