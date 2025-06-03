package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.mapper.SalesMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.ItemSalesRepository;
import com.jovan.erp_v1.repository.SalesRepository;
import com.jovan.erp_v1.request.ItemSalesRequest;
import com.jovan.erp_v1.request.SalesRequest;
import com.jovan.erp_v1.response.SalesResponse;

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
		Buyer buyer = buyerRepository.findById(request.buyerId())
				.orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: " + request.buyerId()));
		sales.setBuyer(buyer);
		updateAndCreateSales(sales, request);
		sales.setCreatedAt(request.createdAt());
		sales.setTotalPrice(request.totalPrice());
		return salesMapper.toResponse(sales);
	}

	@Transactional
	@Override
	public SalesResponse updateSales(Long id, SalesRequest request) {
		Sales sales = salesRepository.findById(id)
				.orElseThrow(() -> new SalesNotFoundException("Sales not found with id: " + id));
		Buyer buyer = buyerRepository.findById(request.buyerId())
				.orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: " + request.buyerId()));
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
		return salesRepository.findByCreatedAtBetween(start, end).stream()
				.map(salesMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> getByBuyer(Buyer buyer) {
		return salesRepository.findByBuyer(buyer).stream()
				.map(salesMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesResponse> getByTotalPrice(BigDecimal totalPrice) {
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

}
