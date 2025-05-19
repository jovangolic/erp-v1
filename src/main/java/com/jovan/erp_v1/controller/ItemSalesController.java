package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.mapper.ItemSalesMapper;
import com.jovan.erp_v1.request.ItemSalesRequest;
import com.jovan.erp_v1.response.ItemSalesResponse;
import com.jovan.erp_v1.service.INTERItemSales;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/itemSales")
@CrossOrigin("http://localhost:5173")
public class ItemSalesController {

	
	private final INTERItemSales itemSales;
	private ItemSalesMapper itemSalesMapper;
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PostMapping("/create/new-item-sales")
	public ResponseEntity<ItemSalesResponse> create(@Valid @RequestBody ItemSalesRequest request){
		ItemSalesResponse response = itemSales.create(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<ItemSalesResponse> update(@PathVariable Long id, @Valid @RequestBody ItemSalesRequest request){
		ItemSalesResponse response = itemSales.update(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		itemSales.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/item/{id}")
	public ResponseEntity<ItemSalesResponse> getOne(@PathVariable Long id){
		ItemSalesResponse response = itemSales.getById(id);
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/get-all")
	public ResponseEntity<List<ItemSalesResponse>> getAllItemSales(){
		List<ItemSalesResponse> responses = itemSales.getAll();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/item/be-quantity")
	public ResponseEntity<List<ItemSalesResponse>> getByQuantity(@RequestParam("quantity") Integer quantity){
		List<ItemSalesResponse> responses = itemSales.getByQuantity(quantity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/item/by-salesId")
	public ResponseEntity<List<ItemSalesResponse>> getBySalesId(@RequestParam("salesId") Long salesId){
		List<ItemSalesResponse> responses = itemSales.getBySalesId(salesId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/item/by-goodsId")
	public ResponseEntity<List<ItemSalesResponse>> getByGoodsId(@RequestParam("goodsId") Long goodsId){
		List<ItemSalesResponse> responses = itemSales.getByGoodsId(goodsId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/item/by-procurementId")
	public ResponseEntity<List<ItemSalesResponse>> getByProcurementId(@RequestParam("procurementId") Long procurementId){
		List<ItemSalesResponse>responses = itemSales.getByProcurementId(procurementId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/item/by-unitPrice")
	public ResponseEntity<List<ItemSalesResponse>> getByUnitPrice(@RequestParam("unitPrice") BigDecimal unitPrice){
		List<ItemSalesResponse> responses = itemSales.getByUnitPrice(unitPrice);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/item/by-salesOrderId")
    public ResponseEntity<List<ItemSalesResponse>> getBySalesOrderId(@RequestParam("salesOrderId") Long salesOrderId) {
        List<ItemSalesResponse> responses = itemSales.getBySalesOrderId(salesOrderId);
        return ResponseEntity.ok(responses);
    }
	
	@GetMapping("/item/by-salesOrderNumber")
	public ResponseEntity<List<ItemSalesResponse>> getBySalesOrderNumber(@RequestParam("orderNumber") String orderNumber){
		List<ItemSalesResponse> responses = itemSales.getBySalesOrderNumber(orderNumber);
		return ResponseEntity.ok(responses);
	}
	
}
