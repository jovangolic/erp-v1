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
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.request.StorageRequest;
import com.jovan.erp_v1.response.StorageResponse;
import com.jovan.erp_v1.service.IStorageService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/storages")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
public class StorageController {

	private final IStorageService storageService;

	@PreAuthorize(RoleGroups.STORAGE_FULL_ACCESS)
	@PostMapping("/create/new-storage")
	public ResponseEntity<StorageResponse> createStorage(@Valid @RequestBody StorageRequest request) {
		StorageResponse response = storageService.createStorage(request);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize(RoleGroups.STORAGE_FULL_ACCESS)
	@PutMapping("/update/{storageId}")
	public ResponseEntity<StorageResponse> updateStorage(@PathVariable Long storageId,
			@Valid @RequestBody StorageRequest request) {
		StorageResponse updated = storageService.updateStorage(storageId, request);
		return ResponseEntity.ok(updated);
	}

	@PreAuthorize(RoleGroups.STORAGE_FULL_ACCESS)
	@DeleteMapping("/delete/{storageId}")
	public ResponseEntity<Void> deleteStorage(@PathVariable Long storageId) {
		storageService.deleteStorage(storageId);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/get-by-storage-type")
	public ResponseEntity<List<StorageResponse>> getByStorageType(@RequestParam("storageType") StorageType type) {
		List<StorageResponse> responses = storageService.getByStorageType(type);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/find-one/{storageId}")
	public ResponseEntity<StorageResponse> getByStorageId(@PathVariable Long storageId) {
		StorageResponse response = storageService.getByStorageId(storageId);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/by-name")
	public ResponseEntity<List<StorageResponse>> getByStorageName(@RequestParam("name") String name) {
		List<StorageResponse> responses = storageService.getByName(name);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/by-location")
	public ResponseEntity<List<StorageResponse>> getByStorageLocation(@RequestParam("location") String location) {
		List<StorageResponse> responses = storageService.getByLocation(location);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/by-capacity")
	public ResponseEntity<List<StorageResponse>> getByStorageCapacity(@RequestParam("capacity") BigDecimal capacity) {
		List<StorageResponse> responses = storageService.getByCapacity(capacity);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/by-name-and-location")
	public ResponseEntity<List<StorageResponse>> getStorageByNameAndLocation(@RequestParam("name") String name,
			@RequestParam("location") String location) {
		List<StorageResponse> responses = storageService.getByNameAndLocation(name, location);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/by-type-and-capacity")
	public ResponseEntity<List<StorageResponse>> getByTypeAndCapacityGreaterThan(@RequestParam("type") StorageType type,
			@RequestParam("capacity") BigDecimal capacity) {
		List<StorageResponse> responses = storageService.getByTypeAndCapacityGreaterThan(type, capacity);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/by-minCount")
	public ResponseEntity<List<StorageResponse>> getStoragesWithMinGoods(@RequestParam("minCount") Integer minCount) {
		List<StorageResponse> responses = storageService.getStoragesWithMinGoods(minCount);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/ignore-case")
	public ResponseEntity<List<StorageResponse>> getByNameContainingIgnoreCase(@RequestParam("name") String name) {
		List<StorageResponse> responses = storageService.getByNameContainingIgnoreCase(name);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/find-all")
	public ResponseEntity<List<StorageResponse>> getAllStorage() {
		List<StorageResponse> responses = storageService.getAllStorage();
		return ResponseEntity.ok(responses);
	}
	
	//nove metode
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storage-type-and-capacity-less-than")
	public ResponseEntity<List<StorageResponse>> findByTypeAndCapacityLessThan(@RequestParam("type") StorageType type,@RequestParam("capacity") BigDecimal capacity){
		List<StorageResponse> responses = storageService.findByTypeAndCapacityLessThan(type, capacity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/capacity-greater-than")
	public ResponseEntity<List<StorageResponse>> findByCapacityGreaterThan(@RequestParam("capacity")BigDecimal capacity){
		List<StorageResponse> responses = storageService.findByCapacityGreaterThan(capacity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/capacity-less-than")
	public ResponseEntity<List<StorageResponse>> findByCapacityLessThan(@RequestParam("capacity") BigDecimal capacity){
		List<StorageResponse> responses = storageService.findByCapacityLessThan(capacity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storage-name-location-capacity")
	public ResponseEntity<List<StorageResponse>> findByNameAndLocationAndCapacity(@RequestParam("name") String name,@RequestParam("location") String location,
			@RequestParam("capacity") BigDecimal capacity){
		List<StorageResponse> responses = storageService.findByNameAndLocationAndCapacity(name, location, capacity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storage-type-and-location")
	public ResponseEntity<List<StorageResponse>> findByTypeAndLocation(@RequestParam("type") StorageType type,@RequestParam("location") String location){
		List<StorageResponse> responses = storageService.findByTypeAndLocation(type, location);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storage-type-and-name")
	public ResponseEntity<List<StorageResponse>> findByTypeAndName(@RequestParam("type") StorageType type,@RequestParam("name") String name){
		List<StorageResponse> responses = storageService.findByTypeAndName(type, name);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storage-location-and-capacity")
	public ResponseEntity<List<StorageResponse>> findByLocationAndCapacity(@RequestParam("location") String location,@RequestParam("capacity") BigDecimal capacity){
		List<StorageResponse> responses = storageService.findByLocationAndCapacity(location, capacity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storage-type-and-capacity")
	public ResponseEntity<List<StorageResponse>> findByTypeAndCapacity(@RequestParam("type") StorageType type,@RequestParam("capacity") BigDecimal capacity){
		List<StorageResponse> responses = storageService.findByTypeAndCapacity(type, capacity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storage-type-location-capacity")
	public ResponseEntity<List<StorageResponse>> findByTypeAndLocationAndCapacity(@RequestParam("type") StorageType type,@RequestParam("location") String location,
			@RequestParam("capacity") BigDecimal capacity){
		List<StorageResponse> responses = storageService.findByTypeAndLocationAndCapacity(type, location, capacity);
		return ResponseEntity.ok(responses);
	}
	 
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storage-name-and-storage-location")
	public ResponseEntity<List<StorageResponse>> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(@RequestParam("name") String name,@RequestParam("location") String location){
		List<StorageResponse> responses = storageService.findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(name, location);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storage-capacity-range")
	public ResponseEntity<List<StorageResponse>> findByCapacityBetween(@RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max){
		List<StorageResponse> responses = storageService.findByCapacityBetween(min, max);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storage-type-capacity-desc")
	public ResponseEntity<List<StorageResponse>>  findByTypeOrderByCapacityDesc(@RequestParam("type") StorageType type){
		List<StorageResponse> responses = storageService.findByTypeOrderByCapacityDesc(type);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/location-order-name-asc")
	public ResponseEntity<List<StorageResponse>> findByLocationOrderByNameAsc(@RequestParam("location") String location){
		List<StorageResponse> responses = storageService.findByLocationOrderByNameAsc(location);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storage-without-goods")
	public ResponseEntity<List<StorageResponse>> findStoragesWithoutGoods(){
		List<StorageResponse> responses = storageService.findStoragesWithoutGoods();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/exact-shelf-count")
	public ResponseEntity<List<StorageResponse>> findByExactShelfCount(@RequestParam("shelfCount") Integer shelfCount){
		List<StorageResponse> responses = storageService.findByExactShelfCount(shelfCount);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/location-and-type")
	public ResponseEntity<List<StorageResponse>> findByLocationContainingIgnoreCaseAndType(@RequestParam("location") String location,@RequestParam("type") StorageType type){
		List<StorageResponse> responses = storageService.findByLocationContainingIgnoreCaseAndType(location, type);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storages-with-materials")
	public ResponseEntity<List<StorageResponse>> findStoragesWithMaterials(){
		List<StorageResponse> responses = storageService.findStoragesWithMaterials();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storages-with-work-centers")
	public ResponseEntity<List<StorageResponse>> findStoragesWithWorkCenters(){
		List<StorageResponse> responses = storageService.findStoragesWithWorkCenters();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storages-without-shelves")
	public ResponseEntity<List<StorageResponse>> findStoragesWithoutShelves(){
		List<StorageResponse> responses = storageService.findStoragesWithoutShelves();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/available-storages")
	public ResponseEntity<List<StorageResponse>> findAvailableStorages(){
		List<StorageResponse> responses = storageService.findAvailableStorages();
		return ResponseEntity.ok(responses);
	}
	 
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/suitable-storages-for-shipment")
	public ResponseEntity<List<StorageResponse>> findSuitableStoragesForShipment(@RequestParam("minCapacity") BigDecimal minCapacity){
		List<StorageResponse> responses = storageService.findSuitableStoragesForShipment(minCapacity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/empty-storages")
	public ResponseEntity<List<StorageResponse>> findEmptyStorages(){
		List<StorageResponse> responses = storageService.findEmptyStorages();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storage/without-goods-and-materials-by-type")
	public ResponseEntity<List<StorageResponse>> findStorageWithoutGoodsAndMaterialsByType(@RequestParam("type") StorageType type){
		List<StorageResponse> responses = storageService.findStorageWithoutGoodsAndMaterialsByType(type);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storage/with-goods-and-materials-by-type")
	public ResponseEntity<List<StorageResponse>> findStorageWithGoodsAndMaterialsByType(@RequestParam("type") StorageType type){
		List<StorageResponse> responses = storageService.findStorageWithGoodsAndMaterialsByType(type);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/with-goods-or-materials-by-type")
	public ResponseEntity<List<StorageResponse>> findStorageWithGoodsOrMaterialsByType(@RequestParam("type") StorageType type){
		List<StorageResponse> responses = storageService.findStorageWithGoodsOrMaterialsByType(type);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/storage/by-type")
	public ResponseEntity<List<StorageResponse>> findAllByType(@RequestParam("type") StorageType type){
		List<StorageResponse> responses = storageService.findAllByType(type);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/empty-storages/by-type")
	public ResponseEntity<List<StorageResponse>> findEmptyStorageByType(@RequestParam("type") StorageType type){
		List<StorageResponse> responses = storageService.findEmptyStorageByType(type);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/production-storages")
	public ResponseEntity<List<StorageResponse>> findProductionStorage(){
		List<StorageResponse> responses = storageService.findProductionStorage();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/distribution-storages")
	public ResponseEntity<List<StorageResponse>> findDistributionStorage(){
		List<StorageResponse> responses = storageService.findDistributionStorage();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/open-storages")
	public ResponseEntity<List<StorageResponse>> findOpenStorage(){
		List<StorageResponse> responses = storageService.findOpenStorage();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/closed-storages")
	public ResponseEntity<List<StorageResponse>> findClosedStorage(){
		List<StorageResponse> responses = storageService.findClosedStorage();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/search/interim-storages")
	public ResponseEntity<List<StorageResponse>> findInterimStorage(){
		List<StorageResponse> responses = storageService.findInterimStorage();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/{id}/available-capacity")
    public ResponseEntity<BigDecimal> getAvailableCapacity(@PathVariable Long id) {
        return ResponseEntity.ok(storageService.getAvailableCapacity(id));
    }
	
	@PreAuthorize(RoleGroups.STORAGE_FULL_ACCESS)
	@PostMapping("/{id}/allocate")
    public ResponseEntity<Void> allocateCapacity(
        @PathVariable Long id,
        @RequestParam BigDecimal amount) {
        storageService.allocateCapacity(id, amount);
        return ResponseEntity.ok().build();
    }
	
	@PreAuthorize(RoleGroups.STORAGE_FULL_ACCESS)
	@PutMapping("/storage/{id}/release-capacity")
	public ResponseEntity<Void> releaseCapacity(
	        @PathVariable Long id,
	        @RequestParam BigDecimal amount) {
	    storageService.releaseCapacity(id, amount);
	    return ResponseEntity.noContent().build(); 
	}
	
	@PreAuthorize(RoleGroups.STORAGE_READ_ACCESS)
	@GetMapping("/storage/{storageId}/has-capacity")
	public ResponseEntity<Boolean> hasCapacity(@PathVariable Long storageId,@RequestParam BigDecimal amount){
		Boolean total = storageService.hasCapacity(storageId, amount);
		return ResponseEntity.ok(total);
	}

}
