package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.enumeration.LedgerType;
import com.jovan.erp_v1.exception.AccountNotFoundErrorException;
import com.jovan.erp_v1.exception.LedgerEntryErrorException;
import com.jovan.erp_v1.mapper.LedgerEntryMapper;
import com.jovan.erp_v1.model.LedgerEntry;
import com.jovan.erp_v1.repository.AccountRepository;
import com.jovan.erp_v1.repository.LedgerEntryRepository;
import com.jovan.erp_v1.request.LedgerEntryRequest;
import com.jovan.erp_v1.response.LedgerEntryResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerEntryService implements ILedgerEntryService {

    private final LedgerEntryRepository ledgerEntryRepository;
    private final LedgerEntryMapper ledgerEntryMapper;
    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public LedgerEntryResponse create(LedgerEntryRequest request) {
        validateRequest(request);
        LedgerEntry entry = ledgerEntryMapper.toEntity(request);
        LedgerEntry saved = ledgerEntryRepository.save(entry);
        return ledgerEntryMapper.toResponse(ledgerEntryRepository.save(saved));
    }

    @Transactional
    @Override
    public LedgerEntryResponse update(Long id, LedgerEntryRequest request) {
    	LedgerEntry entry = ledgerEntryRepository.findById(id)
                .orElseThrow(() -> new LedgerEntryErrorException("LedgerEntry not found with id: " + id));
    	validateRequest(request);
        ledgerEntryMapper.toUpdateEntity(entry, request);
        return ledgerEntryMapper.toResponse(ledgerEntryRepository.save(entry));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!ledgerEntryRepository.existsById(id)) {
            throw new LedgerEntryErrorException("LedgerEntry not found with id: " + id);
        }
        ledgerEntryRepository.deleteById(id);
    }

    @Override
    public LedgerEntryResponse findOne(Long id) {
        LedgerEntry entry = ledgerEntryRepository.findById(id)
                .orElseThrow(() -> new LedgerEntryErrorException("LedgerEntry not found with id: " + id));
        return new LedgerEntryResponse(entry);
    }

    @Override
    public List<LedgerEntryResponse> findAll() {
        return ledgerEntryRepository.findAll().stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByType(LedgerType type) {
    	validateLedgerType(type);
        return ledgerEntryRepository.findByType(type).stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByAmountBetween(BigDecimal min, BigDecimal max) {
    	validateBigDecimal(max);
    	validateBigDecimal(min);
        return ledgerEntryRepository.findByAmountBetween(min, max).stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByDescriptionContainingIgnoreCase(String keyword) {
    	validateString(keyword);
        return ledgerEntryRepository.findByDescriptionContainingIgnoreCase(keyword).stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByEntryDateBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new LedgerEntryErrorException("Both start and end dates must be provided.");
        }
        if (start.isAfter(end)) {
            throw new LedgerEntryErrorException("Start date must be before or equal to end date.");
        }
        if (!start.isBefore(end)) {
            throw new LedgerEntryErrorException("Start date must be strictly before end date.");
        }
        List<LedgerEntry> entries = ledgerEntryRepository.findByEntryDateBetween(start, end);
        return ledgerEntryMapper.toResponseList(entries);
    }

    @Override
    public List<LedgerEntryResponse> findByAccount_Id(Long id) {
        return ledgerEntryRepository.findByAccount_Id(id).stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByAccount_AccountNumber(String accountNumber) {
    	if(!ledgerEntryRepository.existsByAccount_AccountNumber(accountNumber)) {
    		throw new LedgerEntryErrorException("AccountNumber not found");
    	}
        return ledgerEntryRepository.findByAccount_AccountNumber(accountNumber).stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByAccount_AccountName(String accountName) {
    	if(!ledgerEntryRepository.existsByAccount_AccountName(accountName)) {
    		throw new LedgerEntryErrorException("AccountName not found");
    	}
        return ledgerEntryRepository.findByAccount_AccountName(accountName).stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByAccount_AccountNameContainingIgnoreCase(String name) {
    	validateString(name);
        return ledgerEntryRepository.findByAccount_AccountNameContainingIgnoreCase(name).stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByAccount_Type(AccountType type) {
    	validateAccountType(type);
        return ledgerEntryRepository.findByAccount_Type(type).stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByAccount_Balance(BigDecimal balance) {
    	validateBigDecimal(balance);
        return ledgerEntryRepository.findByAccount_Balance(balance).stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByEntryDateEquals(LocalDate date) {
        if (date == null) {
            throw new LedgerEntryErrorException("Date must be provided.");
        }
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<LedgerEntry> entries = ledgerEntryRepository
                .findByEntryDateBetween(startOfDay, endOfDay);
        return ledgerEntryMapper.toResponseList(entries);
    }

    @Override
    public List<LedgerEntryResponse> findByEntryDateBefore(LocalDateTime date) {
        if (date == null) {
            throw new LedgerEntryErrorException("Date must be provided.");
        }
        List<LedgerEntry> entries = ledgerEntryRepository.findByEntryDateBefore(date);
        return ledgerEntryMapper.toResponseList(entries);
    }

    @Override
    public List<LedgerEntryResponse> findByEntryDateAfter(LocalDateTime date) {
        if (date == null) {
            throw new LedgerEntryErrorException("Date must be provided.");
        }
        List<LedgerEntry> entries = ledgerEntryRepository.findByEntryDateAfter(date);
        return ledgerEntryMapper.toResponseList(entries);
    }

    @Override
    public List<LedgerEntryResponse> findByEntryDateAfterAndType(LocalDateTime date, LedgerType type) {
        if (date == null || type == null) {
            throw new LedgerEntryErrorException("Type and date must be provided.");
        }
        List<LedgerEntry> entries = ledgerEntryRepository.findByEntryDateAfterAndType(date, type);
        return ledgerEntryMapper.toResponseList(entries);
    }

    @Override
    public List<LedgerEntryResponse> findByEntryDateBetweenAndAccount_Id(LocalDateTime start, LocalDateTime end,
            Long accountId) {
        if (start == null || end == null || accountId == null) {
            throw new LedgerEntryErrorException("Start date, end date, and account ID must be provided.");
        }
        List<LedgerEntry> entries = ledgerEntryRepository
                .findByEntryDateBetweenAndAccount_Id(start, end, accountId);
        return ledgerEntryMapper.toResponseList(entries);
    }
    
    private void validateLedgerType(LedgerType type) {
    	if(type == null) {
    		throw new IllegalArgumentException("LedgerType type must not be null");
    	}
    }
    
    private void validateAccountType(AccountType type) {
    	if(type == null) {
    		throw new IllegalArgumentException("AccountType type must not be null");
    	}
    }
    
    private void validateString(String str) {
    	if(str == null) {
    		throw new IllegalArgumentException("Given string must not be null nor empty");
    	}
    }

    private void validateBigDecimal(BigDecimal num) {
    	if(num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
    		throw new IllegalArgumentException("Number must be positive");
    	}
    }
    
   private void validateAccountId(Long accountId) {
	   if(accountId == null) {
		   throw new AccountNotFoundErrorException("Account ID must not be null");
	   }
	   if(!accountRepository.existsById(accountId)) {
		   throw new AccountNotFoundErrorException("Account not found with id "+accountId);
	   }
   }
   
   private void validateRequest(LedgerEntryRequest request) {
	   if (request.entryDate().isAfter(LocalDateTime.now().plusYears(5))) {
           throw new LedgerEntryErrorException("Entry date is too far in the future.");
       }
       if (request.entryDate().isBefore(LocalDateTime.of(2000, 1, 1, 0, 0))) {
           throw new LedgerEntryErrorException("Entry date is too far in the past.");
       }
       if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
           throw new LedgerEntryErrorException("Amount must be a positive value.");
       }
       validateBigDecimal(request.amount());
       validateString(request.description());
       validateAccountId(request.accountId());
       validateLedgerType(request.type());
   }
}
