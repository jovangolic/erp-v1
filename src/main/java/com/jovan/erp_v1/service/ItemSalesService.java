package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.GoodsNotFoundException;
import com.jovan.erp_v1.exception.InvoiceNotFoundException;
import com.jovan.erp_v1.exception.ItemSalesNotFoundException;
import com.jovan.erp_v1.exception.ProcurementNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.exception.SalesOrderNotFoundException;
import com.jovan.erp_v1.exception.ShelfNotFoundException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.exception.SupplyNotFoundException;
import com.jovan.erp_v1.mapper.ItemSalesMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.Invoice;
import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.model.SalesOrder;
import com.jovan.erp_v1.model.Shelf;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.InvoiceRepository;
import com.jovan.erp_v1.repository.ItemSalesRepository;
import com.jovan.erp_v1.repository.ProcurementRepository;
import com.jovan.erp_v1.repository.SalesOrderRepository;
import com.jovan.erp_v1.repository.SalesRepository;
import com.jovan.erp_v1.repository.ShelfRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.repository.SupplyRepository;
import com.jovan.erp_v1.repository.specification.ItemSalesSpecification;
import com.jovan.erp_v1.request.ItemSalesFilterRequest;
import com.jovan.erp_v1.request.ItemSalesRequest;
import com.jovan.erp_v1.response.ItemSalesResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemSalesService implements INTERItemSales {

	private final ItemSalesRepository itemSalesRepository;
    private final GoodsRepository goodsRepository;
    private final SalesRepository salesRepository;
    private final ProcurementRepository procurementRepository;
    private final ItemSalesMapper itemSalesMapper;
    private final SalesOrderRepository salesOrderRepository;
    private final BuyerRepository buyerRepository;
    private final InvoiceRepository invoiceRepository;
    private final ShelfRepository shelfRepository;
    private final SupplyRepository supplyRepository;
    private final StorageRepository storageRepository;
	
    @Transactional
	@Override
	public ItemSalesResponse create(ItemSalesRequest request) {
		Goods goods = findGoodsById(request.goodsId());
		Sales sales = findSalesById(request.salesId());
		Procurement procurement = findProcurementById(request.procurementId());
		SalesOrder so = findBySalesOrderId(request.salesOrderId());
		validateBigDecimal(request.quantity());
		validateBigDecimal(request.unitPrice());
		ItemSales items = itemSalesMapper.toCreateEntity(request, goods, sales, procurement, so);
		ItemSales saved = itemSalesRepository.save(items);
		return itemSalesMapper.toResponse(saved);
	}
    
    @Transactional
	@Override
	public ItemSalesResponse update(Long id, ItemSalesRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
    	ItemSales existing = itemSalesRepository.findById(id)
    	        .orElseThrow(() -> new ItemSalesNotFoundException("ItemSales not found with id: " + id));
    	validateBigDecimal(request.quantity());
    	validateBigDecimal(request.unitPrice());
    	ItemSales updated = itemSalesMapper.toUpdateEntity(existing, request);
    	ItemSales saved = itemSalesRepository.save(updated);
    	return itemSalesMapper.toResponse(saved);
	}
    
    @Transactional
	@Override
	public void delete(Long id) {
		if (!itemSalesRepository.existsById(id)) {
            throw new RuntimeException("ItemSales not found with id: " + id);
        }
        itemSalesRepository.deleteById(id);
	}
	@Override
	public ItemSalesResponse getById(Long id) {
		ItemSales items = itemSalesRepository.findById(id).orElseThrow(() -> new ItemSalesNotFoundException("ItemSales with " +id+" not found" ));
		return new ItemSalesResponse(items);
	}
	@Override
    public List<ItemSalesResponse> getAll() {
        return itemSalesRepository.findAll().stream()
                .map(itemSalesMapper::toResponse)
                .collect(Collectors.toList());
    }
	
	@Override
	public List<ItemSalesResponse> findByGoods_Id(Long goodsId) {
		Goods goods = findGoodsById(goodsId);
		return itemSalesRepository.findByGoods_Id(goods.getId()).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByGoods_NameContainingIgnoreCase(String goodsName) {
		validateString(goodsName);
		return itemSalesRepository.findByGoods_NameContainingIgnoreCase(goodsName).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByGoods_UnitMeasure(UnitMeasure unitMeasure) {
		fetchUnitMeasure(unitMeasure);
		return itemSalesRepository.findByGoods_UnitMeasure(unitMeasure).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByGoods_SupplierType(SupplierType supplierType) {
		fetchSupplierType(supplierType);
		return itemSalesRepository.findByGoods_SupplierType(supplierType).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByGoods_StorageType(StorageType storageType) {
		fetchStorageType(storageType);
		return itemSalesRepository.findByGoods_StorageType(storageType).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByGoods_GoodsType(GoodsType goodsType) {
		fetchGoodsType(goodsType);
		return itemSalesRepository.findByGoods_GoodsType(goodsType).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByGoods_Storage_Id(Long storageId) {
		Storage storage = fetchStorage(storageId);
		return itemSalesRepository.findByGoods_Storage_Id(storage.getId()).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByGoods_Supply_Id(Long supplyId) {
		Supply supply = fetchSupply(supplyId);
		return itemSalesRepository.findByGoods_Supply_Id(supply.getId()).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByGoods_Shelf_Id(Long shelfId) {
		Shelf shelf = fetchShelf(shelfId);
		return itemSalesRepository.findByGoods_Shelf_Id(shelf.getId()).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByGoods_Shelf_RowCount(Integer rowCount) {
		validateInteger(rowCount);
		return itemSalesRepository.findByGoods_Shelf_RowCount(rowCount).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByGoods_Shelf_Cols(Integer cols) {
		validateInteger(cols);
		return itemSalesRepository.findByGoods_Shelf_Cols(cols).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySales_Id(Long salesId) {
		Sales sales = findSalesById(salesId);
		return itemSalesRepository.findBySales_Id(sales.getId()).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySales_Buyer_Id(Long buyerId) {
		Buyer buyer = findByBuyerId(buyerId);
		return itemSalesRepository.findBySales_Buyer_Id(buyer.getId()).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySales_CreatedAt(LocalDateTime createdAt) {
		DateValidator.validateNotNull(createdAt, "Date and Time");
		return itemSalesRepository.findBySales_CreatedAt(createdAt).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySales_CreatedAtBetween(LocalDateTime createdAtStart,
			LocalDateTime createdAtEnd) {
		DateValidator.validateRange(createdAtStart, createdAtEnd);
		return itemSalesRepository.findBySales_CreatedAtBetween(createdAtStart, createdAtEnd).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySales_TotalPrice(BigDecimal totalPrice) {
		validateBigDecimal(totalPrice);
		return itemSalesRepository.findBySales_TotalPrice(totalPrice).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySales_TotalPriceGreaterThan(BigDecimal totalPrice) {
		validateBigDecimal(totalPrice);
		return itemSalesRepository.findBySales_TotalPriceGreaterThan(totalPrice).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySales_TotalPriceLessThan(BigDecimal totalPrice) {
		validateBigDecimal(totalPrice);
		return itemSalesRepository.findBySales_TotalPriceLessThan(totalPrice).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySales_SalesDescription(String salesDescription) {
		validateString(salesDescription);
		return itemSalesRepository.findBySales_SalesDescription(salesDescription).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByProcurement_Id(Long procurementId) {
		Procurement p = findProcurementById(procurementId);
		return itemSalesRepository.findByProcurement_Id(p.getId()).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByProcurement_Date(LocalDateTime date) {
		DateValidator.validateNotNull(date, "Date and Time");
		return itemSalesRepository.findByProcurement_Date(date).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByProcurement_DateBetween(LocalDateTime dateStart, LocalDateTime dateEnd) {
		DateValidator.validateRange(dateStart, dateEnd);
		return itemSalesRepository.findByProcurement_DateBetween(dateStart, dateEnd).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByProcurement_TotalCost(BigDecimal totalCost) {
		validateBigDecimal(totalCost);
		return itemSalesRepository.findByProcurement_TotalCost(totalCost).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByProcurement_TotalCostGreaterThan(BigDecimal totalCost) {
		validateBigDecimal(totalCost);
		return itemSalesRepository.findByProcurement_TotalCostGreaterThan(totalCost).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByProcurement_TotalCostLessThan(BigDecimal totalCost) {
		validateBigDecimal(totalCost);
		return itemSalesRepository.findByProcurement_TotalCostLessThan(totalCost).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySalesOrder_Id(Long salesOrderId) {
		SalesOrder so = findBySalesOrderId(salesOrderId);
		return itemSalesRepository.findBySales_Id(so.getId()).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<ItemSalesResponse> findBySalesOrder_OrderNumber(String orderNumber) {
		if(!itemSalesRepository.existsBySalesOrder_OrderNumber(orderNumber)) {
			throw new ItemSalesNotFoundException("OrderNumber not found "+orderNumber);
		}
		return itemSalesRepository.findBySalesOrder_OrderNumber(orderNumber).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySalesOrder_OrderDate(LocalDateTime orderDate) {
		DateValidator.validateNotNull(orderDate, "Date and Time");
		return itemSalesRepository.findBySalesOrder_OrderDate(orderDate).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySalesOrder_OrderDateBetween(LocalDateTime orderDateStart,
			LocalDateTime orderDateEnd) {
		DateValidator.validateRange(orderDateStart, orderDateEnd);
		return itemSalesRepository.findBySalesOrder_OrderDateBetween(orderDateStart, orderDateEnd).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySalesOrder_TotalAmount(BigDecimal totalAmount) {
		validateBigDecimal(totalAmount);
		return itemSalesRepository.findBySalesOrder_TotalAmount(totalAmount).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySalesOrder_TotalAmountGreaterThan(BigDecimal totalAmount) {
		validateBigDecimal(totalAmount);
		return itemSalesRepository.findBySalesOrder_TotalAmountGreaterThan(totalAmount).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySalesOrder_TotalAmountLessThan(BigDecimal totalAmount) {
		validateBigDecimal(totalAmount);
		return itemSalesRepository.findBySalesOrder_TotalAmountLessThan(totalAmount).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySalesOrder_Buyer_Id(Long buyerId) {
		Buyer buyer = findByBuyerId(buyerId);
		return itemSalesRepository.findBySalesOrder_Buyer_Id(buyer.getId()).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySalesOrder_OrderStatus(OrderStatus status) {
		validateOrderStatus(status);
		return itemSalesRepository.findBySalesOrder_OrderStatus(status).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findBySalesOrder_Invoice_Id(Long invoiceId) {
		Invoice invoice = findByInvoiceId(invoiceId);
		return itemSalesRepository.findBySalesOrder_Invoice_Id(invoice.getId()).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByQuantity(BigDecimal quantity) {
		validateBigDecimal(quantity);
		return itemSalesRepository.findByQuantity(quantity).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByQuantityLessThan(BigDecimal quantity) {
		validateBigDecimal(quantity);
		return itemSalesRepository.findByQuantityLessThan(quantity).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByQuantityGreaterThan(BigDecimal quantity) {
		validateBigDecimal(quantity);
		return itemSalesRepository.findByQuantityGreaterThan(quantity).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByUnitPrice(BigDecimal unitPrice) {
		validateBigDecimal(unitPrice);
		return itemSalesRepository.findByUnitPrice(unitPrice).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByUnitPriceGreaterThan(BigDecimal unitPrice) {
		validateBigDecimal(unitPrice);
		return itemSalesRepository.findByUnitPriceGreaterThan(unitPrice).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> findByUnitPriceLessThan(BigDecimal unitPrice) {
		validateBigDecimal(unitPrice);
		return itemSalesRepository.findByUnitPriceLessThan(unitPrice).stream()
				.map(ItemSalesResponse::new)
				.collect(Collectors.toList());
	}
	
	
	@Override
	public List<ItemSalesResponse> filter(ItemSalesFilterRequest filterRequest) {
	    Specification<ItemSales> spec = Specification.where(null);

	    if (filterRequest.goodsId() != null) {
	        spec = spec.and(ItemSalesSpecification.hasGoodsId(filterRequest.goodsId()));
	    }
	    if (filterRequest.buyerId() != null) {
	        spec = spec.and(ItemSalesSpecification.hasBuyerId(filterRequest.buyerId()));
	    }
	    if (filterRequest.createdAtStart() != null && filterRequest.createdAtEnd() != null) {
	        spec = spec.and(ItemSalesSpecification.createdBetween(
	            filterRequest.createdAtStart(), filterRequest.createdAtEnd()));
	    }
	    if (filterRequest.minTotalPrice() != null && filterRequest.maxTotalPrice() != null) {
	        spec = spec.and(ItemSalesSpecification.totalPriceBetween(
	            filterRequest.minTotalPrice(), filterRequest.maxTotalPrice()));
	    }
	    if (filterRequest.orderStatus() != null) {
	        spec = spec.and(ItemSalesSpecification.hasSalesOrderStatus(filterRequest.orderStatus()));
	    }
	    List<ItemSales> result = itemSalesRepository.findAll(spec);
	    return result.stream().map(itemSalesMapper::toResponse).collect(Collectors.toList());
	}
	
	private Supply fetchSupply(Long supplyId) {
		if(supplyId == null) {
			throw new SupplyNotFoundException("Supply ID must not be null");
		}
		return supplyRepository.findById(supplyId).orElseThrow(() -> new SupplyNotFoundException("Supply not found with id "+supplyId));
	}
	
	private Storage fetchStorage(Long storageId) {
		if(storageId == null) {
			throw new StorageNotFoundException("Storage ID must not be null");
		}
		return storageRepository.findById(storageId).orElseThrow(() -> new StorageNotFoundException("Storage not found with id "+storageId));
	}
	
	private void fetchGoodsType(GoodsType goodsType) {
		if(goodsType == null) {
			throw new IllegalArgumentException("GoodsType goodsType must not be null");
		}
	}
	
	private void fetchStorageType(StorageType storageType) {
		if(storageType == null) {
			throw new IllegalArgumentException("StorageType storageType must not be null");
		}
	}
	
	private void fetchSupplierType(SupplierType supplierType) {
		if(supplierType == null) {
			throw new IllegalArgumentException("SupplierType supplierType must not be null");
		}
	}
	
	private void fetchUnitMeasure(UnitMeasure unitMeasure) {
		if(unitMeasure == null) {
			throw new IllegalArgumentException("UnitMeasure unitMeasure must not be null");
		}
	}
	
	private void validateInteger(Integer num) {
		if(num == null || num < 0) {
			throw new IllegalArgumentException("Number must be positive");
		}
	}
	
	private void validateOrderStatus(OrderStatus status) {
		if(status == null) {
			throw new IllegalArgumentException("OrderStatus status must not be null");
		}
	}
	
	private Goods findGoodsById(Long id) {
		if(id == null) {
			throw new GoodsNotFoundException("Goods ID must not be null");
		}
        return goodsRepository.findById(id)
                .orElseThrow(() -> new GoodsNotFoundException("Goods not found with id: " + id));
    }

    private Sales findSalesById(Long id) {
		if(id == null) {
			throw new SalesNotFoundException("Sales ID must not be null");
		}
        return salesRepository.findById(id)
                .orElseThrow(() -> new SalesNotFoundException("Sales not found with id: " + id));
    }

    private Procurement findProcurementById(Long id) {
		if(id == null) {
			throw new ProcurementNotFoundException("Procurement ID must not be null");	
		}
        return procurementRepository.findById(id)
                .orElseThrow(() -> new ProcurementNotFoundException("Procurement not found with id: " + id));
    }
    
    private Buyer findByBuyerId(Long id) {
		if(id == null) {
			throw new BuyerNotFoundException("Buyer ID must not be null");
		}
    	return buyerRepository.findById(id).orElseThrow(() -> new BuyerNotFoundException("SBuyer not found with id: "+ id));
    }
    
    private Invoice findByInvoiceId(Long id) {
		if(id == null) {
			throw new InvoiceNotFoundException("Invoice ID must not be null");
		}
    	return invoiceRepository.findById(id).orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with id: "+ id));
    }
    
    private SalesOrder findBySalesOrderId(Long id) {
		if(id == null) {
			throw new SalesOrderNotFoundException("SalesOrder ID must not be null");
		}
    	return salesOrderRepository.findById(id).orElseThrow(() -> new SalesOrderNotFoundException("Sales-Order not found with id: "+ id));
    }
    
    private SalesOrder findSalesOrderByOrderNumber(String orderNumber) {
        return salesOrderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new SalesOrderNotFoundException("SalesOrder not found with order number: " + orderNumber));
    }
    
    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }

    private void validateString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Tekstualni karakter ne sme biti null ili prazan");
        }
    }
	
    private Shelf fetchShelf(Long shelfId) {
    	if(shelfId == null) {
    		throw new ShelfNotFoundException("Shelf ID must not be null");
    	}
    	return shelfRepository.findById(shelfId).orElseThrow(() -> new ShelfNotFoundException("Shelf not found with id: "+shelfId));
    }
    
}
