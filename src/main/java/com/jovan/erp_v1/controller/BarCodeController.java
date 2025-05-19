package com.jovan.erp_v1.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.jovan.erp_v1.request.BarCodeRequest;
import com.jovan.erp_v1.response.BarCodeResponse;
import com.jovan.erp_v1.service.IBarcodeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/barCodes")
@CrossOrigin("http://localhost:5173")
public class BarCodeController {

	private final IBarcodeService barcodeService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<BarCodeResponse> create(@Valid @RequestBody BarCodeRequest request) {
	    LocalDateTime now = LocalDateTime.now();
	    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    BarCodeRequest enrichedRequest = new BarCodeRequest(
	        null,
	        request.code(),
	        now,            // backend postavlja vreme skeniranja
	        currentUser,    // backend postavlja korisnika iz tokena/session
	        request.goodsId()
	    );

	    BarCodeResponse response = barcodeService.createBarCode(enrichedRequest);
	    return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<BarCodeResponse> update(
	        @PathVariable Long id, 
	        @Valid @RequestBody BarCodeRequest request) {
	    // Uhvati trenutno vreme
	    LocalDateTime now = LocalDateTime.now();
	    // Uhvati trenutno ulogovanog korisnika iz Security Context-a
	    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    // Kreiraj novi request sa automatski postavljenim scannedAt i scannedBy
	    BarCodeRequest enrichedRequest = new BarCodeRequest(
	        id,                 // id ostaje isti, jer je update
	        request.code(),     
	        now,                // backend postavlja vreme skeniranja
	        currentUser,        // backend postavlja korisnika iz auth
	        request.goodsId()
	    );
	    BarCodeResponse response = barcodeService.updateBarCode(id, enrichedRequest);
	    return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		barcodeService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/get-one/{id}")
	public ResponseEntity<BarCodeResponse> getOne(@PathVariable Long id){
		BarCodeResponse response = barcodeService.getOne(id);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<List<BarCodeResponse>> getAll(){
		List<BarCodeResponse> responses = barcodeService.getAll();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/get-by-code")
	public ResponseEntity<BarCodeResponse> getByCode(@RequestParam("code") String code){
		BarCodeResponse response = barcodeService.getByCode(code);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/get-by-goodsId")
	public ResponseEntity<List<BarCodeResponse>> getByGoods(@RequestParam("goodsId") Long goodsId){
		List<BarCodeResponse> responses = barcodeService.getByGoods(goodsId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/get-by-scannedBy")
	public ResponseEntity<List<BarCodeResponse>> getByScannedBy(@RequestParam("scannedBy") String scannedBy){
		List<BarCodeResponse> responses = barcodeService.getByScannedBy(scannedBy);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/get-by-date-between")
	public ResponseEntity<List<BarCodeResponse>> getByScannedAtBetween(
			@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to){
		List<BarCodeResponse> responses = barcodeService.getByScannedAtBetween(from, to);
		return ResponseEntity.ok(responses);
	}
}
