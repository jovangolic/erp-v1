package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jovan.erp_v1.model.JournalEntry;
import com.jovan.erp_v1.request.JournalEntryRequest;
import com.jovan.erp_v1.response.JournalEntryResponse;

public interface IJournalEntryService {

    JournalEntryResponse create(JournalEntryRequest request);

    JournalEntryResponse create(Long id, JournalEntryRequest request);

    void delete(Long id);

    JournalEntryResponse findOne(Long id);

    List<JournalEntryResponse> findAll();

    List<JournalEntryResponse> findByDescription(String description);

    List<JournalEntryResponse> findByEntryDateBetween(LocalDateTime start, LocalDateTime end);

    List<JournalEntryResponse> findByEntryDateOn(LocalDate date);

    List<JournalEntryResponse> findByEntryDateBefore(LocalDateTime dateTime);

    List<JournalEntryResponse> findByEntryDateAfter(LocalDateTime dateTime);

    List<JournalEntryResponse> findByYear(Integer year);

    List<JournalEntryResponse> findByDescriptionAndEntryDateBetween(String description, LocalDateTime start,
            LocalDateTime end);
}
