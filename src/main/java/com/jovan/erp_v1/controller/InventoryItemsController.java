package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InventoryItemsStatus;
import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.InventoryItemsRequest;
import com.jovan.erp_v1.response.InventoryItemCalculateResponse;
import com.jovan.erp_v1.response.InventoryItemStorageCapacityResponse;
import com.jovan.erp_v1.response.InventoryItemsResponse;
import com.jovan.erp_v1.response.InventorySummaryResponse;
import com.jovan.erp_v1.response.StorageCapacityAndInventorySummaryResponseFull;
import com.jovan.erp_v1.response.StorageCapacityResponse;
import com.jovan.erp_v1.response.StorageItemSummaryResponse;
import com.jovan.erp_v1.save_as.InventoryItemsSaveAsRequest;
import com.jovan.erp_v1.search_request.InventoryItemsSearchRequest;
import com.jovan.erp_v1.service.IInventoryItemsService;
import com.jovan.erp_v1.statistics.inventory_items.InventoryStatRequest;
import com.jovan.erp_v1.statistics.inventory_items.InventoryStatResponse;
import com.jovan.erp_v1.statistics.inventory_items.ItemConditionByProductStatDTO;
import com.jovan.erp_v1.statistics.inventory_items.QuantityByProductStatDTO;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventoryItems")
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
public class InventoryItemsController {

