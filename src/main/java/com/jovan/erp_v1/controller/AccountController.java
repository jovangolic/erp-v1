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

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.request.AccountRequest;
import com.jovan.erp_v1.response.AccountResponse;
import com.jovan.erp_v1.service.IAccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
@CrossOrigin("htttp://5173")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
public class AccountController {

    private final IAccountService accountService;

    @PostMapping("/create/new-account")
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AccountResponse> update(@PathVariable Long id, @RequestBody AccountRequest request) {
        AccountResponse response = accountService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<AccountResponse> findOne(@PathVariable Long id) {
        AccountResponse response = accountService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<AccountResponse>> findAll() {
        List<AccountResponse> responses = accountService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-account-type")
    public ResponseEntity<List<AccountResponse>> findByType(@RequestParam("type") AccountType type) {
        List<AccountResponse> responses = accountService.findByType(type);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-balance")
    public ResponseEntity<List<AccountResponse>> findByBalance(BigDecimal balance) {
        List<AccountResponse> responses = accountService.findByBalance(balance);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/balance-between")
    public ResponseEntity<List<AccountResponse>> findByBalanceBetween(BigDecimal min, BigDecimal max) {
        List<AccountResponse> responses = accountService.findByBalanceBetween(min, max);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/balance-greater-than")
    public ResponseEntity<List<AccountResponse>> findByBalanceGreaterThan(BigDecimal amount) {
        List<AccountResponse> responses = accountService.findByBalanceGreaterThan(amount);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/balance-less-than")
    public ResponseEntity<List<AccountResponse>> findByBalanceLessThan(BigDecimal amount) {
        List<AccountResponse> responses = accountService.findByBalanceLessThan(amount);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-accountName")
    public ResponseEntity<AccountResponse> findByAccountName(String accountName) {
        AccountResponse response = accountService.findByAccountName(accountName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-accName-ignoreCase")
    public ResponseEntity<AccountResponse> findByAccountNameIgnoreCase(String accountName) {
        AccountResponse response = accountService.findByAccountNameIgnoreCase(accountName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-accountNumber")
    public ResponseEntity<AccountResponse> findByAccountNumber(String accountNumber) {
        AccountResponse response = accountService.findByAccountNumber(accountNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-accNumber-and-accName")
    public ResponseEntity<AccountResponse> findByAccountNameAndAccountNumber(String accountName, String accountNumber) {
        AccountResponse response = accountService.findByAccountNameAndAccountNumber(accountName, accountNumber);
        return ResponseEntity.ok(response);
    }

}
