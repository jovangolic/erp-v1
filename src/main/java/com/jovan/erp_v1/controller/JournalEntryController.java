package com.jovan.erp_v1.controller;

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

import com.jovan.erp_v1.request.JournalEntryRequest;
import com.jovan.erp_v1.response.JournalEntryResponse;
import com.jovan.erp_v1.service.IJournalEntryService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/journalEntries")
@PreAuthorize(RoleGroups.JOURNAL_ENTRY_READ_ACCESS)
public class JournalEntryController {

    private final IJournalEntryService journalEntryService;

    @PreAuthorize(RoleGroups.JOURNAL_ENTRY_FULL_ACCESS)
    @PostMapping("/create/new-journalEntry")
    public ResponseEntity<JournalEntryResponse> create(@Valid @RequestBody JournalEntryRequest request) {
        JournalEntryResponse response = journalEntryService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ENTRY_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<JournalEntryResponse> update(@PathVariable Long id,
            @Valid @RequestBody JournalEntryRequest request) {
        JournalEntryResponse response = journalEntryService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ENTRY_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        journalEntryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.JOURNAL_ENTRY_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<JournalEntryResponse> findOne(@PathVariable Long id) {
        JournalEntryResponse response = journalEntryService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ENTRY_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<JournalEntryResponse>> findAll() {
        List<JournalEntryResponse> responses = journalEntryService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ENTRY_READ_ACCESS)
    @GetMapping("/by-description")
    public ResponseEntity<List<JournalEntryResponse>> findByDescription(
            @RequestParam("description") String description) {
        List<JournalEntryResponse> responses = journalEntryService.findByDescription(description);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ENTRY_READ_ACCESS)
    @GetMapping("/entry-date-between")
    public ResponseEntity<List<JournalEntryResponse>> findByEntryDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<JournalEntryResponse> responses = journalEntryService.findByEntryDateBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ENTRY_READ_ACCESS)
    @GetMapping("/entry-dateOn")
    public ResponseEntity<List<JournalEntryResponse>> findByEntryDateOn(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<JournalEntryResponse> responses = journalEntryService.findByEntryDateOn(date);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ENTRY_READ_ACCESS)
    @GetMapping("/entry-date-before")
    public ResponseEntity<List<JournalEntryResponse>> findByEntryDateBefore(
            @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        List<JournalEntryResponse> responses = journalEntryService.findByEntryDateBefore(dateTime);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ENTRY_READ_ACCESS)
    @GetMapping("/entry-date-after")
    public ResponseEntity<List<JournalEntryResponse>> findByEntryDateAfter(
            @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        List<JournalEntryResponse> responses = journalEntryService.findByEntryDateAfter(dateTime);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ENTRY_READ_ACCESS)
    @GetMapping("/by-year")
    public ResponseEntity<List<JournalEntryResponse>> findByYear(@RequestParam("year") Integer year) {
        List<JournalEntryResponse> responses = journalEntryService.findByYear(year);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.JOURNAL_ENTRY_READ_ACCESS)
    @GetMapping("/by-description-and-date")
    public ResponseEntity<List<JournalEntryResponse>> findByDescriptionAndEntryDateBetween(
            @RequestParam("description") String description,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<JournalEntryResponse> responses = journalEntryService.findByDescriptionAndEntryDateBetween(description,
                start, end);
        return ResponseEntity.ok(responses);
    }

}
