package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
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
import com.jovan.erp_v1.request.JournalItemRequest;
import com.jovan.erp_v1.request.JournalItemSearchRequest;
import com.jovan.erp_v1.response.JournalItemResponse;
import com.jovan.erp_v1.service.IJournalItemService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/journalItems")
@PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
public class JournalItemController {

    private final IJournalItemService journalItemService;

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_FULL_ACCESS)
    @PostMapping("/create/new-journalItem")
    public ResponseEntity<JournalItemResponse> create(@Valid @RequestBody JournalItemRequest request) {
        JournalItemResponse response = journalItemService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<JournalItemResponse> update(@PathVariable Long id,
            @Valid @RequestBody JournalItemRequest request) {
        JournalItemResponse response = journalItemService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        journalItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<JournalItemResponse> findOne(@PathVariable Long id) {
        JournalItemResponse response = journalItemService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<JournalItemResponse>> findAll() {
        List<JournalItemResponse> responses = journalItemService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/account/{id}")
    public ResponseEntity<List<JournalItemResponse>> findByAccount_Id(@PathVariable Long id) {
        List<JournalItemResponse> responses = journalItemService.findByAccount_Id(id);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/by-accountNumber")
    public ResponseEntity<List<JournalItemResponse>> findByAccount_AccountNumber(
            @RequestParam("accountNumber") String accountNumber) {
        List<JournalItemResponse> responses = journalItemService.findByAccount_AccountNumber(accountNumber);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/by-accountName")
    public ResponseEntity<List<JournalItemResponse>> findByAccount_AccountName(
            @RequestParam("accountName") String accountName) {
        List<JournalItemResponse> responses = journalItemService.findByAccount_AccountName(accountName);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/by-accountType")
    public ResponseEntity<List<JournalItemResponse>> findByAccount_Type(@RequestParam("type") AccountType type) {
        List<JournalItemResponse> responses = journalItemService.findByAccount_Type(type);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/debit-greaterThan")
    public ResponseEntity<List<JournalItemResponse>> findByDebitGreaterThan(@RequestParam("amount") BigDecimal amount) {
        List<JournalItemResponse> responses = journalItemService.findByDebitGreaterThan(amount);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/debit-lessThan")
    public ResponseEntity<List<JournalItemResponse>> findByDebitLessThan(@RequestParam("amount") BigDecimal amount) {
        List<JournalItemResponse> responses = journalItemService.findByDebitLessThan(amount);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/credit-greaterThan")
    public ResponseEntity<List<JournalItemResponse>> findByCreditGreaterThan(
            @RequestParam("amount") BigDecimal amount) {
        List<JournalItemResponse> responses = journalItemService.findByCreditGreaterThan(amount);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/credit-lessThan")
    public ResponseEntity<List<JournalItemResponse>> findByCreditLessThan(@RequestParam("amount") BigDecimal amount) {
        List<JournalItemResponse> responses = journalItemService.findByCreditLessThan(amount);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/by-debit")
    public ResponseEntity<List<JournalItemResponse>> findByDebit(@RequestParam("debit") BigDecimal debit) {
        List<JournalItemResponse> responses = journalItemService.findByDebit(debit);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/by-credit")
    public ResponseEntity<List<JournalItemResponse>> findByCredit(@RequestParam("credit") BigDecimal credit) {
        List<JournalItemResponse> responses = journalItemService.findByCredit(credit);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/journalEntry/{journalEntryId}")
    public ResponseEntity<List<JournalItemResponse>> findByJournalEntry_Id(@PathVariable Long journalEntryId) {
        List<JournalItemResponse> responses = journalItemService.findByJournalEntry_Id(journalEntryId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/search")
    public ResponseEntity<List<JournalItemResponse>> search(@RequestParam("request") JournalItemSearchRequest request) {
        List<JournalItemResponse> responses = journalItemService.search(request);
        return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/by-entry-date")
    public ResponseEntity<List<JournalItemResponse>> findByJournalEntry_EntryDate(@RequestParam("entryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime entryDate){
    	List<JournalItemResponse> responses = journalItemService.findByJournalEntry_EntryDate(entryDate);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/by-date-range")
    public ResponseEntity<List<JournalItemResponse>> findByJournalEntry_EntryDateBetween(@RequestParam("entryDateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime entryDateStart,
            @RequestParam("entryDateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime entryDateEnd){
    	List<JournalItemResponse> responses = journalItemService.findByJournalEntry_EntryDateBetween(entryDateStart, entryDateEnd);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.JOURNAL_ITEM_READ_ACCESS)
    @GetMapping("/by-description")
    public ResponseEntity<List<JournalItemResponse>> findByJournalEntry_Description(@RequestParam("description") String description){
    	List<JournalItemResponse> responses = journalItemService.findByJournalEntry_Description(description);
    	return ResponseEntity.ok(responses);
    }
}
