package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.MaterialTransactionStatus;
import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.request.MaterialTransactionRequest;
import com.jovan.erp_v1.response.MaterialTransactionResponse;
import com.jovan.erp_v1.service.IMaterialTransactionService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/materialTransactions")
@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
public class MaterialTransactionController {

	private final IMaterialTransactionService materialTransactionService;
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_FULL_ACCESS)
	@PostMapping("/create/new-materialTransaction")
	public ResponseEntity<MaterialTransactionResponse> create(@Valid @RequestBody MaterialTransactionRequest request){
		MaterialTransactionResponse response = materialTransactionService.create(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<MaterialTransactionResponse> update(@PathVariable Long id, @Valid @RequestBody MaterialTransactionRequest request){
		MaterialTransactionResponse response = materialTransactionService.update(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		materialTransactionService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/find-one/{id}")
	public ResponseEntity<MaterialTransactionResponse> findOne(@PathVariable Long id){
		MaterialTransactionResponse response = materialTransactionService.findOne(id);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/find-all")
	public ResponseEntity<List<MaterialTransactionResponse>> findAll(){
		List<MaterialTransactionResponse> responses = materialTransactionService.findAll();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/material/{materialId}")
	public ResponseEntity<List<MaterialTransactionResponse>> findByMaterial_Id(@PathVariable Long materialId){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByMaterial_Id(materialId);
		return ResponseEntity.ok(responses);		
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/material-materialCode")
	public ResponseEntity<List<MaterialTransactionResponse>> findByMaterial_CodeContainingIgnoreCase(@RequestParam("materialCode") String materialCode){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByMaterial_CodeContainingIgnoreCase(materialCode);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/material-materialName")
	public ResponseEntity<List<MaterialTransactionResponse>> findByMaterial_NameContainingIgnoreCase(@RequestParam("materialName") String materialName){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByMaterial_NameContainingIgnoreCase(materialName);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/mayerial-unit")
	public ResponseEntity<List<MaterialTransactionResponse>> findByMaterial_Unit(@RequestParam("unit") UnitOfMeasure unit){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByMaterial_Unit(unit);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/material-currentStock")
	public ResponseEntity<List<MaterialTransactionResponse>> findByMaterial_CurrentStock(@RequestParam("currentStock") BigDecimal currentStock){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByMaterial_CurrentStock(currentStock);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/material-currentStock-greater-than")
	public ResponseEntity<List<MaterialTransactionResponse>> findByMaterial_CurrentStockGreaterThan(@RequestParam("currentStock") BigDecimal currentStock){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByMaterial_CurrentStockGreaterThan(currentStock);
				return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/material-currentStock-less-than")
	public ResponseEntity<List<MaterialTransactionResponse>> findByMaterial_CurrentStockLessThan(@RequestParam("currentStock") BigDecimal currentStock){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByMaterial_CurrentStockLessThan(currentStock);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/material/storage/{storageId}")
	public ResponseEntity<List<MaterialTransactionResponse>> findByMaterial_Storage_Id(@PathVariable Long storageId){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByMaterial_Storage_Id(storageId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/material-reorderLevel")
	public ResponseEntity<List<MaterialTransactionResponse>> findByMaterial_ReorderLevel(@RequestParam("reorderLevel") BigDecimal reorderLevel){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByMaterial_ReorderLevel(reorderLevel);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/by-quantity")
	public ResponseEntity<List<MaterialTransactionResponse>> findByQuantity(@RequestParam("quantity") BigDecimal quantity){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByQuantity(quantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/quantity-greater-than")
	public ResponseEntity<List<MaterialTransactionResponse>> findByQuantityGreaterThan(@RequestParam("quantity") BigDecimal quantity){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByQuantityGreaterThan(quantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/quantity-less-than")
	public ResponseEntity<List<MaterialTransactionResponse>> findByQuantityLessThan(@RequestParam("quantity") BigDecimal quantity){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByQuantityLessThan(quantity);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/by-type")
	public ResponseEntity<List<MaterialTransactionResponse>> findByType(@RequestParam("type") TransactionType type){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByType(type);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/by-transactionDate")
	public ResponseEntity<List<MaterialTransactionResponse>> findByTransactionDate(@RequestParam("transactionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByTransactionDate(transactionDate);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/transactionDate-range")
	public ResponseEntity<List<MaterialTransactionResponse>> findByTransactionDateBetween(@RequestParam("transactionDateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDateStart,
            @RequestParam("transactionDateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDateEnd){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByTransactionDateBetween(transactionDateStart, transactionDateEnd);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/transactionDate-greater-than-equal")
	public ResponseEntity<List<MaterialTransactionResponse>> findByTransactionDateGreaterThanEqual(@RequestParam("transactionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByTransactionDateGreaterThanEqual(transactionDate);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/vendor/{vendorId}")
	public ResponseEntity<List<MaterialTransactionResponse>> findByVendor_Id(@PathVariable Long vendorId){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByVendor_Id(vendorId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/vendor-vendorName")
	public ResponseEntity<List<MaterialTransactionResponse>> findByVendor_NameContainingIgnoreCase(@RequestParam("vendorName") String vendorName){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByVendor_NameContainingIgnoreCase(vendorName);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/vendor-vendorEmail")
	public ResponseEntity<List<MaterialTransactionResponse>> findByVendor_EmailContainingIgnoreCase(@RequestParam("vendorEmail") String vendorEmail){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByVendor_EmailContainingIgnoreCase(vendorEmail);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/vendor-vendorPhone")
	public ResponseEntity<List<MaterialTransactionResponse>> findByVendor_PhoneNumber(@RequestParam("vendorPhone") String vendorPhone){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByVendor_PhoneNumber(vendorPhone);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/vendor-vendorAddress")
	public ResponseEntity<List<MaterialTransactionResponse>> findByVendor_AddressContainingIgnoreCase(@RequestParam("vendorAddress") String vendorAddress){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByVendor_AddressContainingIgnoreCase(vendorAddress);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/by-documentReference")
	public ResponseEntity<List<MaterialTransactionResponse>> findByDocumentReference(@RequestParam("documentReference") String documentReference){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByDocumentReference(documentReference);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/by-notes")
	public ResponseEntity<List<MaterialTransactionResponse>> findByNotes(@RequestParam("notes") String notes){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByNotes(notes);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/by-status")
	public ResponseEntity<List<MaterialTransactionResponse>>  findByStatus(@RequestParam("status") MaterialTransactionStatus status){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByStatus(status);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/createdByUser/{userId}")
	public ResponseEntity<List<MaterialTransactionResponse>> findByCreatedByUser_Id(@PathVariable Long userId){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByCreatedByUser_Id(userId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/createdUser-name")
	public ResponseEntity<List<MaterialTransactionResponse>> findByCreatedByUser_FirstNameContainingIgnoreCaseAndCreatedByUser_LastNameContainingIgnoreCase(
			@RequestParam("userFirstName") String userFirstName,@RequestParam("userLastName") String userLastName){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByCreatedByUser_FirstNameContainingIgnoreCaseAndCreatedByUser_LastNameContainingIgnoreCase(userFirstName, userLastName);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.MATERIAL_TRANSACTION_READ_ACCESS)
	@GetMapping("/createdByUser-userEmail")
	public ResponseEntity<List<MaterialTransactionResponse>> findByCreatedByUser_EmailContainingIgnoreCase(@RequestParam("userEmail") String userEmail){
		List<MaterialTransactionResponse> responses = materialTransactionService.findByCreatedByUser_EmailContainingIgnoreCase(userEmail);
		return ResponseEntity.ok(responses);
	}
	
}