	private final IInventoryItemsService inventoryItemsService;
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_FULL_ACCESS)
	@PostMapping("/create/new-inventory-items")
	public ResponseEntity<InventoryItemsResponse> create(@Valid @RequestBody InventoryItemsRequest request){
		InventoryItemsResponse response = inventoryItemsService.create(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<InventoryItemsResponse> update(@PathVariable Long id, @Valid @RequestBody InventoryItemsRequest request){
		InventoryItemsResponse response = inventoryItemsService.update(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		inventoryItemsService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/get-one/{id}")
	public ResponseEntity<InventoryItemsResponse> findOneById(@PathVariable Long id){
		InventoryItemsResponse response = inventoryItemsService.findById(id);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/get-all")
	public ResponseEntity<List<InventoryItemsResponse>> findAll(){
		List<InventoryItemsResponse> responses = inventoryItemsService.findAllInventories();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/by-quantity")
	public ResponseEntity<List<InventoryItemsResponse>> getByQuantity(@RequestParam("quantity") BigDecimal quantity){
		List<InventoryItemsResponse> responses = inventoryItemsService.getByQuantity(quantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/by-condition")
	public ResponseEntity<List<InventoryItemsResponse>> getByCondition(@RequestParam("condition") BigDecimal itemCondition){
		List<InventoryItemsResponse> responses = inventoryItemsService.getByItemCondition(itemCondition);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/by-inventory/{inventoryId}")
	public ResponseEntity<List<InventoryItemsResponse>> getByInventoryId(@PathVariable Long inventoryId){
		List<InventoryItemsResponse> responses = inventoryItemsService.getByInventoryId(inventoryId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/by-product/{productId}")
	public ResponseEntity<List<InventoryItemsResponse>> getByProductId(@PathVariable Long productId){
		List<InventoryItemsResponse> responses = inventoryItemsService.getByProductId(productId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/by-product-name")
	public ResponseEntity<List<InventoryItemsResponse>> getByProductName(@RequestParam("productName") String productName){
		List<InventoryItemsResponse> responses = inventoryItemsService.getByProductName(productName);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/find-by-threshold")
	public ResponseEntity<List<InventoryItemsResponse>> findItemsWithDifference(@RequestParam("threshold") BigDecimal threshold){
		List<InventoryItemsResponse> responses = inventoryItemsService.findItemsWithDifference(threshold);
		return ResponseEntity.ok(responses);
	}
	
	//nove metode
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/by-difference")
	public ResponseEntity<List<InventoryItemsResponse>> findByDifference(@RequestParam("difference") BigDecimal difference){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByDifference(difference);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/by-difference-less-than")
	public ResponseEntity<List<InventoryItemsResponse>> findByDifferenceLessThan(@RequestParam("difference") BigDecimal difference){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByDifferenceLessThan(difference);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/by-quantity-greater-than")
	public ResponseEntity<List<InventoryItemsResponse>> findByQuantityGreaterThan(@RequestParam("quantity") BigDecimal quantity){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByQuantityGreaterThan(quantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/by-quantity-less-than")
	public ResponseEntity<List<InventoryItemsResponse>> findByQuantityLessThan(@RequestParam("quantity") BigDecimal quantity){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByQuantityLessThan(quantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/by-item-condition-greater-than")
	public ResponseEntity<List<InventoryItemsResponse>> findByItemConditionGreaterThan(@RequestParam("itemCondition") BigDecimal itemCondition){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByItemConditionGreaterThan(itemCondition);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/by-item-condition-less-than")
	public ResponseEntity<List<InventoryItemsResponse>> findByItemConditionLessThan(@RequestParam("itemCondition") BigDecimal itemCondition){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByItemConditionLessThan(itemCondition);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/item-condition-and-quantity")
	public ResponseEntity<List<InventoryItemsResponse>> findByItemConditionAndQuantity(@RequestParam("itemCondition") BigDecimal itemCondition,
			@RequestParam("quantity") BigDecimal quantity){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByItemConditionAndQuantity(itemCondition, quantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search-calculation/inventory/{inventoryId}")
	public ResponseEntity<List<InventoryItemCalculateResponse>> findInventoryItemsForCalculation(@PathVariable Long inventoryId){
		List<InventoryItemCalculateResponse> responses = inventoryItemsService.findInventoryItemsForCalculation(inventoryId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/items-for-storage-allowed/{inventoryId}")
	public ResponseEntity<List<InventoryItemCalculateResponse>> findItemsForShortageAllowed(@PathVariable Long inventoryId){
		List<InventoryItemCalculateResponse> responses = inventoryItemsService.findItemsForShortageAllowed(inventoryId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/inventory/storage-employee/{storageEmployeeId}")
	public ResponseEntity<List<InventoryItemsResponse>> findByInventory_StorageEmployee_Id(@PathVariable Long storageEmployeeId){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByInventory_StorageEmployee_Id(storageEmployeeId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/inventory/storage-foreman/{storageForemanId}")
	public ResponseEntity<List<InventoryItemsResponse>> findByInventory_StorageForeman_Id(@PathVariable Long storageForemanId){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByInventory_StorageForeman_Id(storageForemanId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/inventory-date")
	public ResponseEntity<List<InventoryItemsResponse>> findByInventoryDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByInventoryDate(date);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/inventory-date-range")
	public ResponseEntity<List<InventoryItemsResponse>> findByInventoryDateBetween(@RequestParam("start")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByInventoryDateBetween(start, end);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/inventory-date-after")
	public ResponseEntity<List<InventoryItemsResponse>> findByInventoryDateAfter(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByInventoryDateAfter(date);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/inventory-date-before")
	public ResponseEntity<List<InventoryItemsResponse>> findByInventoryDateBefore(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByInventoryDateBefore(date);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/inventory-status")
	public ResponseEntity<List<InventoryItemsResponse>> findByInventory_Status(@RequestParam("status") InventoryStatus status){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByInventory_Status(status);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/exists-by-inventory-aligned")
	public ResponseEntity<Boolean> existsByInventory_Aligned(@RequestParam("bool") Boolean aligned){
		Boolean responses = inventoryItemsService.existsByInventory_Aligned(aligned);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/inventory-aligned-false")
	public ResponseEntity<List<InventoryItemsResponse>> findByInventoryAlignedFalse(){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByInventoryAlignedFalse();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/inventory-status-storage-employee/{storageEmployeeId}")
	public ResponseEntity<List<InventoryItemsResponse>> findByInventoryStatusAndInventoryStorageEmployeeId(@RequestParam("status") InventoryStatus status
			,@PathVariable Long storageEmployeeId){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByInventoryStatusAndInventoryStorageEmployeeId(status, storageEmployeeId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/inventory-status-storage-foreman/{storageForemanId}")
	public ResponseEntity<List<InventoryItemsResponse>> findByInventoryStatusAndInventoryStorageForemanId(@RequestParam("status") InventoryStatus status,@PathVariable Long storageForemanId){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByInventoryStatusAndInventoryStorageForemanId(status, storageForemanId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/exists-inventory-aligned-false/{employeeId}")
	public ResponseEntity<Boolean> existsByInventoryAlignedFalseAndInventoryStorageEmployeeId(@PathVariable Long employeeId){
		Boolean responses = inventoryItemsService.existsByInventoryAlignedFalseAndInventoryStorageEmployeeId(employeeId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/items-with-non-zero-difference")
	public ResponseEntity<List<InventoryItemsResponse>> findItemsWithNonZeroDifference(){
		List<InventoryItemsResponse> responses = inventoryItemsService.findItemsWithNonZeroDifference();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/inventory-status-and-aligned-false")
	public ResponseEntity<List<InventoryItemsResponse>> findByInventoryStatusAndInventoryAlignedFalse(@RequestParam("status") InventoryStatus status){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByInventoryStatusAndInventoryAlignedFalse(status);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/inventory-date-and-storage-foreman/{foremanId}")
	public ResponseEntity<List<InventoryItemsResponse>> findByInventoryDateAndInventoryStorageForemanId(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@PathVariable Long foremanId){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByInventoryDateAndInventoryStorageForemanId(date, foremanId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/fetch-inventory-summaries")
	public ResponseEntity<List<InventorySummaryResponse>> fetchInventorySummaries(){
		List<InventorySummaryResponse> responses = inventoryItemsService.fetchInventorySummaries();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-current-quantity")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_CurrentQuantity(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_CurrentQuantity(currentQuantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-current-quantity-greater-than")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_CurrentQuantityGreaterThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_CurrentQuantityGreaterThan(currentQuantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-current-quantity-less-than")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_CurrentQuantityLessThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_CurrentQuantityLessThan(currentQuantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-unit-measure")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_UnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_UnitMeasure(unitMeasure);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-supplier-type")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_SupplierType(@RequestParam("supplierType") SupplierType supplierType){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_SupplierType(supplierType);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-storage-type")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_StorageType(@RequestParam("storageType") StorageType storageType){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_StorageType(storageType);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-goods-type")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_GoodsType(@RequestParam("type") GoodsType type){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_GoodsType(type);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product/storage/{storageId}")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_StorageId(@PathVariable Long storageId){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_StorageId(storageId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-storage-name")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_StorageNameContainingIgnoreCase(@RequestParam("storageName") String storageName){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_StorageNameContainingIgnoreCase(storageName);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-storage-location")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_StorageLocationContainingIgnoreCase(@RequestParam("storageLocation") String storageLocation){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_StorageLocationContainingIgnoreCase(storageLocation);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-storage-capacity")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_StorageCapacity(@RequestParam("capacity") BigDecimal capacity){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_StorageCapacity(capacity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-storage-capacity-greater-than")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_StorageCapacityGreaterThan(@RequestParam("capacity") BigDecimal capacity){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_StorageCapacityGreaterThan(capacity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-storage-capacity-less-than")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_StorageCapacityLessThan(@RequestParam("capacity") BigDecimal capacity){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_StorageCapacityLessThan(capacity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-storage-status")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_Storage_Status(@RequestParam("status") StorageStatus status){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_Storage_Status(status);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/used-capacity-for-items")
	public ResponseEntity<List<InventoryItemStorageCapacityResponse>> fetchUsedCapacitiesForItems(){
		List<InventoryItemStorageCapacityResponse> responses = inventoryItemsService.fetchUsedCapacitiesForItems();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/fetch-all-storage-capacities")
	public ResponseEntity<List<StorageCapacityResponse>> fetchAllStorageCapacities(){
		List<StorageCapacityResponse> responses = inventoryItemsService.fetchAllStorageCapacities();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/fetch-item-quantities-per-storage")
	public ResponseEntity<List<StorageItemSummaryResponse>> fetchItemQuantitiesPerStorage(){
		List<StorageItemSummaryResponse> responses = inventoryItemsService.fetchItemQuantitiesPerStorage();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/fetch-storage-capacity-summary")
	public ResponseEntity<List<StorageCapacityAndInventorySummaryResponseFull>> fetchStorageCapacityAndInventorySummary(){
		List<StorageCapacityAndInventorySummaryResponseFull> responses = inventoryItemsService.fetchStorageCapacityAndInventorySummary();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/fetch-detailed-storage-stats")
	public ResponseEntity<List<StorageCapacityAndInventorySummaryResponseFull>> fetchDetailedStorageStats(){
		List<StorageCapacityAndInventorySummaryResponseFull> responses = inventoryItemsService.fetchDetailedStorageStats();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product/supply/{supplyId}")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_SupplyId(@PathVariable Long supplyId){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_SupplyId(supplyId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-supply-quantity")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_SupplyQuantity(@RequestParam("quantity") BigDecimal quantity){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_SupplyQuantity(quantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-supply-quantity-greater-than")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_SupplyQuantityGreaterThan(@RequestParam("quantity") BigDecimal quantity){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_SupplyQuantityGreaterThan(quantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-supply-quantity-less-than")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_SupplyQuantityLessThan(@RequestParam("quantity") BigDecimal quantity){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_SupplyQuantityLessThan(quantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-supply-updates")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_SupplyUpdates(@RequestParam("updates") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updates){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_SupplyUpdates(updates);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-supply-update-range")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_SupplyUpdatesBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_SupplyUpdatesBetween(start, end);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product/supply/{storageId}")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_SupplyStorageId(@PathVariable Long storageId){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_SupplyStorageId(storageId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product/shelf/{shelfId}")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_ShelfId(@PathVariable Long shelfId){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_ShelfId(shelfId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-shelf-row-count")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_ShelfRowCount(@RequestParam("rowCount") Integer rowCount){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_ShelfRowCount(rowCount);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-shelf-cols")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_ShelfCols(@RequestParam("cols") Integer cols){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_ShelfCols(cols);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/inventory-items-without-shelf")
	public ResponseEntity<List<InventoryItemsResponse>> findInventoryItemsWithoutShelf(){
		List<InventoryItemsResponse> responses = inventoryItemsService.findInventoryItemsWithoutShelf();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/search/product-shelf-is-not-null")
	public ResponseEntity<List<InventoryItemsResponse>> findByProduct_ShelfIsNotNull(){
		List<InventoryItemsResponse> responses = inventoryItemsService.findByProduct_ShelfIsNotNull();
		return ResponseEntity.ok(responses);
	}
	
	//nove metode
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/track-inventory-items/{id}")
	public ResponseEntity<InventoryItemsResponse> trackInventoryItems(@PathVariable Long id){
		InventoryItemsResponse items = inventoryItemsService.trackInventoryItems(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/track-by-inventory/{inventoryId}")
	public ResponseEntity<InventoryItemsResponse> trackInventoryItemsByInventory(@PathVariable Long inventoryId){
		InventoryItemsResponse items = inventoryItemsService.trackInventoryItemsByInventory(inventoryId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/track-by-product/{productId}")
	public ResponseEntity<InventoryItemsResponse> trackInventoryItemsByProduct(@PathVariable Long productId){
		InventoryItemsResponse items = inventoryItemsService.trackInventoryItemsByProduct(productId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_FULL_ACCESS)
	@PostMapping("/{id}/confirm")
	public ResponseEntity<InventoryItemsResponse> confirmInventoryItems(@PathVariable Long id){
		InventoryItemsResponse items = inventoryItemsService.confirmInventoryItems(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_FULL_ACCESS)
	@PostMapping("/{id}/cancel")
	public ResponseEntity<InventoryItemsResponse> cancelInventoryItems(@PathVariable Long id){
		InventoryItemsResponse items = inventoryItemsService.cancelInventoryItems(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_FULL_ACCESS)
	@PostMapping("/{id}/close")
	public ResponseEntity<InventoryItemsResponse> closeInventoryItems(@PathVariable Long id){
		InventoryItemsResponse items = inventoryItemsService.closeInventoryItems(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_FULL_ACCESS)
	@PostMapping("/{id}/status/{status}")
	public ResponseEntity<InventoryItemsResponse> changeStatus(@PathVariable Long id,@PathVariable  InventoryItemsStatus status){
		InventoryItemsResponse items = inventoryItemsService.changeStatus(id, status);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/count/quantity-by-product")
	public ResponseEntity<List<QuantityByProductStatDTO>> countQuantityByProduct(){
		List<QuantityByProductStatDTO> items = inventoryItemsService.countQuantityByProduct();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/count/item-condition-by-product")
	public ResponseEntity<List<ItemConditionByProductStatDTO>> countItemConditionByProduct(){
		List<ItemConditionByProductStatDTO> items = inventoryItemsService.countItemConditionByProduct();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_READ_ACCESS)
	@GetMapping("/stats/total-difference/{productId}")
	public ResponseEntity<BigDecimal> getTotalDifferenceByProduct(@PathVariable Long productId){
		BigDecimal items = inventoryItemsService.getTotalDifferenceByProduct(productId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_FULL_ACCESS)
	@PostMapping("/stats/inventory")
	public ResponseEntity<InventoryStatResponse> getInventoryStatistics(@RequestBody InventoryStatRequest request){
		InventoryStatResponse items = inventoryItemsService.getInventoryStatistics(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_FULL_ACCESS)
	@PostMapping("/save")
	public ResponseEntity<InventoryItemsResponse> saveInventoryItems(@Valid @RequestBody InventoryItemsRequest request){
		InventoryItemsResponse items = inventoryItemsService.saveInventoryItems(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_FULL_ACCESS)
	@PostMapping("/save-as")
	public ResponseEntity<InventoryItemsResponse> saveAs(@Valid @RequestBody InventoryItemsSaveAsRequest request){
		InventoryItemsResponse items = inventoryItemsService.saveAs(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_FULL_ACCESS)
	@PostMapping("/save-all")
	public ResponseEntity<List<InventoryItemsResponse>> saveAll(@Valid @RequestBody List<InventoryItemsRequest> requests){
		List<InventoryItemsResponse> items = inventoryItemsService.saveAll(requests);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_ITEMS_FULL_ACCESS)
	@PostMapping("/general-search")
	public ResponseEntity<List<InventoryItemsResponse>> generalSearch(@RequestBody InventoryItemsSearchRequest request){
		List<InventoryItemsResponse> items = inventoryItemsService.generalSearch(request);
		return ResponseEntity.ok(items);
	}
}
