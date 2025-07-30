package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.JournalEntryErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.JournalEntryMapper;
import com.jovan.erp_v1.mapper.JournalItemMapper;
import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.model.JournalEntry;
import com.jovan.erp_v1.model.JournalItem;
import com.jovan.erp_v1.repository.AccountRepository;
import com.jovan.erp_v1.repository.JournalEntryRepository;
import com.jovan.erp_v1.request.JournalEntryRequest;
import com.jovan.erp_v1.request.JournalItemRequest;
import com.jovan.erp_v1.response.JournalEntryResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JournalEntryService implements IJournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final JournalEntryMapper journalEntryMapper;
    private final AccountRepository accountRepository;
    private final JournalItemMapper journalItemMapper;

    @Transactional
    @Override
    public JournalEntryResponse create(JournalEntryRequest request) {
    	DateValidator.validateNotNull(request.entryDate(), "Date and Time");
    	validateString(request.description(), "Description");
    	if (request.itemRequests() == null || request.itemRequests().isEmpty()) {
    	    throw new IllegalArgumentException("Lista stavki ne sme biti prazna.");
    	}
        JournalEntry entry = journalEntryMapper.toEntity(request);
        List<JournalItem> items = mapRequestToItems(request.itemRequests(), entry);
        entry.setItems(items);
        JournalEntry saved = journalEntryRepository.save(entry);
        return journalEntryMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public JournalEntryResponse update(Long id, JournalEntryRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        JournalEntry j = journalEntryRepository.findById(id)
                .orElseThrow(() -> new JournalEntryErrorException("JournalEntry not found with id: " + id));
        DateValidator.validateNotNull(request.entryDate(), "Date and Time");
    	validateString(request.description(), "Description");
    	if (request.itemRequests() == null || request.itemRequests().isEmpty()) {
    	    throw new IllegalArgumentException("Lista stavki ne sme biti prazna prilikom ažuriranja unosa.");
    	}
        journalEntryMapper.toUpdateEntity(j, request);
        //j.getItems().clear(); ako se budi ovaj deo koda brisao iz mappera, onda se mora u servisnoj metodi raditi ovo brisanje
        List<JournalItem> items = mapRequestToItems(request.itemRequests(), j);
        j.setItems(items);
        return journalEntryMapper.toResponse(journalEntryRepository.save(j));
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
    	List<JournalEntry> items = journalEntryRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("JournalEntry list is empty");
    	}
        return journalEntryRepository.findAll().stream()
                .map(JournalEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalEntryResponse> findByDescription(String description) {
    	validateString(description, "Description");
    	List<JournalEntry> items = journalEntryRepository.findByDescription(description);
    	if(items.isEmpty()) {
    		String msg = String.format("No JournalEntry found for desription %s", description);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(JournalEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalEntryResponse> findByEntryDateBetween(LocalDateTime start, LocalDateTime end) {
        validateDateRange(start, end);
        List<JournalEntry> entries = journalEntryRepository.findByEntryDateBetween(start, end);
        if(entries.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        	String msg = String.format("No JournalEntry found for date between %s and %s", 
        			start.format(formatter),end.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return journalEntryMapper.toResponseList(entries);
    }

    @Override
    public List<JournalEntryResponse> findByEntryDateOn(LocalDate date) {
        if (date == null) {
            throw new JournalEntryErrorException("Date must be provided");
        }
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // 23:59:59.999999999
        List<JournalEntry> entries = journalEntryRepository.findByEntryDateBetween(startOfDay, endOfDay);
        if(entries.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        	String msg = String.format("No JournalEntry found for date on %s", date.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return journalEntryMapper.toResponseList(entries);
    }

    @Override
    public List<JournalEntryResponse> findByEntryDateBefore(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new JournalEntryErrorException("Date and time must be provided");
        }
        List<JournalEntry> entries = journalEntryRepository.findByEntryDateBefore(dateTime);
        if(entries.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        	String msg = String.format("No JournalEntry found for date before %s", dateTime.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return journalEntryMapper.toResponseList(entries);
    }

    @Override
    public List<JournalEntryResponse> findByEntryDateAfter(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new JournalEntryErrorException("Date and time must be provided");
        }
        List<JournalEntry> entries = journalEntryRepository.findByEntryDateAfter(dateTime);
        if(entries.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        	String msg = String.format("No JournalEntry found for date after %s", dateTime.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return journalEntryMapper.toResponseList(entries);
    }

    @Override
    public List<JournalEntryResponse> findByYear(Integer year) {
    	validateInteger(year);
    	List<JournalEntry> items = journalEntryRepository.findByYear(year);
    	if(items.isEmpty()) {
    		String msg = String.format("No JournalEntry found for given year %d", year);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(JournalEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalEntryResponse> findByDescriptionAndEntryDateBetween(String description, LocalDateTime start,
            LocalDateTime end) {
        if (description == null) {
            throw new IllegalArgumentException("Description must be provided");
        }
        validateDateRange(start, end);
        List<JournalEntry> entries = journalEntryRepository.findByDescriptionAndEntryDateBetween(description, start,
                end);
        if(entries.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        	String msg = String.format("No JournalEntry found for description %s and date between %s and %s", 
        			description,start.format(formatter),end.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return journalEntryMapper.toResponseList(entries);
    }

    private void validateDateRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new JournalEntryErrorException("Date and time must be provided");
        }
        if (start.isAfter(end)) {
            throw new JournalEntryErrorException("Start date and time must not be after end date and time");
        }
    }
    
    private void validateString(String str, String fieldName) {
    	if(str == null || str.trim().isEmpty())	{
    		throw new IllegalArgumentException("Must be string not empty or null");
    	}
    }
    
    private void validateInteger(Integer num) {
    	if(num == null || num < 0) {
    		throw new IllegalArgumentException("Number must be positive");
    	}
    }
    
    private List<JournalItem> mapRequestToItems(List<JournalItemRequest> itemRequests, JournalEntry entry) {
        return itemRequests.stream()
            .map(itemReq -> {
                Account account = accountRepository.findById(itemReq.accountId())
                    .orElseThrow(() -> new ValidationException("Account with ID " + itemReq.accountId() + " not found"));
                return journalItemMapper.toEntity(itemReq, account, entry);
            })
            .collect(Collectors.toList());
    }

}
