package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.mapper.StorageMapper;
import com.jovan.erp_v1.request.StorageRequest;
import com.jovan.erp_v1.response.StorageResponse;
import com.jovan.erp_v1.service.IStorageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/storages")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class StorageController {

	private final IStorageService storageService;
	private final StorageMapper storageMapper;

	@PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PostMapping("/create/new-storage")
	public ResponseEntity<StorageResponse> createStorage(@Valid @RequestBody StorageRequest request) {
		StorageResponse response = storageService.createStorage(request);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PutMapping("/update/{storageId}")
	public ResponseEntity<StorageResponse> updateStorage(@PathVariable Long storageId,
			@Valid @RequestBody StorageRequest request) {
		StorageResponse updated = storageService.updateStorage(storageId, request);
		return ResponseEntity.ok(updated);
	}

	@PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@DeleteMapping("/delete/{storageId}")
	public ResponseEntity<Void> deleteStorage(@PathVariable Long storageId) {
		storageService.deleteStorage(storageId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/get-by-storage-type")
	public ResponseEntity<List<StorageResponse>> getByStorageType(@RequestParam("storageType") StorageType type) {
		List<StorageResponse> responses = storageService.getByStorageType(type);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/{storageId}")
	public ResponseEntity<StorageResponse> getByStorageId(@PathVariable Long storageId) {
		StorageResponse response = storageService.getByStorageId(storageId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/storage/by-name")
	public ResponseEntity<List<StorageResponse>> getByStorageName(@RequestParam("name") String name) {
		List<StorageResponse> responses = storageService.getByName(name);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/by-location")
	public ResponseEntity<List<StorageResponse>> getByStorageLocation(@RequestParam("location") String location) {
		List<StorageResponse> responses = storageService.getByLocation(location);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/by-capacity")
	public ResponseEntity<List<StorageResponse>> getByStorageCapacity(@RequestParam("capacity") BigDecimal capacity) {
		List<StorageResponse> responses = storageService.getByCapacity(capacity);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/by-name-and-location")
	public ResponseEntity<List<StorageResponse>> getStorageByNameAndLocation(@RequestParam("name") String name,
			@RequestParam("location") String location) {
		List<StorageResponse> responses = storageService.getByNameAndLocation(name, location);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/by-type-and-capacity")
	public ResponseEntity<List<StorageResponse>> getByTypeAndCapacityGreaterThan(@RequestParam("type") StorageType type,
			@RequestParam("capacity") BigDecimal capacity) {
		List<StorageResponse> responses = storageService.getByTypeAndCapacityGreaterThan(type, capacity);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/by-minCount")
	public ResponseEntity<List<StorageResponse>> getStoragesWithMinGoods(@RequestParam("minCount") Integer minCount) {
		List<StorageResponse> responses = storageService.getStoragesWithMinGoods(minCount);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/ignore-case")
	public ResponseEntity<List<StorageResponse>> getByNameContainingIgnoreCase(@RequestParam("name") String name) {
		List<StorageResponse> responses = storageService.getByNameContainingIgnoreCase(name);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/get-all-storages")
	public ResponseEntity<List<StorageResponse>> getAllStorage() {
		List<StorageResponse> responses = storageService.getAllStorage();
		return ResponseEntity.ok(responses);
	}
	
	//nove metode
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findByTypeAndCapacityLessThan(StorageType type, BigDecimal capacity){
		List<StorageResponse> responses = storageService.findByTypeAndCapacityLessThan(type, capacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findByCapacityGreaterThan(BigDecimal capacity){
		List<StorageResponse> responses = storageService.findByCapacityGreaterThan(capacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findByCapacityLessThan(BigDecimal capacity){
		List<StorageResponse> responses = storageService.findByCapacityLessThan(capacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findByNameAndLocationAndCapacity(String name, String location, BigDecimal capacity){
		List<StorageResponse> responses = storageService.findByNameAndLocationAndCapacity(name, location, capacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findByTypeAndLocation(StorageType type, String location){
		List<StorageResponse> responses = storageService.findByTypeAndLocation(type, location);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findByTypeAndName(StorageType type, String name){
		List<StorageResponse> responses = storageService.findByTypeAndName(type, name);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findByLocationAndCapacity(StorageType type, BigDecimal capacity){
		List<StorageResponse> responses = storageService.findByLocationAndCapacity(type, capacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findByTypeAndLocationAndCapacity(StorageType type, String location, BigDecimal capacity){
		List<StorageResponse> responses = storageService.findByTypeAndLocationAndCapacity(type, location, capacity);
		return ResponseEntity.ok(responses);
	}
	 
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String name, String location){
		List<StorageResponse> responses = storageService.findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(name, location);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findByCapacityBetween(BigDecimal min, BigDecimal max){
		List<StorageResponse> responses = storageService.findByCapacityBetween(min, max);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>>  findByTypeOrderByCapacityDesc(StorageType type){
		List<StorageResponse> responses = storageService.findByTypeOrderByCapacityDesc(type);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findByLocationOrderByNameAsc(String location){
		List<StorageResponse> responses = storageService.findByLocationOrderByNameAsc(location);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findStoragesWithoutGoods(){
		List<StorageResponse> responses = storageService.findStoragesWithoutGoods();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findByExactShelfCount(Integer shelfCount){
		List<StorageResponse> responses = storageService.findByExactShelfCount(shelfCount);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findByLocationContainingIgnoreCaseAndType(String location, StorageType type){
		List<StorageResponse> responses = storageService.findByLocationContainingIgnoreCaseAndType(location, type);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findStoragesWithMaterials(){
		List<StorageResponse> responses = storageService.findStoragesWithMaterials();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findStoragesWithWorkCenters(){
		List<StorageResponse> responses = storageService.findStoragesWithWorkCenters();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findStoragesWithoutShelves(){
		List<StorageResponse> responses = storageService.findStoragesWithoutShelves();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findAvailableStorages(){
		List<StorageResponse> responses = storageService.findAvailableStorages();
		return ResponseEntity.ok(responses);
	}
	 
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findSuitableStoragesForShipment( BigDecimal minCapacity){
		List<StorageResponse> responses = storageService.findSuitableStoragesForShipment(minCapacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findEmptyStorages(){
		List<StorageResponse> responses = storageService.findEmptyStorages();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findStorageWithoutGoodsAndMaterialsByType( StorageType type){
		List<StorageResponse> responses = storageService.findStorageWithoutGoodsAndMaterialsByType(type);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findStorageWithGoodsAndMaterialsByType( StorageType type){
		List<StorageResponse> responses = storageService.findStorageWithGoodsAndMaterialsByType(type);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findStorageWithGoodsOrMaterialsByType( StorageType type){
		List<StorageResponse> responses = storageService.findStorageWithGoodsOrMaterialsByType(type);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findAllByType(StorageType type){
		List<StorageResponse> responses = storageService.findAllByType(type);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findEmptyStorageByType( StorageType type){
		List<StorageResponse> responses = storageService.findEmptyStorageByType(type);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findProductionStorage(){
		List<StorageResponse> responses = storageService.findProductionStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findDistributionStorage(){
		List<StorageResponse> responses = storageService.findDistributionStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findOpenStorage(){
		List<StorageResponse> responses = storageService.findOpenStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findClosedStorage(){
		List<StorageResponse> responses = storageService.findClosedStorage();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<StorageResponse>> findInterimStorage(){
		List<StorageResponse> responses = storageService.findInterimStorage();
		return ResponseEntity.ok(responses);
	}
}
