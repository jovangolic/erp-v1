package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.JournalEntryErrorException;
import com.jovan.erp_v1.model.JournalEntry;
import com.jovan.erp_v1.repository.JournalEntryRepository;
import com.jovan.erp_v1.request.JournalEntryRequest;
import com.jovan.erp_v1.response.JournalEntryResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JournalEntryService implements IJournalEntryService {

    private final JournalEntryRepository journalEntryRepository;

    @Transactional
    @Override
    public JournalEntryResponse create(JournalEntryRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Transactional
    @Override
    public JournalEntryResponse create(Long id, JournalEntryRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!journalEntryRepository.existsById(id)) {
            throw new JournalEntryErrorException("JournalEntry not found with id: " + id);
        }
        journalEntryRepository.deleteById(id);
    }

    @Override
    public JournalEntryResponse findOne(Long id) {
        JournalEntry j = journalEntryRepository.findById(id)
                .orElseThrow(() -> new JournalEntryErrorException("JournalEntry not found with id: " + id));
        return new JournalEntryResponse(j);
    }

    @Override
    public List<JournalEntryResponse> findAll() {
        return journalEntryRepository.findAll().stream()
                .map(JournalEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalEntryResponse> findByDescription(String description) {
        return journalEntryRepository.findByDescription(description).stream()
                .map(JournalEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalEntryResponse> findByEntryDateBetween(LocalDateTime start, LocalDateTime end) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEntryDateBetween'");
    }

    @Override
    public List<JournalEntryResponse> findByEntryDateOn(LocalDate date) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEntryDateOn'");
    }

    @Override
    public List<JournalEntryResponse> findByEntryDateBefore(LocalDateTime dateTime) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEntryDateBefore'");
    }

    @Override
    public List<JournalEntryResponse> findByEntryDateAfter(LocalDateTime dateTime) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEntryDateAfter'");
    }

    @Override
    public List<JournalEntryResponse> findByYear(Integer year) {
        return journalEntryRepository.findByYear(year).stream()
                .map(JournalEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalEntryResponse> findByDescriptionAndEntryDateBetween(String description, LocalDateTime start,
            LocalDateTime end) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByDescriptionAndEntryDateBetween'");
    }

}
