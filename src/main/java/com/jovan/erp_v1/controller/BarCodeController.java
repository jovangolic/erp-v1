package com.jovan.erp_v1.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

import com.jovan.erp_v1.enumeration.BarCodeStatus;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.BarCodeRequest;
import com.jovan.erp_v1.response.BarCodeResponse;
import com.jovan.erp_v1.save_as.BarCodeSaveAsRequest;
import com.jovan.erp_v1.search_request.BarCodeSearchRequest;
import com.jovan.erp_v1.service.IBarcodeService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/barCodes")
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.BAR_CODE_READ_ACCESS)
public class BarCodeController {

	private final IBarcodeService barcodeService;
	private final UserRepository userRepository;
	
	@PreAuthorize(RoleGroups.BAR_CODE_FULL_ACCESS)
	@PostMapping("/create")
	public ResponseEntity<BarCodeResponse> create(@Valid @RequestBody BarCodeRequest request) {
	    LocalDateTime now = LocalDateTime.now();
	    String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
	    Long scannedById = userRepository.findByUsername(currentUsername)
	            .orElseThrow(() -> new UsernameNotFoundException("Korisnik nije pronadjen: " + currentUsername))
	            .getId();
	    BarCodeRequest enrichedRequest = new BarCodeRequest(
	        null,
	        request.code(),
	        now,          // backend postavlja vreme skeniranja
	        scannedById,  // backend postavlja scannedById (Long)
	        request.goodsId(),
	        BarCodeStatus.ACTIVE,
	        false
	    );
	    BarCodeResponse response = barcodeService.createBarCode(enrichedRequest);
	    return ResponseEntity.ok(response);
	}

	@PreAuthorize(RoleGroups.BAR_CODE_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<BarCodeResponse> update(
	        @PathVariable Long id, 
	        @Valid @RequestBody BarCodeRequest request) {
	    LocalDateTime now = LocalDateTime.now();
	    String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
	    Long scannedById = userRepository.findByUsername(currentUsername)
	            .orElseThrow(() -> new UsernameNotFoundException("Korisnik nije pronadjen: " + currentUsername))
	            .getId();
	    BarCodeRequest enrichedRequest = new BarCodeRequest(
	        id,
	        request.code(),
	        now,
	        scannedById,
	        request.goodsId(),
	        BarCodeStatus.CONFIRMED, 
	        true 
	    );
	    BarCodeResponse response = barcodeService.updateBarCode(id, enrichedRequest);
	    return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		barcodeService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_READ_ACCESS)
	@GetMapping("/get-one/{id}")
	public ResponseEntity<BarCodeResponse> getOne(@PathVariable Long id){
		BarCodeResponse response = barcodeService.getOne(id);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_READ_ACCESS)
	@GetMapping("/get-all")
	public ResponseEntity<List<BarCodeResponse>> getAll(){
		List<BarCodeResponse> responses = barcodeService.getAll();
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_READ_ACCESS)
	@GetMapping("/get-by-code")
	public ResponseEntity<BarCodeResponse> getByCode(@RequestParam("code") String code){
		BarCodeResponse response = barcodeService.getByCode(code);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_READ_ACCESS)
	@GetMapping("/goods/{goodsId}")
	public ResponseEntity<List<BarCodeResponse>> findByGoods_Id(@PathVariable Long goodsId){
		List<BarCodeResponse> responses = barcodeService.findByGoods_Id(goodsId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_READ_ACCESS)
	@GetMapping("by-goodsName")
	public ResponseEntity<List<BarCodeResponse>> findByGoods_Name(@RequestParam("goodsName") String goodsName){
		List<BarCodeResponse> responses = barcodeService.findByGoods_Name(goodsName);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_READ_ACCESS)
	@GetMapping("/scannedBy-first-last-name")
	public ResponseEntity<List<BarCodeResponse>> findByScannedBy_FirstNameContainingIgnoreCaseAndScannedBy_LastNameContainingIgnoreCase(
			@RequestParam("userFirstName") String userFirstName,@RequestParam("userLastName") String userLastName){
		List<BarCodeResponse> responses = barcodeService.findByScannedBy_FirstNameContainingIgnoreCaseAndScannedBy_LastNameContainingIgnoreCase(userFirstName, userLastName);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_READ_ACCESS)
	@GetMapping("/scannedBy/{scannedById}")
	public ResponseEntity<List<BarCodeResponse>> getByScannedBy(@PathVariable Long scannedById){
		List<BarCodeResponse> responses = barcodeService.findByScannedBy_Id(scannedById);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_READ_ACCESS)
	@GetMapping("/get-by-date-between")
	public ResponseEntity<List<BarCodeResponse>> getByScannedAtBetween(
			@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to){
		List<BarCodeResponse> responses = barcodeService.findByScannedAtBetween(from, to);
		return ResponseEntity.ok(responses);
	}
	
	//nove metode
	@PreAuthorize(RoleGroups.BAR_CODE_READ_ACCESS)
	@GetMapping("/track/{id}")
	public ResponseEntity<BarCodeResponse> trackBarCode(@PathVariable Long id){
		BarCodeResponse items = barcodeService.trackBarCode(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_FULL_ACCESS)
	@PostMapping("/{id}/confirm")
	public ResponseEntity<BarCodeResponse> confirmBarCode(@PathVariable Long id){
		BarCodeResponse items = barcodeService.confirmBarCode(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_FULL_ACCESS)
	@PostMapping("/{id}/close")
	public ResponseEntity<BarCodeResponse> closeBarCode(@PathVariable Long id){
		BarCodeResponse items = barcodeService.closeBarCode(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_FULL_ACCESS)
	@PostMapping("/{id}/cancel")
	public ResponseEntity<BarCodeResponse> cancelBarCode(@PathVariable Long id){
		BarCodeResponse items = barcodeService.cancelBarCode(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_FULL_ACCESS)
	@PostMapping("/{id}/status/{status}")
	public ResponseEntity<BarCodeResponse> changeStatus(@PathVariable Long id,@PathVariable BarCodeStatus status){
		BarCodeResponse items = barcodeService.changeStatus(id, status);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_FULL_ACCESS)
	@PostMapping("/save")
	public ResponseEntity<BarCodeResponse> saveBarCode(@Valid @RequestBody BarCodeRequest request){
		BarCodeResponse items = barcodeService.saveBarCode(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_FULL_ACCESS)
	@PostMapping("/save-as")
	public ResponseEntity<BarCodeResponse> saveAs(@Valid @RequestBody BarCodeSaveAsRequest request){
		BarCodeResponse items = barcodeService.saveAs(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_FULL_ACCESS)
	@PostMapping("/save-all")
	public ResponseEntity<List<BarCodeResponse>> saveAll(@Valid @RequestBody List<BarCodeRequest> request){
		List<BarCodeResponse> items = barcodeService.saveAll(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BAR_CODE_FULL_ACCESS)
	@PostMapping("/general-search")
	public ResponseEntity<List<BarCodeResponse>> generalSearch(@Valid @RequestBody BarCodeSearchRequest request){
		List<BarCodeResponse> items = barcodeService.generalSearch(request);
		return ResponseEntity.ok(items);
	}
}
