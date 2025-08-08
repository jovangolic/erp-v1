package com.jovan.erp_v1.controller;

import java.time.LocalDate;
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

import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.request.InventoryRequest;
import com.jovan.erp_v1.response.InventoryResponse;
import com.jovan.erp_v1.service.IInventoryService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventories")
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
public class InventoryController {

	private final IInventoryService inventoryService;
	
	@PreAuthorize(RoleGroups.INVENTORY_EDIT_ACCESS)
	@PostMapping("/create/new-inventory")
	public ResponseEntity<InventoryResponse> create(@Valid @RequestBody InventoryRequest request){
		InventoryResponse response = inventoryService.create(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_EDIT_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<InventoryResponse> update(@PathVariable Long id, @Valid @RequestBody InventoryRequest request){
		InventoryResponse response = inventoryService.update(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		inventoryService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_APPROVE_ACCESS)
	@PostMapping("/{id}/approve")
	public ResponseEntity<InventoryResponse> approve(@PathVariable Long id) {
		InventoryResponse approvedInventory = inventoryService.approveInventory(id);
	    return ResponseEntity.ok(approvedInventory);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/find-by-status")
	public ResponseEntity<List<InventoryResponse>> findInventoryByStatus(@RequestParam("status") InventoryStatus status){
		List<InventoryResponse> responses = inventoryService.findInventoryByStatus(status);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/by-storageEmployeeId")
	public ResponseEntity<List<InventoryResponse>> findByStorageEmployeeId(@RequestParam("storageEmployeeId") Long storageEmpolyeeId){
		List<InventoryResponse> responses = inventoryService.findByStorageEmployeeId(storageEmpolyeeId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/by-storageForemanId")
	public ResponseEntity<List<InventoryResponse>> findByStorageForemanId(@RequestParam("storageForemanId") Long storageForemanId){
		List<InventoryResponse> responses = inventoryService.findByStorageForemanId(storageForemanId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/find-one/{id}")
	public ResponseEntity<InventoryResponse> findOne(@PathVariable Long id){
		InventoryResponse response = inventoryService.findById(id);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("find-all")
	public ResponseEntity<List<InventoryResponse>> findAll(){
		List<InventoryResponse> responses = inventoryService.findAll();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/find-by-date")
	public ResponseEntity<List<InventoryResponse>> findByDate(@RequestParam("date") LocalDate date){
		List<InventoryResponse> responses = inventoryService.findByDate(date);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/find-by-date-range")
	public ResponseEntity<List<InventoryResponse>> findByDateRange(
	        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
	    List<InventoryResponse> responses = inventoryService.findByDateRange(startDate, endDate);
	    return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_EDIT_ACCESS)
	@PostMapping("/changeStatus/{inventoryId}")
	public ResponseEntity<Void> changeStatus(@PathVariable Long inventoryId,
	                                         @RequestParam("newStatus") String newStatusStr) {
	    try {
	        InventoryStatus newStatus = InventoryStatus.valueOf(newStatusStr.toUpperCase());
	        inventoryService.changeStatus(inventoryId, newStatus);
	        return ResponseEntity.ok().build();
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().build(); // ili custom exception
	    }
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/find-by-pending-inventories")
	public ResponseEntity<List<InventoryResponse>> findPendingInventories(){
		List<InventoryResponse> responses = inventoryService.findPendingInventories();
		return ResponseEntity.ok(responses);
	}
	
	//nove metode
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/date-after")
	public ResponseEntity<List<InventoryResponse>> findByDateAfter(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
		List<InventoryResponse> responses = inventoryService.findByDateAfter(date);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/date-before")
	public ResponseEntity<List<InventoryResponse>> findByDateBefore(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
		List<InventoryResponse> responses = inventoryService.findByDateBefore(date);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/search/employee-full-name")
	public ResponseEntity<List<InventoryResponse>> findByStorageEmployee_FullNameContainingIgnoreCase(@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName){
		List<InventoryResponse> responses = inventoryService.findByStorageEmployee_FullNameContainingIgnoreCase(firstName, lastName);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/search/foreman-full-name")
	public ResponseEntity<List<InventoryResponse>> findBystorageForeman_FullNameContainingIgnoreCase(@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName){
		List<InventoryResponse> responses = inventoryService.findBystorageForeman_FullNameContainingIgnoreCase(firstName, lastName);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/search/employee-email")
	public ResponseEntity<List<InventoryResponse>> findByStorageEmployee_EmailILikegnoreCase(@RequestParam("email") String email){
		List<InventoryResponse> responses = inventoryService.findByStorageEmployee_EmailILikegnoreCase(email);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/search/employee-address")
	public ResponseEntity<List<InventoryResponse>> findByStorageEmployee_Address(@RequestParam("address") String address){
		List<InventoryResponse> responses = inventoryService.findByStorageEmployee_Address(address);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/search/employee-phone-number")
	public ResponseEntity<List<InventoryResponse>> findByStorageEmployee_PhoneNumberLikeIgnoreCase(@RequestParam("phoneNumber") String phoneNumber){
		List<InventoryResponse> responses = inventoryService.findByStorageEmployee_PhoneNumberLikeIgnoreCase(phoneNumber);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/search/foreman-address")
	public ResponseEntity<List<InventoryResponse>> findByStorageForeman_Address(@RequestParam("address")String address){
		List<InventoryResponse> responses = inventoryService.findByStorageForeman_Address(address);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/search/foreman-phone-number")
	public ResponseEntity<List<InventoryResponse>> findByStorageForeman_PhoneNumberLikeIgnoreCase(@RequestParam("phoneNumber") String phoneNumber){
		List<InventoryResponse> responses = inventoryService.findByStorageForeman_PhoneNumberLikeIgnoreCase(phoneNumber);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/search/foreman-email")
	public ResponseEntity<List<InventoryResponse>> findByStorageForeman_EmailLikeIgnoreCase(@RequestParam("email") String email){
		List<InventoryResponse> responses = inventoryService.findByStorageForeman_EmailLikeIgnoreCase(email);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/search/status-and-employee-full-name")
	public ResponseEntity<List<InventoryResponse>> findByStatusAndStorageEmployeeFullNameContainingIgnoreCase(@RequestParam("status") InventoryStatus status,
			@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName){
		List<InventoryResponse> responses = inventoryService.findByStatusAndStorageEmployeeFullNameContainingIgnoreCase(status, firstName, lastName);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/search/status-and-foreman-full-name")
	public ResponseEntity<List<InventoryResponse>> findByStatusAndStorageForemanFullNameContainingIgnoreCase(@RequestParam("status") InventoryStatus status,
			@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName){
		List<InventoryResponse> responses = inventoryService.findByStatusAndStorageForemanFullNameContainingIgnoreCase(status, firstName, lastName);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/foreman/{foremanId}/inventory-date-range")
	public ResponseEntity<List<InventoryResponse>> findInventoryByStorageForemanIdAndDateRange(@PathVariable Long foremanId,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
		List<InventoryResponse> responses = inventoryService.findInventoryByStorageForemanIdAndDateRange(foremanId, startDate, endDate);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/search/employee/{employeeId}/status")
	public ResponseEntity<List<InventoryResponse>> findByStorageEmployeeIdAndStatus(@PathVariable Long employeeId,@RequestParam("status") InventoryStatus status){
		List<InventoryResponse> responses = inventoryService.findByStorageEmployeeIdAndStatus(employeeId, status);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/search/foreman/{foremanId}/status")
	public ResponseEntity<List<InventoryResponse>> findByStorageForemanIdAndStatus(@PathVariable Long foremanId,@RequestParam("status") InventoryStatus status){
		List<InventoryResponse> responses = inventoryService.findByStorageForemanIdAndStatus(foremanId, status);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/employee/{employeeId}/date-range")
	public ResponseEntity<List<InventoryResponse>> findByStorageEmployeeIdAndDateBetween(@PathVariable Long employeeId,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
		List<InventoryResponse> responses = inventoryService.findByStorageEmployeeIdAndDateBetween(employeeId, startDate, endDate);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/foreman/{foremanId}/date-range")
	public ResponseEntity<List<InventoryResponse>> findByStorageForemanIdAndDateBetween(@PathVariable Long foremanId,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
		List<InventoryResponse> responses = inventoryService.findByStorageForemanIdAndDateBetween(foremanId, startDate, endDate);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/count-by/{foremanId}")
	public ResponseEntity<Long> countByStorageForemanId(@PathVariable Long foremanId){
		Long responses = inventoryService.countByStorageForemanId(foremanId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.INVENTORY_READ_ACCESS)
	@GetMapping("/exists-by-status")
	public ResponseEntity<Boolean> existsByStatus(@RequestParam("status") InventoryStatus status){
		Boolean responses = inventoryService.existsByStatus(status);
		return ResponseEntity.ok(responses);
	}
	
}
