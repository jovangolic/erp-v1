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

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.mapper.SupplyMapper;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.SupplyRequest;
import com.jovan.erp_v1.response.StorageGoodsCountResponse;
import com.jovan.erp_v1.response.StorageIncomingMovementCountResponse;
import com.jovan.erp_v1.response.StorageIncomingTransferCountResponse;
import com.jovan.erp_v1.response.StorageMaterialCountResponse;
import com.jovan.erp_v1.response.StorageOutgoingMovementCountResponse;
import com.jovan.erp_v1.response.StorageOutgoingTransferCountResponse;
import com.jovan.erp_v1.response.StorageShelfCountResponse;
import com.jovan.erp_v1.response.StorageShipmentCountResponse;
import com.jovan.erp_v1.response.StorageWorkCenterCountResponse;
import com.jovan.erp_v1.response.SupplyResponse;
import com.jovan.erp_v1.service.ISupplyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supplies")
@CrossOrigin("http://localhost:5173")
public class SupplyController {

	private final ISupplyService supplyService;
	private final SupplyMapper supplyMapper;
	private final StorageRepository storageRepository;

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PostMapping("/create/new-supply")
	public ResponseEntity<SupplyResponse> createSupply(@Valid @RequestBody SupplyRequest request) {
		SupplyResponse response = supplyService.createSupply(request);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PutMapping("/update/{id}")
	public ResponseEntity<SupplyResponse> updateSupply(@PathVariable Long id,
			@Valid @RequestBody SupplyRequest request) {
		SupplyResponse response = supplyService.updateSupply(id, request);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteSupply(@PathVariable Long id) {
		supplyService.deleteSupply(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/supply/{supplyId}")
	public ResponseEntity<SupplyResponse> getBySupplyId(@PathVariable Long supplyId) {
		SupplyResponse response = supplyService.getBySupplyId(supplyId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-all-supplies")
	public ResponseEntity<List<SupplyResponse>> getAllSupplies() {
		List<SupplyResponse> responses = supplyService.getAllSupply();
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/supply/by-goods-name")
	public ResponseEntity<List<SupplyResponse>> getBySuppliesByGoodsName(@RequestParam("name") String name) {
		List<SupplyResponse> responses = supplyService.getBySuppliesByGoodsName(name);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/supply/by-minQuantity")
	public ResponseEntity<List<SupplyResponse>> getBySuppliesWithMinQuantity(
			@RequestParam("minQuantity") BigDecimal minQuantity) {
		List<SupplyResponse> responses = supplyService.getBySuppliesWithMinQuantity(minQuantity);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/supply/storageId/{storageId}")
	public ResponseEntity<List<SupplyResponse>> getBySuppliesByStorageId(@PathVariable Long storageId) {
		List<SupplyResponse> responses = supplyService.getBySuppliesByStorageId(storageId);
		return ResponseEntity.ok(responses);
	}
	
	//nove metode
	
	@GetMapping("/updates-range")
	public ResponseEntity<List<SupplyResponse>> findByUpdatesBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<SupplyResponse> responses = supplyService.findByUpdatesBetween(start, end);
		return ResponseEntity.ok(responses);		
	}
	
	@GetMapping("/search/storage-name")
	public ResponseEntity<List<SupplyResponse>> findByStorage_NameContainingIgnoreCase(@RequestParam("name") String name){
		List<SupplyResponse> responses = supplyService.findByStorage_NameContainingIgnoreCase(name);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-location")
	public ResponseEntity<List<SupplyResponse>> findByStorage_LocationContainingIgnoreCase(@RequestParam("location") String location){
		List<SupplyResponse> responses = supplyService.findByStorage_LocationContainingIgnoreCase(location);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-capacity")
	public ResponseEntity<List<SupplyResponse>> findByStorage_Capacity(@RequestParam("capacity") BigDecimal capacity){
		List<SupplyResponse> responses = supplyService.findByStorage_Capacity(capacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-capacity-greater-than")
	public ResponseEntity<List<SupplyResponse>> findByStorage_CapacityGreaterThan(@RequestParam("capacity") BigDecimal capacity){
		List<SupplyResponse> responses = supplyService.findByStorage_CapacityGreaterThan(capacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-capacity-less-than")
	public ResponseEntity<List<SupplyResponse>> findByStorage_CapacityLessThan(@RequestParam("capacity") BigDecimal capacity){
		List<SupplyResponse> responses = supplyService.findByStorage_CapacityLessThan(capacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-type")
	public ResponseEntity<List<SupplyResponse>> findByStorage_Type(@RequestParam("type") StorageType type){
		List<SupplyResponse> responses = supplyService.findByStorage_Type(type);
		return ResponseEntity.ok(responses);
	}
		
	@GetMapping("/search/storage-status")
	public ResponseEntity<List<SupplyResponse>> findByStorage_Status(@RequestParam("status") StorageStatus status){
		List<SupplyResponse> responses = supplyService.findByStorage_Status(status);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-type-and-capacity")
	public ResponseEntity<List<SupplyResponse>> findByStorage_Type_AndCapacity(@RequestParam("type") StorageType type, @RequestParam("capacity")BigDecimal capacity){
		List<SupplyResponse> responses = supplyService.findByStorage_Type_AndCapacity(type, capacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-type-and-capacuty-greater-than")
	public ResponseEntity<List<SupplyResponse>> findByStorage_Type_AndCapacityGreaterThan(@RequestParam("type")StorageType type,@RequestParam("capacity") BigDecimal capacity){
		List<SupplyResponse> responses = supplyService.findByStorage_Type_AndCapacityGreaterThan(type, capacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-type-and-capacity-less-than")
	public ResponseEntity<List<SupplyResponse>> findByStorage_Type_AndCapacityLessThan(@RequestParam("type")StorageType type,@RequestParam("capacity") BigDecimal capacity){
		List<SupplyResponse> responses = supplyService.findByStorage_Type_AndCapacityLessThan(type, capacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/type-and-status")
	public ResponseEntity<List<SupplyResponse>> findByStorage_Type_AndStatus(@RequestParam("type")StorageType type, @RequestParam("status")StorageStatus status){
		List<SupplyResponse> responses = supplyService.findByStorage_Type_AndStatus(type, status);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-type-location")
	public ResponseEntity<List<SupplyResponse>> findByStorage_Type_AndLocation(@RequestParam("type")StorageType type,@RequestParam("location") String location){
		List<SupplyResponse> responses = supplyService.findByStorage_Type_AndLocation(type, location);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-location-capacity")
	public ResponseEntity<List<SupplyResponse>> findByStorage_Location_AndCapacity(@RequestParam("location")String location,@RequestParam("capacity") BigDecimal capacity){
		List<SupplyResponse> responses = supplyService.findByStorage_Location_AndCapacity(location, capacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-location-capacity-greater-than")
	public ResponseEntity<List<SupplyResponse>> findByStorage_Location_AndCapacityGreaterThan(@RequestParam("location")String location,@RequestParam("capacity") BigDecimal capacity){
		List<SupplyResponse> responses = supplyService.findByStorage_Location_AndCapacityGreaterThan(location, capacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-location-capacity-less-than")
	public ResponseEntity<List<SupplyResponse>> findByStorage_Location_AndCapacityLessThan(@RequestParam("location") String location,@RequestParam("capacity") BigDecimal capacity){
		List<SupplyResponse> responses = supplyService.findByStorage_Location_AndCapacityLessThan(location, capacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-capacity-range")
	public ResponseEntity<List<SupplyResponse>> findByStorage_CapacityBetween(@RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max){
		List<SupplyResponse> responses = supplyService.findByStorage_CapacityBetween(min, max);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-with-min-goods-count")
	public ResponseEntity<List<SupplyResponse>> findByStorageWithMinGoodsCount(@RequestParam("minGoodsCount") Integer minGoodsCount){
		List<SupplyResponse> responses = supplyService.findByStorageWithMinGoodsCount(minGoodsCount);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-with-max-goods-count")
	public ResponseEntity<List<SupplyResponse>> findByStorageWithMaxGoodsCount(@RequestParam("maxGoodsCount") Integer maxGoodsCount){
		List<SupplyResponse> responses = supplyService.findByStorageWithMaxGoodsCount(maxGoodsCount);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-contain-material-name")
	public ResponseEntity<List<SupplyResponse>> findByStorageContainingMaterial(@RequestParam("name") String name){
		List<SupplyResponse> responses = supplyService.findByStorageContainingMaterial(name);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage/shlef-capacity-in")
	public ResponseEntity<List<SupplyResponse>> findByShelfCapacityInStorage(@RequestParam("minShelfCapacity") BigDecimal minShelfCapacity){
		List<SupplyResponse> responses = supplyService.findByShelfCapacityInStorage(minShelfCapacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-used-as-transfer-origin")
	public ResponseEntity<List<SupplyResponse>> findByStorageUsedAsTransferOrigin(){
		List<SupplyResponse> responses = supplyService.findByStorageUsedAsTransferOrigin();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/supplies-with-workCenter")
	public ResponseEntity<List<SupplyResponse>> findSuppliesWithWorkCenters(){
		List<SupplyResponse> responses = supplyService.findSuppliesWithWorkCenters();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-used-capacity-greater-than")
	public ResponseEntity<List<SupplyResponse>> findByStorage_UsedCapacityGreaterThan(@RequestParam("usedCapacity") BigDecimal usedCapacity){
		List<SupplyResponse> responses = supplyService.findByStorage_UsedCapacityGreaterThan(usedCapacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-used-capacity-less-than")
	public ResponseEntity<List<SupplyResponse>> findByStorage_UsedCapacityLessThan(@RequestParam("usedCapacity") BigDecimal usedCapacity){
		List<SupplyResponse> responses = supplyService.findByStorage_UsedCapacityLessThan(usedCapacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-with-empty-shelves")
	public ResponseEntity<List<SupplyResponse>> findByStorageWithEmptyShelves(){
		List<SupplyResponse> responses = supplyService.findByStorageWithEmptyShelves();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/storage-used-as-transfer-destination")
	public ResponseEntity<List<SupplyResponse>> findByStorageUsedAsTransferDestination(){
		List<SupplyResponse> responses = supplyService.findByStorageUsedAsTransferDestination();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/production-storage")
	public ResponseEntity<List<SupplyResponse>> findProductionStorage(){
		List<SupplyResponse> responses = supplyService.findProductionStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/distribution-storage")
	public ResponseEntity<List<SupplyResponse>> findDistributionStorage(){
		List<SupplyResponse> responses = supplyService.findDistributionStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/open-storage")
	public ResponseEntity<List<SupplyResponse>> findOpenStorage(){
		List<SupplyResponse> responses = supplyService.findOpenStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/closed-storage")
	public ResponseEntity<List<SupplyResponse>> findClosedStorage(){
		List<SupplyResponse> responses = supplyService.findClosedStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/interim-storage")
	public ResponseEntity<List<SupplyResponse>> findInterimStorage(){
		List<SupplyResponse> responses = supplyService.findInterimStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/available-storage")
	public ResponseEntity<List<SupplyResponse>> findAvailableStorage(){
		List<SupplyResponse> responses = supplyService.findAvailableStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/active-storage")
	public ResponseEntity<List<SupplyResponse>> findActiveStorage(){
		List<SupplyResponse> responses = supplyService.findActiveStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/under-maintenance-storage")
	public ResponseEntity<List<SupplyResponse>> findUnderMaintenanceStorage(){
		List<SupplyResponse> responses = supplyService.findUnderMaintenanceStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/decommissioned-storage")
	public ResponseEntity<List<SupplyResponse>> findDecommissionedStorage(){
		List<SupplyResponse> responses = supplyService.findDecommissionedStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/reserved-storage")
	public ResponseEntity<List<SupplyResponse>> findReservedStorage(){
		List<SupplyResponse> responses = supplyService.findReservedStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/temporary-storage")
	public ResponseEntity<List<SupplyResponse>> findTemporaryStorage(){
		List<SupplyResponse> responses = supplyService.findTemporaryStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/full-storage")
	public ResponseEntity<List<SupplyResponse>> findFullStorage(){
		List<SupplyResponse> responses = supplyService.findFullStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/count-goods-per-storage")
	public ResponseEntity<List<StorageGoodsCountResponse>> countGoodsPerStorage(){
		List<StorageGoodsCountResponse> responses = supplyService.countGoodsPerStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/count-shelves-per-storage")
	public ResponseEntity<List<StorageShelfCountResponse>> countShelvesPerStorage(){
		List<StorageShelfCountResponse> responses = supplyService.countShelvesPerStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/count-outgoing-shipments-per-storage")
	public ResponseEntity<List<StorageShipmentCountResponse>> countOutgoingShipmentsPerStorage(){
		List<StorageShipmentCountResponse> responses = supplyService.countOutgoingShipmentsPerStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/count-outgoing-transfers-per-storage")
	public ResponseEntity<List<StorageOutgoingTransferCountResponse>> countOutgoingTransfersPerStorage(){
		List<StorageOutgoingTransferCountResponse> responses = supplyService.countOutgoingTransfersPerStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/count-incoming-transfers-per-storage")
	public ResponseEntity<List<StorageIncomingTransferCountResponse>> countIncomingTransfersPerStorage(){
		List<StorageIncomingTransferCountResponse> responses = supplyService.countIncomingTransfersPerStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/count-materials-per-storage")
	public ResponseEntity<List<StorageMaterialCountResponse>> countMaterialsPerStorage(){
		List<StorageMaterialCountResponse> responses = supplyService.countMaterialsPerStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/count-outgoing-material-movements-per-storage")
	public ResponseEntity<List<StorageOutgoingMovementCountResponse>> countOutgoingMaterialMovementsPerStorage(){
		List<StorageOutgoingMovementCountResponse> responses = supplyService.countOutgoingMaterialMovementsPerStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/count-incoming-material-movements-per-storage")
	public ResponseEntity<List<StorageIncomingMovementCountResponse>> countIncomingMaterialMovementsPerStorage(){
		List<StorageIncomingMovementCountResponse> responses = supplyService.countIncomingMaterialMovementsPerStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/count-workCenters-per-storage")
	public ResponseEntity<List<StorageWorkCenterCountResponse>> countWorkCentersPerStorage(){
		List<StorageWorkCenterCountResponse> responses = supplyService.countWorkCentersPerStorage();
		return ResponseEntity.ok(responses);
	}
}
