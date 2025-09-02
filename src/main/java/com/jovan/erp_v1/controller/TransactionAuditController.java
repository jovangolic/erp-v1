package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.response.TransactionAuditResponse;
import com.jovan.erp_v1.service.ITransactionAuditService;
import com.jovan.erp_v1.util.RoleGroups;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/audit/transactions")
@PreAuthorize(RoleGroups.TRANSACTION_AUDIT_ACCESS)
public class TransactionAuditController {

	private final ITransactionAuditService transactionAuditService;
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<TransactionAuditResponse>> findByUserId(@PathVariable Long userId){
		List<TransactionAuditResponse> items = transactionAuditService.findByUserId(userId);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/transaction/{transactionId}")
	public ResponseEntity<List<TransactionAuditResponse>> findByTransactionId(@PathVariable Long transactionId){
		List<TransactionAuditResponse> items = transactionAuditService.findByTransactionId(transactionId);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-account-number")
	public ResponseEntity<List<TransactionAuditResponse>> findByAccountNumberContainingIgnoreCase(@RequestParam("accountNumber") String accountNumber){
		List<TransactionAuditResponse> items = transactionAuditService.findByAccountNumberContainingIgnoreCase(accountNumber);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-transaction-type")
	public ResponseEntity<List<TransactionAuditResponse>> findByTransactionType(@RequestParam("transactionType") TransactionType transactionType){
		List<TransactionAuditResponse> items = transactionAuditService.findByTransactionType(transactionType);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/find-all")
	public ResponseEntity<List<TransactionAuditResponse>> findAll(){
		List<TransactionAuditResponse> items = transactionAuditService.findAll();
		return ResponseEntity.ok(items);
	}
}
