package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.ItemSalesFilterRequest;
import com.jovan.erp_v1.request.ItemSalesRequest;
import com.jovan.erp_v1.response.ItemSalesResponse;
import com.jovan.erp_v1.service.INTERItemSales;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/itemSales")
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
public class ItemSalesController {

	private final INTERItemSales itemSales;
	
	@PreAuthorize(RoleGroups.ITEM_SALES_FULL_ACCESS)
	@PostMapping("/create/new-item-sales")
	public ResponseEntity<ItemSalesResponse> create(@Valid @RequestBody ItemSalesRequest request){
		ItemSalesResponse response = itemSales.create(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<ItemSalesResponse> update(@PathVariable Long id, @Valid @RequestBody ItemSalesRequest request){
		ItemSalesResponse response = itemSales.update(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		itemSales.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/item/{id}")
	public ResponseEntity<ItemSalesResponse> getOne(@PathVariable Long id){
		ItemSalesResponse response = itemSales.getById(id);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/get-all")
	public ResponseEntity<List<ItemSalesResponse>> getAllItemSales(){
		List<ItemSalesResponse> responses = itemSales.getAll();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-goods/{goodsId}")
	public ResponseEntity<List<ItemSalesResponse>> findByGoods_Id(@PathVariable Long goodsId){
		List<ItemSalesResponse> responses = itemSales.findByGoods_Id(goodsId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-goods-name")
	public ResponseEntity<List<ItemSalesResponse>> findByGoods_NameContainingIgnoreCase(@RequestParam("goodsName") String goodsName){
		List<ItemSalesResponse> responses = itemSales.findByGoods_NameContainingIgnoreCase(goodsName);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-unit-measure")
	public ResponseEntity<List<ItemSalesResponse>> findByGoods_UnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure){
		List<ItemSalesResponse> responses = itemSales.findByGoods_UnitMeasure(unitMeasure);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-supplier-type")
	public ResponseEntity<List<ItemSalesResponse>> findByGoods_SupplierType(@RequestParam("supplierType") SupplierType supplierType){
		List<ItemSalesResponse> responses = itemSales.findByGoods_SupplierType(supplierType);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-storage-type")
	public ResponseEntity<List<ItemSalesResponse>> findByGoods_StorageType(@RequestParam("storageType") StorageType storageType){
		List<ItemSalesResponse> responses = itemSales.findByGoods_StorageType(storageType);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-goods-type")
	public ResponseEntity<List<ItemSalesResponse>> findByGoods_GoodsType(@RequestParam("goodsType") GoodsType goodsType){
		List<ItemSalesResponse> responses = itemSales.findByGoods_GoodsType(goodsType);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-storage/{storageId}")
	public ResponseEntity<List<ItemSalesResponse>> findByGoods_Storage_Id(@PathVariable Long storageId){
		List<ItemSalesResponse> responses = itemSales.findByGoods_Storage_Id(storageId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-supply/{supplyId}")
	public ResponseEntity<List<ItemSalesResponse>> findByGoods_Supply_Id(@PathVariable Long supplyId){
		List<ItemSalesResponse> responses = itemSales.findByGoods_Supply_Id(supplyId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-shelf/{shelfId}")
	public ResponseEntity<List<ItemSalesResponse>> findByGoods_Shelf_Id(@PathVariable Long shelfId){
		List<ItemSalesResponse> responses = itemSales.findByGoods_Shelf_Id(shelfId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-shelf-row")
	public ResponseEntity<List<ItemSalesResponse>> findByGoods_Shelf_RowCount(@RequestParam("rowCount") Integer rowCount){
		List<ItemSalesResponse> responses = itemSales.findByGoods_Shelf_RowCount(rowCount);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-shelf-cols")
	public ResponseEntity<List<ItemSalesResponse>> findByGoods_Shelf_Cols(@RequestParam("cols") Integer cols){
		List<ItemSalesResponse> responses = itemSales.findByGoods_Shelf_Cols(cols);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-sales/id/{salesId}")
	public ResponseEntity<List<ItemSalesResponse>> findBySales_Id(@PathVariable Long salesId){
		List<ItemSalesResponse> responses = itemSales.findBySales_Id(salesId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-sales/buyer/{buyerId}")
	public ResponseEntity<List<ItemSalesResponse>> findBySales_Buyer_Id(@PathVariable Long buyerId){
		List<ItemSalesResponse> responses = itemSales.findBySales_Buyer_Id(buyerId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-by-createdAt")
	public ResponseEntity<List<ItemSalesResponse>> findBySales_CreatedAt(@RequestParam("createdAt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt){
		List<ItemSalesResponse> responses = itemSales.findBySales_CreatedAt(createdAt);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-created-range")
	public ResponseEntity<List<ItemSalesResponse>> findBySales_CreatedAtBetween(@RequestParam("createdAtStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtStart,
			@RequestParam("createdAtEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtEnd){
		List<ItemSalesResponse> responses = itemSales.findBySales_CreatedAtBetween(createdAtStart, createdAtEnd);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-total-price")
	public ResponseEntity<List<ItemSalesResponse>> findBySales_TotalPrice(@RequestParam("totalPrice") BigDecimal totalPrice){
		List<ItemSalesResponse> responses = itemSales.findBySales_TotalPrice(totalPrice);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-total-price-greate-than")
	public ResponseEntity<List<ItemSalesResponse>> findBySales_TotalPriceGreaterThan(@RequestParam("totalPrice") BigDecimal totalPrice){
		List<ItemSalesResponse> responses = itemSales.findBySales_TotalPriceGreaterThan(totalPrice);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-total-price-less-than")
	public ResponseEntity<List<ItemSalesResponse>> findBySales_TotalPriceLessThan(@RequestParam("totalPrice") BigDecimal totalPrice){
		List<ItemSalesResponse> responses = itemSales.findBySales_TotalPriceLessThan(totalPrice);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-sales-description")
	public ResponseEntity<List<ItemSalesResponse>> findBySales_SalesDescription(@RequestParam("salesDescription") String salesDescription){
		List<ItemSalesResponse> responses = itemSales.findBySales_SalesDescription(salesDescription);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-procurement/{procurementId}")
	public ResponseEntity<List<ItemSalesResponse>> findByProcurement_Id(@PathVariable Long procurementId){
		List<ItemSalesResponse> responses = itemSales.findByProcurement_Id(procurementId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-date")
	public ResponseEntity<List<ItemSalesResponse>> findByProcurement_Date(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		List<ItemSalesResponse> responses = itemSales.findByProcurement_Date(date);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-date-range")
	public ResponseEntity<List<ItemSalesResponse>> findByProcurement_DateBetween(@RequestParam("dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateStart,
			@RequestParam("dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateEnd){
		List<ItemSalesResponse> responses = itemSales.findByProcurement_DateBetween(dateStart, dateEnd);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-total-cost")
	public ResponseEntity<List<ItemSalesResponse>> findByProcurement_TotalCost(@RequestParam("totalCost") BigDecimal totalCost){
		List<ItemSalesResponse> responses = itemSales.findByProcurement_TotalCost(totalCost);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-total-cost-greater-than")
	public ResponseEntity<List<ItemSalesResponse>> findByProcurement_TotalCostGreaterThan(@RequestParam("totalCost") BigDecimal totalCost){
		List<ItemSalesResponse> responses = itemSales.findByProcurement_TotalCostGreaterThan(totalCost);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-total-cost-less-than")
	public ResponseEntity<List<ItemSalesResponse>> findByProcurement_TotalCostLessThan(@RequestParam("totalCost") BigDecimal totalCost){
		List<ItemSalesResponse> responses = itemSales.findByProcurement_TotalCostLessThan(totalCost);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-salesOrder/id/{salesOrderId}")
	public ResponseEntity<List<ItemSalesResponse>> findBySalesOrder_Id(@PathVariable Long salesOrderId){
		List<ItemSalesResponse> responses = itemSales.findBySalesOrder_Id(salesOrderId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-order-number")
	public ResponseEntity<List<ItemSalesResponse>> findBySalesOrder_OrderNumber(@RequestParam("orderNumber") String orderNumber){
		List<ItemSalesResponse> responses = itemSales.findBySalesOrder_OrderNumber(orderNumber);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-order-date")
	public ResponseEntity<List<ItemSalesResponse>> findBySalesOrder_OrderDate(@RequestParam("orderDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime orderDate){
		List<ItemSalesResponse> responses = itemSales.findBySalesOrder_OrderDate(orderDate);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-order-date-range")
	public ResponseEntity<List<ItemSalesResponse>> findBySalesOrder_OrderDateBetween(@RequestParam("orderDateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime orderDateStart,
			@RequestParam("orderDateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime orderDateEnd){
		List<ItemSalesResponse> responses = itemSales.findBySalesOrder_OrderDateBetween(orderDateStart, orderDateEnd);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-total-amount")
	public ResponseEntity<List<ItemSalesResponse>> findBySalesOrder_TotalAmount(@RequestParam("totalAmount") BigDecimal totalAmount){
		List<ItemSalesResponse> responses = itemSales.findBySalesOrder_TotalAmount(totalAmount);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-total-amount-greater-than")
	public ResponseEntity<List<ItemSalesResponse>> findBySalesOrder_TotalAmountGreaterThan(@RequestParam("totalAmount") BigDecimal totalAmount){
		List<ItemSalesResponse> responses = itemSales.findBySalesOrder_TotalAmountGreaterThan(totalAmount);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-total-amount-less-than")
	public ResponseEntity<List<ItemSalesResponse>> findBySalesOrder_TotalAmountLessThan(@RequestParam("totalAmount") BigDecimal totalAmount){
		List<ItemSalesResponse> responses = itemSales.findBySalesOrder_TotalAmountLessThan(totalAmount);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-salesOrder/buyer/{buyerId}")
	public ResponseEntity<List<ItemSalesResponse>> findBySalesOrder_Buyer_Id(@PathVariable Long buyerId){
		List<ItemSalesResponse> responses = itemSales.findBySalesOrder_Buyer_Id(buyerId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale-status")
	public ResponseEntity<List<ItemSalesResponse>> findBySalesOrder_OrderStatus(@RequestParam("status") OrderStatus status){
		List<ItemSalesResponse> responses = itemSales.findBySalesOrder_OrderStatus(status);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/itemSale/by-salesOrder/invoice/{invoiceId}")
	public ResponseEntity<List<ItemSalesResponse>> findBySalesOrder_Invoice_Id(@PathVariable Long invoiceId){
		List<ItemSalesResponse> responses = itemSales.findBySalesOrder_Invoice_Id(invoiceId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/by-quantity")
	public ResponseEntity<List<ItemSalesResponse>> findByQuantity(@RequestParam("quantity") BigDecimal quantity){
		List<ItemSalesResponse> responses = itemSales.findByQuantity(quantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/by-quantity-less-than")
	public ResponseEntity<List<ItemSalesResponse>> findByQuantityLessThan(@RequestParam("quantity") BigDecimal quantity){
		List<ItemSalesResponse> responses = itemSales.findByQuantityLessThan(quantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/by-quantity-greater-than")
	public ResponseEntity<List<ItemSalesResponse>>  findByQuantityGreaterThan(@RequestParam("quantity") BigDecimal quantity){
		List<ItemSalesResponse> responses = itemSales.findByQuantityGreaterThan(quantity);
		return ResponseEntity.ok(responses);
	}
	 
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/by-unit-price")
	public ResponseEntity<List<ItemSalesResponse>> findByUnitPrice(@RequestParam("unitPrice") BigDecimal unitPrice){
		List<ItemSalesResponse> responses = itemSales.findByUnitPrice(unitPrice);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/by-unit-price-greater-than")
	public ResponseEntity<List<ItemSalesResponse>> findByUnitPriceGreaterThan(@RequestParam("unitPrice") BigDecimal unitPrice){
		List<ItemSalesResponse> responses = itemSales.findByUnitPriceGreaterThan(unitPrice);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	@GetMapping("/by-unit-price-less-than")
	public ResponseEntity<List<ItemSalesResponse>> findByUnitPriceLessThan(@RequestParam("unitPrice") BigDecimal unitPrice){
		List<ItemSalesResponse> responses = itemSales.findByUnitPriceLessThan(unitPrice);
		return ResponseEntity.ok(responses);
	}
	
	//swagger dokumentacija
	@GetMapping("/search")
	@PreAuthorize(RoleGroups.ITEM_SALES_READ_ACCESS)
	public ResponseEntity<List<ItemSalesResponse>> search(@ParameterObject @ModelAttribute ItemSalesFilterRequest filterRequest) {
	    List<ItemSalesResponse> result = itemSales.filter(filterRequest);
	    return ResponseEntity.ok(result);
	}
	
	/*@PostMapping("/search")
	public ResponseEntity<List<ItemSalesResponse>> searchPost(@RequestBody ItemSalesFilterRequest filterRequest) {
	    List<ItemSalesResponse> result = itemSales.filter(filterRequest);
	    return ResponseEntity.ok(result);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<ItemSalesResponse>> search(@ModelAttribute ItemSalesFilterRequest filterRequest) {
	    List<ItemSalesResponse> result = itemSales.filter(filterRequest);
	    return ResponseEntity.ok(result);
	}*/
	
}
