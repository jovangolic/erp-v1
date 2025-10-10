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

import com.jovan.erp_v1.enumeration.AccountStatus;
import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.request.AccountRequest;
import com.jovan.erp_v1.response.AccountResponse;
import com.jovan.erp_v1.response.AccountWithTransactionsResponse;
import com.jovan.erp_v1.save_as.AccountSaveAsRequest;
import com.jovan.erp_v1.search_request.AccountSearchRequest;
import com.jovan.erp_v1.service.IAccountService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
@CrossOrigin("htttp://5173")
@PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
public class AccountController {

    private final IAccountService accountService;

    @PreAuthorize(RoleGroups.ACCOUNT_FULL_ACCESS)
    @PostMapping("/create/new-account")
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.ACCOUNT_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<AccountResponse> update(@PathVariable Long id, @RequestBody AccountRequest request) {
        AccountResponse response = accountService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.ACCOUNT_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<AccountResponse> findOne(@PathVariable Long id) {
        AccountResponse response = accountService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<AccountResponse>> findAll() {
        List<AccountResponse> responses = accountService.findAll();
        return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
    // GET /accounts/detailed-account/{id} -> jedan racun sa transakcijama
    @GetMapping("/detailed-account/{id}")
    public ResponseEntity<AccountWithTransactionsResponse> findOneWithTransactions(@PathVariable Long id){
    	AccountWithTransactionsResponse responses = accountService.findOneWithTransactions(id);
    	return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
    @GetMapping("/by-account-type")
    public ResponseEntity<List<AccountResponse>> findByType(@RequestParam("type") AccountType type) {
        List<AccountResponse> responses = accountService.findByType(type);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
    @GetMapping("/by-balance")
    public ResponseEntity<List<AccountResponse>> findByBalance(@RequestParam("balance") BigDecimal balance) {
        List<AccountResponse> responses = accountService.findByBalance(balance);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
    @GetMapping("/balance-between")
    public ResponseEntity<List<AccountResponse>> findByBalanceBetween(@RequestParam("min") BigDecimal min,
            @RequestParam("max") BigDecimal max) {
        List<AccountResponse> responses = accountService.findByBalanceBetween(min, max);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
    @GetMapping("/balance-greater-than")
    public ResponseEntity<List<AccountResponse>> findByBalanceGreaterThan(@RequestParam("amount") BigDecimal amount) {
        List<AccountResponse> responses = accountService.findByBalanceGreaterThan(amount);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
    @GetMapping("/balance-less-than")
    public ResponseEntity<List<AccountResponse>> findByBalanceLessThan(@RequestParam("amount") BigDecimal amount) {
        List<AccountResponse> responses = accountService.findByBalanceLessThan(amount);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
    @GetMapping("/by-accountName")
    public ResponseEntity<AccountResponse> findByAccountName(@RequestParam("accountName") String accountName) {
        AccountResponse response = accountService.findByAccountName(accountName);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
    @GetMapping("/by-accName-ignoreCase")
    public ResponseEntity<AccountResponse> findByAccountNameIgnoreCase(
            @RequestParam("accountName") String accountName) {
        AccountResponse response = accountService.findByAccountNameIgnoreCase(accountName);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
    @GetMapping("/by-accountNumber")
    public ResponseEntity<AccountResponse> findByAccountNumber(@RequestParam("accountNumber") String accountNumber) {
        AccountResponse response = accountService.findByAccountNumber(accountNumber);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
    @GetMapping("/by-accNumber-and-accName")
    public ResponseEntity<AccountResponse> findByAccountNameAndAccountNumber(
            @RequestParam("accountName") String accountName, @RequestParam("accountNumber") String accountNumber) {
        AccountResponse response = accountService.findByAccountNameAndAccountNumber(accountName, accountNumber);
        return ResponseEntity.ok(response);
    }

    //nove metode
    
    @PreAuthorize(RoleGroups.ACCOUNT_FULL_ACCESS)
    @PostMapping("/{id}/confirm")
    public ResponseEntity<AccountResponse> confirmAccount(Long id){
    	AccountResponse items = accountService.confirmAccount(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.ACCOUNT_FULL_ACCESS)
    @PostMapping("/{id}/cancel")
    public ResponseEntity<AccountResponse> cancelAccount(Long id){
    	AccountResponse items = accountService.cancelAccount(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.ACCOUNT_FULL_ACCESS)
    @PostMapping("/{id}/close")
    public ResponseEntity<AccountResponse> closeAccount(Long id){
    	AccountResponse items = accountService.closeAccount(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.ACCOUNT_FULL_ACCESS)
    @PostMapping("/{id}/status/{status}")
    public ResponseEntity<AccountResponse> changeStatus(@PathVariable Long id,@PathVariable AccountStatus status){
    	AccountResponse items = accountService.changeStatus(id, status);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
    @GetMapping("/track-source/{id}")
    public ResponseEntity<AccountResponse> trackAccountSourceTransactions(@PathVariable Long id){
    	AccountResponse items = accountService.trackAccountSourceTransactions(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
    @GetMapping("/track-target/{id}")
    public ResponseEntity<AccountResponse> trackAccountTargetTransactions(@PathVariable Long id){
    	AccountResponse items = accountService.trackAccountTargetTransactions(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.ACCOUNT_READ_ACCESS)
    @GetMapping("/track-all/{id}")
    public ResponseEntity<AccountResponse> trackAll(@PathVariable Long id){
    	AccountResponse items = accountService.trackAll(id);
    	return ResponseEntity.ok(items);
    }
     
    @PreAuthorize(RoleGroups.ACCOUNT_FULL_ACCESS)
    @PostMapping("/general-search")
    public ResponseEntity<List<AccountResponse>> generalSearch(@RequestBody AccountSearchRequest request){
    	List<AccountResponse> items = accountService.generalSearch(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.ACCOUNT_FULL_ACCESS)
    @PostMapping("/save")
    public ResponseEntity<AccountResponse> saveAccount(@Valid @RequestBody AccountRequest request){
    	AccountResponse items = accountService.saveAccount(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.ACCOUNT_FULL_ACCESS)
    @PostMapping("/save-as")
    public ResponseEntity<AccountResponse> saveAs(@Valid @RequestBody AccountSaveAsRequest request){
    	AccountResponse items = accountService.saveAs(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.ACCOUNT_FULL_ACCESS)
    @PostMapping("/save-all")
    public ResponseEntity<List<AccountResponse>> saveAll(@Valid @RequestBody List<AccountRequest> request){
    	List<AccountResponse> items = accountService.saveAll(request);
    	return ResponseEntity.ok(items);
    }
}
