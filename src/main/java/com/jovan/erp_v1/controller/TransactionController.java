package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.TransactionRequest;
import com.jovan.erp_v1.response.TransactionResponse;
import com.jovan.erp_v1.service.ITransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

	private final ITransactionService transactionService;
	
	@PostMapping("/create/new-transaction")
	public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request){
		TransactionResponse items = transactionService.create(request);
		return ResponseEntity.ok(items);
	}
	
	
	@PutMapping("/update/{id}")
	public ResponseEntity<TransactionResponse> update(@PathVariable Long id, @Valid @RequestBody TransactionRequest request){
		TransactionResponse items = transactionService.update(id, request);
		return ResponseEntity.ok(items);
	}
	
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		transactionService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/find-one/{id}")
	public ResponseEntity<TransactionResponse> findOne(@PathVariable Long id){
		TransactionResponse items = transactionService.findOne(id);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/find-all")
	public ResponseEntity<List<TransactionResponse>> findAll(){
		List<TransactionResponse> items = transactionService.findAll();
		return ResponseEntity.ok(items);
	}
}
