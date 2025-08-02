package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.GoodsNotFoundException;
import com.jovan.erp_v1.exception.ItemSalesNotFoundException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ProcurementNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.exception.SalesOrderNotFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.SalesMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.GoodsRepository;
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
	private final GoodsRepository goodsRepository;

	@Transactional
	@Override
	public SalesResponse createSales(SalesRequest request) {
		Buyer buyer = fetchBuyerId(request.buyerId());
		validateUpdateAndCreateMethod(request);
		Sales sales = salesMapper.toEntity(request, buyer);
		updateAndCreateSales(sales, request);
		return salesMapper.toResponse(sales);
	}

	@Transactional
	@Override
	public SalesResponse updateSales(Long id, SalesRequest request) {
		if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
		Sales sales = salesRepository.findById(id)
				.orElseThrow(() -> new SalesNotFoundException("Sales not found with id: " + id));
		Buyer buyer = sales.getBuyer();
		if(request.buyerId() != null && (buyer.getId() == null || !request.buyerId().equals(buyer.getId()))) {
			buyer = fetchBuyerId(request.buyerId());
		}
		validateUpdateAndCreateMethod(request);
		salesMapper.toEntityUpdate(sales, request, buyer);
		updateAndCreateSales(sales, request);
		Sales saved = salesRepository.save(sales);
		return salesMapper.toResponse(saved);
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
		List<Sales> items = salesRepository.findByCreatedAtBetween(start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Sales for createdSt between %s and %s is found",
					start.format(formatter), end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(salesMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> getByTotalPrice(BigDecimal totalPrice) {
		validateBigDecimal(totalPrice);
		List<Sales> items = salesRepository.findByTotalPrice(totalPrice);
		if(items.isEmpty()) {
			String msg = String.format("No Sales for total-price %s is found", totalPrice);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
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
								.orElseThrow(() -> new ValidationException(
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
		List<Sales> items = salesRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Sales list is empty");
		}
		return salesRepository.findAll().stream()
				.map(salesMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByBuyer_Id(Long buyerId) {
		fetchBuyerId(buyerId);
		List<Sales> items = salesRepository.findByBuyer_Id(buyerId);
		if(items.isEmpty()) {
			String msg = String.format("No Sales for buyer-id %d is found", buyerId);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByBuyer_CompanyNameContainingIgnoreCase(String buyerCompanyName) {
		validateString(buyerCompanyName);
		List<Sales> items = salesRepository.findByBuyer_CompanyNameContainingIgnoreCase(buyerCompanyName);
		if(items.isEmpty()) {
			String msg = String.format("No Sales for buyer's company-name %s is found", buyerCompanyName);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByBuyer_PibContainingIgnoreCase(String buyerPib) {
		validateBuyerPIB(buyerPib);
		List<Sales> items = salesRepository.findByBuyer_PibContainingIgnoreCase(buyerPib);
		if(items.isEmpty()) {
			String msg = String.format("No Sales for buyer's pib %s is found", buyerPib);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByBuyer_AddressContainingIgnoreCase(String buyerAddress) {
		validateString(buyerAddress);
		List<Sales> items = salesRepository.findByBuyer_AddressContainingIgnoreCase(buyerAddress);
		if(items.isEmpty()) {
			String msg = String.format("No Sales for buyer's address %s is found", buyerAddress);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByBuyer_ContactPerson(String contactPerson) {
		validateString(contactPerson);
		List<Sales> items = salesRepository.findByBuyer_ContactPerson(contactPerson);
		if(items.isEmpty()) {
			String msg = String.format("No Sales for buyer's contact-person %s is found", contactPerson);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByBuyer_EmailContainingIgnoreCase(String buyerEmail) {
		validateString(buyerEmail);
		List<Sales> items = salesRepository.findByBuyer_EmailContainingIgnoreCase(buyerEmail);
		if(items.isEmpty()) {
			String msg = String.format("No Sales for buyer's email %s is found", buyerEmail);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByBuyer_PhoneNumber(String buyerPhoneNumber) {
		validateString(buyerPhoneNumber);
		List<Sales> items = salesRepository.findByBuyer_PhoneNumber(buyerPhoneNumber);
		if(items.isEmpty()) {
			String msg = String.format("No Sales for buyer's phone-number %s is found", buyerPhoneNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByTotalPriceGreaterThan(BigDecimal totalPrice) {
		validateBigDecimal(totalPrice);
		List<Sales> items = salesRepository.findByTotalPriceGreaterThan(totalPrice);
		if(items.isEmpty()) {
			String msg = String.format("No Sales for total-price greater than %s is found", totalPrice);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(SalesResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> findByTotalPriceLessThan(BigDecimal totalPrice) {
		validateBigDecimalNonNegative(totalPrice);
		List<Sales> items = salesRepository.findByTotalPriceLessThan(totalPrice);
		if(items.isEmpty()) {
			String msg = String.format("No Sales for total-price less than %s is found", totalPrice);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
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
	
	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
	
	private void validateBuyerPIB(String pib) {
		if(pib == null || pib.trim().isEmpty()) {
			throw new ValidationException("Buyer's PIB must not be null nor empty");
		}
		if(!salesRepository.existsByBuyer_Pib(pib)) {
			throw new BuyerNotFoundException("Buyer PIB not found "+pib);
		}
	}

}
