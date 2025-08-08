package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.enumeration.LedgerType;
import com.jovan.erp_v1.request.LedgerEntryRequest;
import com.jovan.erp_v1.response.LedgerEntryResponse;
import com.jovan.erp_v1.service.ILedgerEntryService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ledgerEntries")
@PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
public class LedgerEntryController {

    private final ILedgerEntryService ledgerEntryService;

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_FULL_ACCESS)
    @PostMapping("/create/new-ledgerEntry")
    public ResponseEntity<LedgerEntryResponse> create(@Valid @RequestBody LedgerEntryRequest request) {
        LedgerEntryResponse response = ledgerEntryService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<LedgerEntryResponse> update(@PathVariable Long id,
            @Valid @RequestBody LedgerEntryRequest request) {
        LedgerEntryResponse response = ledgerEntryService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ledgerEntryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<LedgerEntryResponse> findOne(@PathVariable Long id) {
        LedgerEntryResponse response = ledgerEntryService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<LedgerEntryResponse>> findAll() {
        List<LedgerEntryResponse> responses = ledgerEntryService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/by-type")
    public ResponseEntity<List<LedgerEntryResponse>> findByType(@RequestParam("type") LedgerType type) {
        List<LedgerEntryResponse> responses = ledgerEntryService.findByType(type);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/amount-between-min-max")
    public ResponseEntity<List<LedgerEntryResponse>> findByAmountBetween(@RequestParam("min") BigDecimal min,
            @RequestParam("max") BigDecimal max) {
        List<LedgerEntryResponse> responses = ledgerEntryService.findByAmountBetween(min, max);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/by-description")
    public ResponseEntity<List<LedgerEntryResponse>> findByDescriptionContainingIgnoreCase(
            @RequestParam("keyword") String keyword) {
        List<LedgerEntryResponse> responses = ledgerEntryService.findByDescriptionContainingIgnoreCase(keyword);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/entryDateBetween")
    public ResponseEntity<List<LedgerEntryResponse>> findByEntryDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<LedgerEntryResponse> responses = ledgerEntryService.findByEntryDateBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/account/{id}")
    public ResponseEntity<List<LedgerEntryResponse>> findByAccount_Id(@PathVariable Long id) {
        List<LedgerEntryResponse> responses = ledgerEntryService.findByAccount_Id(id);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/accountNumber")
    public ResponseEntity<List<LedgerEntryResponse>> findByAccount_AccountNumber(
            @RequestParam("accountNumber") String accountNumber) {
        List<LedgerEntryResponse> responses = ledgerEntryService.findByAccount_AccountNumber(accountNumber);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/accountName")
    public ResponseEntity<List<LedgerEntryResponse>> findByAccount_AccountName(
            @RequestParam("accountName") String accountName) {
        List<LedgerEntryResponse> responses = ledgerEntryService.findByAccount_AccountName(accountName);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/search-by-name")
    public ResponseEntity<List<LedgerEntryResponse>> findByAccount_AccountNameContainingIgnoreCase(
            @RequestParam("name") String name) {
        List<LedgerEntryResponse> responses = ledgerEntryService.findByAccount_AccountNameContainingIgnoreCase(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/account-type")
    public ResponseEntity<List<LedgerEntryResponse>> findByAccount_Type(@RequestParam("type") AccountType type) {
        List<LedgerEntryResponse> responses = ledgerEntryService.findByAccount_Type(type);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/account-balance")
    public ResponseEntity<List<LedgerEntryResponse>> findByAccount_Balance(
            @RequestParam("balance") BigDecimal balance) {
        List<LedgerEntryResponse> responses = ledgerEntryService.findByAccount_Balance(balance);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/entryDateEquals")
    public ResponseEntity<List<LedgerEntryResponse>> findByEntryDateEquals(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<LedgerEntryResponse> responses = ledgerEntryService.findByEntryDateEquals(date);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/entryDateBefore")
    public ResponseEntity<List<LedgerEntryResponse>> findByEntryDateBefore(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<LedgerEntryResponse> responses = ledgerEntryService.findByEntryDateBefore(date);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/entryDateAfter")
    public ResponseEntity<List<LedgerEntryResponse>> findByEntryDateAfter(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<LedgerEntryResponse> responses = ledgerEntryService.findByEntryDateAfter(date);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/dateAfter-type")
    public ResponseEntity<List<LedgerEntryResponse>> findByEntryDateAfterAndType(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam("type") LedgerType type) {
        List<LedgerEntryResponse> responses = ledgerEntryService.findByEntryDateAfterAndType(date, type);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.LEDGER_ENTRY_READ_ACCESS)
    @GetMapping("/account/{accountId}/date-range")
    public ResponseEntity<List<LedgerEntryResponse>> findByEntryDateBetweenAndAccount_Id(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @PathVariable Long accountId) {
        List<LedgerEntryResponse> responses = ledgerEntryService.findByEntryDateBetweenAndAccount_Id(start, end,
                accountId);
        return ResponseEntity.ok(responses);
    }

}
