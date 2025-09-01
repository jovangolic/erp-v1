package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.TransactionRequest;
import com.jovan.erp_v1.response.TransactionResponse;
import com.jovan.erp_v1.service.ITransactionService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
public class TransactionController {

	private final ITransactionService transactionService;
	private final UserRepository userRepository;
	
	@PreAuthorize(RoleGroups.TRANSACTION_FULL_ACCESS)
	@PostMapping("/create/new-transaction")
	public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request){
		TransactionResponse items = transactionService.create(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<TransactionResponse> update(@PathVariable Long id, @Valid @RequestBody TransactionRequest request){
		TransactionResponse items = transactionService.update(id, request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		transactionService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/find-one/{id}")
	public ResponseEntity<TransactionResponse> findOne(@PathVariable Long id){
		TransactionResponse items = transactionService.findOne(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/find-all")
	public ResponseEntity<List<TransactionResponse>> findAll(){
		List<TransactionResponse> items = transactionService.findAll();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/by-amount")
	public ResponseEntity<List<TransactionResponse>> findByAmount(@RequestParam("amount") BigDecimal amount){
		List<TransactionResponse> items = transactionService.findByAmount(amount);
		return ResponseEntity.ok(items);	
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/by-amount-greater-than")
	public ResponseEntity<List<TransactionResponse>> findByAmountGreaterThan(@RequestParam("amount") BigDecimal amount){
		List<TransactionResponse> items = transactionService.findByAmountGreaterThan(amount);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/by-amount-less-than")
	public ResponseEntity<List<TransactionResponse>> findByAmountLessThan(@RequestParam("amount") BigDecimal amount){
		List<TransactionResponse> items = transactionService.findByAmountLessThan(amount);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/by-amount-range")
	public ResponseEntity<List<TransactionResponse>> findByAmountBetween(@RequestParam("amountMin") BigDecimal amountMin,@RequestParam("amountMax") BigDecimal amountMax){
		List<TransactionResponse> items = transactionService.findByAmountBetween(amountMin, amountMax);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/by-transaction-date")
	public ResponseEntity<List<TransactionResponse>> findByTransactionDate(
			@RequestParam("transactionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime transactionDate){
		List<TransactionResponse> items = transactionService.findByTransactionDate(transactionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/by-transaction-date-after")
	public ResponseEntity<List<TransactionResponse>> findByTransactionDateAfter(
			@RequestParam("transactionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime transactionDate){
		List<TransactionResponse> items = transactionService.findByTransactionDateAfter(transactionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/by-transaction-date-before")
	public ResponseEntity<List<TransactionResponse>> findByTransactionDateBefore(
			@RequestParam("transactionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime transactionDate){
		List<TransactionResponse> items = transactionService.findByTransactionDateBefore(transactionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/by-transaction-date-range")
	public ResponseEntity<List<TransactionResponse>> findByTransactionDateBetween(
			@RequestParam("transactionDateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime transactionDateStart,
			@RequestParam("transactionDateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime transactionDateEnd){
		List<TransactionResponse> items = transactionService.findByTransactionDateBetween(transactionDateStart, transactionDateEnd);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/by-transaction-type")
	public ResponseEntity<List<TransactionResponse>> findByTransactionType(@RequestParam("transactionType") TransactionType transactionType){
		List<TransactionResponse> items = transactionService.findByTransactionType(transactionType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/transaction-type-and-date")
	public ResponseEntity<List<TransactionResponse>> findByTransactionTypeAndTransactionDate(
			@RequestParam("transactionType") TransactionType transactionType,
			@RequestParam("transactionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime transactionDate){
		List<TransactionResponse> items = transactionService.findByTransactionTypeAndTransactionDate(transactionType, transactionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/transaction-type-and-date-after")
	public ResponseEntity<List<TransactionResponse>> findByTransactionTypeAndTransactionDateAfter(
			@RequestParam("transactionType") TransactionType transactionType,
			@RequestParam("transactionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime transactionDate){
		List<TransactionResponse> items = transactionService.findByTransactionTypeAndTransactionDateAfter(transactionType, transactionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/transaction-type-and-date-before")
	public ResponseEntity<List<TransactionResponse>> findByTransactionTypeAndTransactionDateBefore(
			@RequestParam("transactionType") TransactionType transactionType,
			@RequestParam("transactionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime transactionDate){
		List<TransactionResponse> items = transactionService.findByTransactionTypeAndTransactionDateBefore(transactionType, transactionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/transaction-type-and-date-range")
	public ResponseEntity<List<TransactionResponse>> findByTransactionTypeAndTransactionDateBetween(
			@RequestParam("transactionType") TransactionType transactionType,
			@RequestParam("transactionDateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime transactionDateStart,
			@RequestParam("transactionDateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime transactionDateEnd){
		List<TransactionResponse> items = transactionService.findByTransactionTypeAndTransactionDateBetween(transactionType, transactionDateStart, transactionDateEnd);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/user-full-name")
	public ResponseEntity<List<TransactionResponse>> findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(
			@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName){
		List<TransactionResponse> items = transactionService.findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(firstName, lastName);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/user-email")
	public ResponseEntity<List<TransactionResponse>> findByUserEmailLikeIgnoreCase(@RequestParam("userEmail") String userEmail){
		List<TransactionResponse> items = transactionService.findByUserEmailLikeIgnoreCase(userEmail);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/search/source-account/{sourceAccountId}")
	public ResponseEntity<List<TransactionResponse>> findBySourceAccountId(@PathVariable Long sourceAccountId){
		List<TransactionResponse> items = transactionService.findBySourceAccountId(sourceAccountId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/search/source-account-number")
	public ResponseEntity<List<TransactionResponse>> findBySourceAccountAccountNumberContainingIgnoreCase(@RequestParam("accountNumber") String accountNumber){
		List<TransactionResponse> items = transactionService.findBySourceAccountAccountNumberContainingIgnoreCase(accountNumber);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/search/source-account-name")
	public ResponseEntity<List<TransactionResponse>> findBySourceAccountAccountName(@RequestParam("accountName") String accountName){
		List<TransactionResponse> items = transactionService.findBySourceAccountAccountName(accountName);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/search/source-account-type")
	public ResponseEntity<List<TransactionResponse>> findBySourceAccountType(@RequestParam("type") AccountType type){
		List<TransactionResponse> items = transactionService.findBySourceAccountType(type);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/search/source-account-number-and-name")
	public ResponseEntity<List<TransactionResponse>> findBySourceAccountAccountNumberContainingIgnoreCaseAndSourceAccountAccountNameContainingIgnoreCase(
			@RequestParam("accountNumber") String accountNumber,@RequestParam("accountName") String accountName){
		List<TransactionResponse> items = transactionService.findBySourceAccountAccountNumberContainingIgnoreCaseAndSourceAccountAccountNameContainingIgnoreCase(accountNumber, accountName);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/target-account/{targetAccountId}")
	public ResponseEntity<List<TransactionResponse>> findByTargetAccountId(@PathVariable Long targetAccountId){
		List<TransactionResponse> items = transactionService.findByTargetAccountId(targetAccountId);
		return ResponseEntity.ok(items);
	}
	 
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/search/target-account-number")
	public ResponseEntity<List<TransactionResponse>> findByTargetAccountAccountNumberContainingIgnoreCase(@RequestParam("accountNumber") String accountNumber){
		List<TransactionResponse> items = transactionService.findByTargetAccountAccountNumberContainingIgnoreCase(accountNumber);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/search/target-account-name")
	public ResponseEntity<List<TransactionResponse>> findByTargetAccountAccountName(@RequestParam("accountName") String accountName){
		List<TransactionResponse> items = transactionService.findByTargetAccountAccountName(accountName);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/search/target-account-type")
	public ResponseEntity<List<TransactionResponse>> findByTargetAccountType(@RequestParam("type") AccountType type){
		List<TransactionResponse> items = transactionService.findByTargetAccountType(type);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/search/target-account-number-and-name")
	public ResponseEntity<List<TransactionResponse>> findByTargetAccountAccountNumberContainingIgnoreCaseAndTargetAccountAccountNameContainingIgnoreCase(
			@RequestParam("accountNumber") String accountNumber,@RequestParam("accountName") String accountName){
		List<TransactionResponse> items = transactionService.findByTargetAccountAccountNumberContainingIgnoreCaseAndTargetAccountAccountNameContainingIgnoreCase(accountNumber, accountName);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/search/amount-range-and-transaction-date-range")
	public ResponseEntity<List<TransactionResponse>> findByAmountBetweenAndTransactionDateBetween(@RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max,
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<TransactionResponse> items = transactionService.findByAmountBetweenAndTransactionDateBetween(min, max, start, end);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/user/{userId}/transaction-date-between")
	public ResponseEntity<List<TransactionResponse>> findByUserIdAndTransactionDateBetween(@PathVariable Long userId,
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<TransactionResponse> items = transactionService.findByUserIdAndTransactionDateBetween(userId, start, end);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/source-account/{accountId}/transaction-type-date-between")
	public ResponseEntity<List<TransactionResponse>> findBySourceAccountIdAndTransactionTypeAndTransactionDateBetween(
		    @PathVariable Long accountId,
		    @RequestParam("transactionType") TransactionType transactionType,
		    @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
		    @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<TransactionResponse> items = transactionService.findBySourceAccountIdAndTransactionTypeAndTransactionDateBetween(accountId, transactionType, start, end);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/sum-of-outgoing-transactions/account/{accountId}")
	public ResponseEntity<BigDecimal> sumOfOutgoingTransactions(@PathVariable Long accountId){
		BigDecimal items = transactionService.sumOfOutgoingTransactions(accountId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/sum-of-incoming-transactions/account/{accountId}")
	public ResponseEntity<BigDecimal> sumOfIncomingTransactions(@PathVariable Long accountId){
		BigDecimal items = transactionService.sumOfIncomingTransactions(accountId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/exists/source-account-number")
	public ResponseEntity<Boolean> existsBySourceAccount_AccountNumberContainingIgnoreCase(@RequestParam("accountNumber") String accountNumber){
		Boolean items = transactionService.existsBySourceAccount_AccountNumberContainingIgnoreCase(accountNumber);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/exists/source-account-name")
	public ResponseEntity<Boolean> existsBySourceAccount_AccountNameContainingIgnoreCase(@RequestParam("accountName") String accountName){
		Boolean items = transactionService.existsBySourceAccount_AccountNameContainingIgnoreCase(accountName);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/exists/target-account-number")
	public ResponseEntity<Boolean> existsByTargetAccount_AccountNumberContainingIgnoreCase(@RequestParam("accountNumber") String accountNumber){
		Boolean items = transactionService.existsByTargetAccount_AccountNumberContainingIgnoreCase(accountNumber);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/exists/target-account-name")
	public ResponseEntity<Boolean> existsByTargetAccount_AccountNameContainingIgnoreCase(@RequestParam("accountName") String accountName){
		Boolean items = transactionService.existsByTargetAccount_AccountNumberContainingIgnoreCase(accountName);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_READ_ACCESS)
	@GetMapping("/exists/source-account/{sourceId}/target-account/{targetId}")
	public ResponseEntity<Boolean> existsBySourceAccountIdAndTargetAccountId(@PathVariable Long sourceId,@PathVariable Long targetId){
		Boolean items = transactionService.existsBySourceAccountIdAndTargetAccountId(sourceId, targetId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_FULL_ACCESS)
	@PostMapping("/fund-transfer/{userId}")
	public ResponseEntity<TransactionResponse> fundTransfer(@RequestParam("sourceAccountNumber") String sourceAccountNumber,
			@RequestParam("targetAccountNumber") String targetAccountNumber,
			@RequestParam("paymentMethod") PaymentMethod paymentMethod,
			@PathVariable Long userId,
			@RequestParam("amount") BigDecimal amount){
		User user = userRepository.findById(userId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		TransactionResponse items = transactionService.fundTransfer(sourceAccountNumber, targetAccountNumber, paymentMethod, user, amount);
	    return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_FULL_ACCESS)
	@PostMapping("/cash-withdrawal/{userId}")
	public ResponseEntity<TransactionResponse> cashWithdrawal(@RequestParam("accountNumber") String accountNumber,
			@RequestParam("amount") BigDecimal amount,@RequestParam("paymentMethod") PaymentMethod paymentMethod, @PathVariable Long userId){
		User user = userRepository.findById(userId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		TransactionResponse items = transactionService.cashWithdrawal(accountNumber, amount, paymentMethod, user);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_FULL_ACCESS)
	@PostMapping("/deposit/{userId}")
	public ResponseEntity<TransactionResponse> deposit(@RequestParam("accountNumber") String accountNumber,
			@RequestParam("amount") BigDecimal amount,@RequestParam("paymentMethod") PaymentMethod paymentMethod, @PathVariable Long userId){
		User user = userRepository.findById(userId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		TransactionResponse items = transactionService.deposit(accountNumber, amount, paymentMethod, user);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_FULL_ACCESS)
	@PostMapping("/make-payment/{userId}")
	public ResponseEntity<TransactionResponse> makePayment(@RequestParam("sourceAccountNumber") String sourceAccountNumber,
			@RequestParam("targetAccountNumber") String targetAccountNumber,
			@RequestParam("amount") BigDecimal amount,
			@RequestParam("paymentMethod") PaymentMethod paymentMethod, @PathVariable Long userId){
		User user = userRepository.findById(userId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		TransactionResponse items = transactionService.makePayment(sourceAccountNumber, targetAccountNumber, amount, paymentMethod, user);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.TRANSACTION_FULL_ACCESS)
	@PostMapping("/refund/{userId}/{originalTransactionId}")
	public ResponseEntity<TransactionResponse> refund(@PathVariable Long originalTransactionId, @PathVariable Long userId){
		User user = userRepository.findById(userId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		TransactionResponse items = transactionService.refund(originalTransactionId, user);
		return ResponseEntity.ok(items);
	}
}
