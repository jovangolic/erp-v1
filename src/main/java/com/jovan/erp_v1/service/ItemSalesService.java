package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.exception.ItemSalesNotFoundException;
import com.jovan.erp_v1.exception.SalesOrderNotFoundException;
import com.jovan.erp_v1.mapper.ItemSalesMapper;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.model.SalesOrder;
import com.jovan.erp_v1.repository.GoodsRepository;
import com.jovan.erp_v1.repository.ItemSalesRepository;
import com.jovan.erp_v1.repository.ProcurementRepository;
import com.jovan.erp_v1.repository.SalesOrderRepository;
import com.jovan.erp_v1.repository.SalesRepository;
import com.jovan.erp_v1.request.ItemSalesRequest;
import com.jovan.erp_v1.response.ItemSalesResponse;

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
	
	@Override
	public ItemSalesResponse create(ItemSalesRequest request) {
		ItemSales items = new ItemSales();
		items.setId(request.id());
		items.setGoods(goodsRepository.findGoodsById(request.goodsId()));
		items.setSales(salesRepository.findSalesById(request.salesId()));
		items.setProcurement(findProcurementById(request.procurementId()));
		items.setSalesOrder(findBySalesOrderId(request.salesOrderId()));
		items.setQuantity(request.quantity());
		items.setUnitPrice(request.unitPrice());
		ItemSales saved = itemSalesRepository.save(items);
		return itemSalesMapper.toResponse(saved);
	}
	@Override
	public ItemSalesResponse update(Long id, ItemSalesRequest request) {
		ItemSales items = itemSalesRepository.findById(id).orElseThrow(() -> new ItemSalesNotFoundException("ItemSales with " +id+" not found" ));
		items.setGoods(goodsRepository.findGoodsById(request.goodsId()));
		items.setSales(salesRepository.findSalesById(request.salesId()));
		items.setProcurement(findProcurementById(request.procurementId()));
		items.setSalesOrder(findBySalesOrderId(request.salesOrderId()));
		items.setQuantity(request.quantity());
		items.setUnitPrice(request.unitPrice());
		ItemSales saved = itemSalesRepository.save(items);
		return itemSalesMapper.toResponse(saved);
	}
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
	public List<ItemSalesResponse> getByQuantity(Integer quantity) {
		return itemSalesRepository.findByQuantity(quantity).stream()
				.map(itemSalesMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> getBySalesId(Long salesId) {
		Sales sales = findSalesById(salesId);
		return itemSalesRepository.findBySales(sales).stream()
				.map(itemSalesMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> getByGoodsId(Long goodsId) {
		Goods goods = findGoodsById(goodsId);
		return itemSalesRepository.findByGoods(goods).stream()
				.map(itemSalesMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> getByProcurementId(Long procurementId) {
		Procurement procurement = findProcurementById(procurementId);
		return itemSalesRepository.findByProcurement(procurement).stream()
				.map(itemSalesMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> getByUnitPrice(BigDecimal unitPrice) {
		return itemSalesRepository.findByUnitPrice(unitPrice).stream()
				.map(itemSalesMapper::toResponse).collect(Collectors.toList());
	}
	
	private Goods findGoodsById(Long id) {
        return goodsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goods not found with id: " + id));
    }

    private Sales findSalesById(Long id) {
        return salesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales not found with id: " + id));
    }

    private Procurement findProcurementById(Long id) {
        return procurementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procurement not found with id: " + id));
    }
    
    private SalesOrder findBySalesOrderId(Long id) {
    	return salesOrderRepository.findById(id).orElseThrow(() -> new SalesOrderNotFoundException("Sales-Order not found with id: "+ id));
    }
    
    private SalesOrder findSalesOrderByOrderNumber(String orderNumber) {
        return salesOrderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new SalesOrderNotFoundException("SalesOrder not found with order number: " + orderNumber));
    }
    
	@Override
	public List<ItemSalesResponse> getBySalesOrderId(Long salesOrderId) {
		SalesOrder salesOrder = findBySalesOrderId(salesOrderId);
		return itemSalesRepository.findBySalesOrder(salesOrder).stream()
				.map(itemSalesMapper::toResponse)
				.collect(Collectors.toList());
	}
	@Override
	public List<ItemSalesResponse> getBySalesOrderNumber(String orderNumber) {
		SalesOrder salesOrder = findSalesOrderByOrderNumber(orderNumber);
	    return itemSalesRepository.findBySalesOrder(salesOrder).stream()
	            .map(itemSalesMapper::toResponse)
	            .collect(Collectors.toList());
	}
	
}
