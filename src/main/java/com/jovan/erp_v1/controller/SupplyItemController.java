package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
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

import com.jovan.erp_v1.dto.AvgCostByProcurementResponse;
import com.jovan.erp_v1.dto.ProcurementGlobalStatsResponse;
import com.jovan.erp_v1.dto.ProcurementStatsPerEntityResponse;
import com.jovan.erp_v1.dto.SumCostGroupedByProcurementResponse;
import com.jovan.erp_v1.dto.SupplierItemCountResponse;
import com.jovan.erp_v1.dto.SupplyItemStatsResponse;
import com.jovan.erp_v1.request.CostSumByProcurement;
import com.jovan.erp_v1.request.SupplyItemRequest;
import com.jovan.erp_v1.response.SupplyItemResponse;
import com.jovan.erp_v1.service.ISupplyItemService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suppliesItems")
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
public class SupplyItemController {

	private final ISupplyItemService supplyItemService;
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_FULL_ACCESS)
	@PostMapping("/create/new-supply-item")
	public ResponseEntity<SupplyItemResponse> create(@Valid @RequestBody SupplyItemRequest request){
		SupplyItemResponse response = supplyItemService.createSupplyItem(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<SupplyItemResponse> update(@PathVariable Long id, @Valid @RequestBody SupplyItemRequest request){
		SupplyItemResponse response = supplyItemService.updateSupplyItem(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		supplyItemService.deleteSupplyItem(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/supplyItem/{id}")
	public ResponseEntity<SupplyItemResponse> getOne(@PathVariable Long id){
		SupplyItemResponse response = supplyItemService.getOneById(id);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/get-all-supplies-items")
	public ResponseEntity<List<SupplyItemResponse>> getAllSuppliesItems(){
		List<SupplyItemResponse> responses = supplyItemService.getAllSupplyItem();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/procurement/{procurementId}")
	public ResponseEntity<List<SupplyItemResponse>> getByProcurementId(@PathVariable Long procurementId){
		List<SupplyItemResponse> responses = supplyItemService.getByProcurementId(procurementId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/supplier/{supplierId}")
	public ResponseEntity<List<SupplyItemResponse>> getBySupplierId(@PathVariable Long supplierId){
		List<SupplyItemResponse> responses = supplyItemService.getBySupplierId(supplierId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/cost-between")
	public ResponseEntity<List<SupplyItemResponse>> getByCostBetween(@RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max){
		List<SupplyItemResponse> responses = supplyItemService
				.getByCostBetween(min, max);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/procurement-date-between")
	public ResponseEntity<List<SupplyItemResponse>> getByProcurementDateBetween(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		List<SupplyItemResponse> responses = supplyItemService.getByProcurementDateBetween(startDate, endDate);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/date-cost-between")
	public ResponseEntity<List<SupplyItemResponse>> getByProcurementDateAndCostBetween(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max){
		List<SupplyItemResponse> responses = supplyItemService.getByProcurementDateAndCostBetween(startDate, endDate, min, max);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/by-procurement-vendor/{procurementId}/{vendorId}")
	public ResponseEntity<List<SupplyItemResponse>> getByProcurementAndVendor(@PathVariable Long procurementId, @PathVariable Long vendorId){
		List<SupplyItemResponse> responses = supplyItemService.getByProcurementAndVendor(procurementId, vendorId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/by-supplier-procurement-cost/{supplierId}/{procurementId}")
	public ResponseEntity<List<SupplyItemResponse>> getByVendorAndProcurementAndCost(
	        @PathVariable Long supplierId,
	        @PathVariable Long procurementId,
	        @RequestParam("minCost") BigDecimal minCost){
		List<SupplyItemResponse> responses = supplyItemService.getByVendorAndProcurementAndCost(supplierId, procurementId, minCost);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/date-cost")
	public ResponseEntity<List<SupplyItemResponse>> getByDateAndCost(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max
			){
		List<SupplyItemResponse> responses = supplyItemService.getByDateAndCost(startDate, endDate, min, max);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/filter-by-supplier-date-cost")
	public ResponseEntity<List<SupplyItemResponse>> getBySupplierNameAndProcurementDateAndMaxCost(
			@RequestParam("supplierName") String supplierName, 
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam("max") BigDecimal max
			){
		List<SupplyItemResponse> responses = supplyItemService.getBySupplierNameAndProcurementDateAndMaxCost(supplierName, startDate, endDate, max);
		return ResponseEntity.ok(responses);
	}
	
	//nove metode
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/supplier-name")
	public ResponseEntity<List<SupplyItemResponse>> findBySupplier_NameContainingIgnoreCase(@RequestParam("supplierName") String supplierName){
		List<SupplyItemResponse> responses = supplyItemService.findBySupplier_NameContainingIgnoreCase(supplierName);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/supplier-phone-number")
	public ResponseEntity<List<SupplyItemResponse>> findBySupplier_PhoneNumberLikeIgnoreCase(@RequestParam("phoneNumber") String phoneNumber){
		List<SupplyItemResponse> responses = supplyItemService.findBySupplier_PhoneNumberLikeIgnoreCase(phoneNumber);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/supplier-email")
	public ResponseEntity<List<SupplyItemResponse>> findBySupplier_EmailLikeIgnoreCase(@RequestParam("email") String email){
		List<SupplyItemResponse> responses = supplyItemService.findBySupplier_EmailLikeIgnoreCase(email);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/supplier-address")
	public ResponseEntity<List<SupplyItemResponse>> findBySupplier_Address(@RequestParam("address") String address){
		List<SupplyItemResponse> responses = supplyItemService.findBySupplier_Address(address);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/supplier-name-cost-range")
	public ResponseEntity<List<SupplyItemResponse>> findBySupplierNameContainingAndCostBetween(@RequestParam("supplierName") String supplierName,
			@RequestParam("minCost") BigDecimal minCost,@RequestParam("maxCost") BigDecimal maxCost){
		List<SupplyItemResponse> responses = supplyItemService.findBySupplierNameContainingAndCostBetween(supplierName, minCost, maxCost);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/supplier-name-procurement-date-range")
	public ResponseEntity<List<SupplyItemResponse>> findBySupplierNameAndProcurementDateBetween(@RequestParam("supplierName") String supplierName,
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<SupplyItemResponse> responses = supplyItemService.findBySupplierNameAndProcurementDateBetween(supplierName, start, end);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/address-min-cost")
	public ResponseEntity<List<SupplyItemResponse>> findByAddressAndMinCost(@RequestParam("address") String address,@RequestParam("minCost") BigDecimal minCost){
		List<SupplyItemResponse> responses = supplyItemService.findByAddressAndMinCost(address, minCost);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/phone-number-cost")
	public ResponseEntity<List<SupplyItemResponse>> findByPhoneNumberAndCost(@RequestParam("phoneNumber") String phoneNumber,@RequestParam("cost") BigDecimal cost){
		List<SupplyItemResponse> responses = supplyItemService.findByPhoneNumberAndCost(phoneNumber, cost);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/procurement-supply-item-count")
	public ResponseEntity<List<SupplyItemResponse>> findByProcurementSupplyItemCount(@RequestParam("count") Integer count){
		List<SupplyItemResponse> responses = supplyItemService.findByProcurementSupplyItemCount(count);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/by-cost")
	public ResponseEntity<List<SupplyItemResponse>> findByCost(@RequestParam("cost") BigDecimal cost){
		List<SupplyItemResponse> responses = supplyItemService.findByCost(cost);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/cost-greater-than")
	public ResponseEntity<List<SupplyItemResponse>> findByCostGreaterThan(@RequestParam("cost") BigDecimal cost){
		List<SupplyItemResponse> responses = supplyItemService.findByCostGreaterThan(cost);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/cost-less-than")
	public ResponseEntity<List<SupplyItemResponse>> findByCostLessThan(@RequestParam("cost") BigDecimal cost){
		List<SupplyItemResponse> responses = supplyItemService.findByCostLessThan(cost);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/procurement/total-cost-greater-than")
	public ResponseEntity<List<SupplyItemResponse>> findByProcurementTotalCostGreaterThan(@RequestParam("minCost") BigDecimal minCost){
		List<SupplyItemResponse> responses = supplyItemService.findByProcurementTotalCostGreaterThan(minCost);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/procurement-date")
	public ResponseEntity<List<SupplyItemResponse>> findByProcurementDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		List<SupplyItemResponse> responses = supplyItemService.findByProcurementDate(date);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/supply-sales-count-mismatch")
	public ResponseEntity<List<SupplyItemResponse>> findBySupplyAndSalesCountMismatch(){
		List<SupplyItemResponse> responses = supplyItemService.findBySupplyAndSalesCountMismatch();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/procurement-with-supply-cost-over")
	public ResponseEntity<List<SupplyItemResponse>> findByProcurementWithSupplyCostOver(@RequestParam("minCount") BigDecimal minTotal){
		List<SupplyItemResponse> responses = supplyItemService.findByProcurementWithSupplyCostOver(minTotal);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/supplier-wuth-more-than-n-times")
	public ResponseEntity<List<SupplyItemResponse>> findBySupplierWithMoreThanNItems(@RequestParam("minCount") BigDecimal minCount){
		List<SupplyItemResponse> responses = supplyItemService.findBySupplierWithMoreThanNItems(minCount);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/sum-cost-group-by-procurement")
	public ResponseEntity<List<SumCostGroupedByProcurementResponse>> sumCostGroupedByProcurement(){
		List<SumCostGroupedByProcurementResponse> responses = supplyItemService.sumCostGroupedByProcurement();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/count-by-supplier")
	public ResponseEntity<List<SupplierItemCountResponse>> countBySupplier(){
		List<SupplierItemCountResponse> responses = supplyItemService.countBySupplier();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/avg-cost-by-procurement")
	public ResponseEntity<List<AvgCostByProcurementResponse>> avgCostByProcurement(){
		List<AvgCostByProcurementResponse> responses = supplyItemService.avgCostByProcurement();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/procurement-per-entity-stats")
	public ResponseEntity<List<ProcurementStatsPerEntityResponse>> procurementPerEntityStats(){
		List<ProcurementStatsPerEntityResponse> responses = supplyItemService.procurementPerEntityStats();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/cost-sum-grouped-by-procurement")
	public ResponseEntity<List<CostSumByProcurement>> findCostSumGroupedByProcurement(){
		List<CostSumByProcurement> responses = supplyItemService.findCostSumGroupedByProcurement();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/cost-sum-by-procurement-with-min-total")
	public ResponseEntity<List<CostSumByProcurement>> findCostSumGroupedByProcurementWithMinTotal(@RequestParam("minTotal") BigDecimal minTotal){
		List<CostSumByProcurement> responses = supplyItemService.findCostSumGroupedByProcurementWithMinTotal(minTotal);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/procurement-stats")
	public ResponseEntity<ProcurementGlobalStatsResponse> procurementStats(){
		ProcurementGlobalStatsResponse responses = supplyItemService.procurementStats();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/count-all-supply-items")
	public ResponseEntity<SupplyItemStatsResponse> countAllSupplyItems(){
		SupplyItemStatsResponse responses = supplyItemService.countAllSupplyItems();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/count-by-procurement/{procurementId}")
	public ResponseEntity<SupplyItemStatsResponse> countByProcurementId(@PathVariable Long procurementId){
		SupplyItemStatsResponse responses = supplyItemService.countByProcurementId(procurementId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/sum-all-costs")
	public ResponseEntity<SupplyItemStatsResponse> sumAllCosts(){
		SupplyItemStatsResponse responses = supplyItemService.sumAllCosts();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/average-cost")
	public ResponseEntity<SupplyItemStatsResponse> averageCost(){
		SupplyItemStatsResponse responses = supplyItemService.averageCost();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/min-cost")
	public ResponseEntity<SupplyItemStatsResponse> minCost(){
		SupplyItemStatsResponse responses = supplyItemService.minCost();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SUPPLY_ITEM_READ_ACCESS)
	@GetMapping("/search/max-cost")
	public ResponseEntity<SupplyItemStatsResponse> maxCost(){
		SupplyItemStatsResponse responses = supplyItemService.maxCost();
		return ResponseEntity.ok(responses);
	}
}

