package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.enumeration.LedgerType;
import com.jovan.erp_v1.exception.AccountNotFoundErrorException;
import com.jovan.erp_v1.exception.LedgerEntryErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.LedgerEntryMapper;
import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.model.LedgerEntry;
import com.jovan.erp_v1.repository.AccountRepository;
import com.jovan.erp_v1.repository.LedgerEntryRepository;
import com.jovan.erp_v1.request.LedgerEntryRequest;
import com.jovan.erp_v1.response.LedgerEntryResponse;
import com.jovan.erp_v1.util.DateValidator;

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
        Account account = validateAccountId(request.accountId());;
        LedgerEntry entry = ledgerEntryMapper.toEntity(request,account);
        LedgerEntry saved = ledgerEntryRepository.save(entry);
        return ledgerEntryMapper.toResponse(ledgerEntryRepository.save(saved));
    }

    @Transactional
    @Override
    public LedgerEntryResponse update(Long id, LedgerEntryRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
    	LedgerEntry entry = ledgerEntryRepository.findById(id)
                .orElseThrow(() -> new LedgerEntryErrorException("LedgerEntry not found with id: " + id));
    	validateUpdateRequest(request);
    	Account acc = entry.getAccount();
    	if(request.accountId() != null && (acc.getId() == null || !request.accountId().equals(acc.getId()))) {
    		acc = validateAccountId(request.accountId());
    	}
        ledgerEntryMapper.toUpdateEntity(entry, request,acc);
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
    	List<LedgerEntry> items = ledgerEntryRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("LedgerEntry list is empty");
    	}
        return ledgerEntryRepository.findAll().stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByType(LedgerType type) {
    	validateLedgerType(type);
    	List<LedgerEntry> items = ledgerEntryRepository.findByType(type);
    	if(items.isEmpty()) {
    		String msg = String.format("No LedgerEnrty for type %s is found", type);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByAmountBetween(BigDecimal min, BigDecimal max) {
    	validateMinAndMax(min, max);
    	List<LedgerEntry> items = ledgerEntryRepository.findByAmountBetween(min, max);
    	if(items.isEmpty()) {
    		String msg = String.format("No LedgerEntry for amount between %s and %s is found", min,max);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByDescriptionContainingIgnoreCase(String keyword) {
    	validateString(keyword);
    	List<LedgerEntry> items = ledgerEntryRepository.findByDescriptionContainingIgnoreCase(keyword);
    	if(items.isEmpty()) {
    		String msg = String.format("No LedgerEntry for description containing keyword %s is found", keyword);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
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
        if(entries.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        	String msg = String.format("No LedgerEntry for entry date between %s and %s is found", 
        			start.format(formatter),end.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return ledgerEntryMapper.toResponseList(entries);
    }

    @Override
    public List<LedgerEntryResponse> findByAccount_Id(Long id) {
    	validateAccountId(id);
    	List<LedgerEntry> items = ledgerEntryRepository.findByAccount_Id(id);
    	if(items.isEmpty()) {
    		String msg = String.format("No LedgerEntry for account-id %d is found", id);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
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
    	validateString(accountName);
    	List<LedgerEntry> items = ledgerEntryRepository.findByAccount_AccountName(accountName);
    	if(items.isEmpty()) {
    		String msg = String.format("No LedgerEntry for account-name %s is found", accountName);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByAccount_AccountNameContainingIgnoreCase(String name) {
    	validateString(name);
    	List<LedgerEntry> items = ledgerEntryRepository.findByAccount_AccountNameContainingIgnoreCase(name);
    	if(items.isEmpty()) {
    		String msg = String.format("No LedgerEntry for account-name %s is found", name);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByAccount_Type(AccountType type) {
    	validateAccountType(type);
    	List<LedgerEntry> items = ledgerEntryRepository.findByAccount_Type(type);
    	if(items.isEmpty()) {
    		String msg = String.format("No LedgerEntry for account-type %s is found", type);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(LedgerEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerEntryResponse> findByAccount_Balance(BigDecimal balance) {
    	validateBigDecimal(balance);
    	List<LedgerEntry> items = ledgerEntryRepository.findByAccount_Balance(balance);
    	if(items.isEmpty()) {
    		String msg = String.format("No LedgerEntry for account balance %s is found", balance);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
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
        if(entries.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        	String msg = String.format("No LedgerEntry for entry date equals %s and %s is found", 
        			startOfDay.format(formatter), endOfDay.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return ledgerEntryMapper.toResponseList(entries);
    }

    @Override
    public List<LedgerEntryResponse> findByEntryDateBefore(LocalDateTime date) {
        DateValidator.validateNotInFuture(date, "Date-time");
        List<LedgerEntry> entries = ledgerEntryRepository.findByEntryDateBefore(date);
        if(entries.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        	String msg = String.format("No LedgerEntry for entry date before %s is found", date.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return ledgerEntryMapper.toResponseList(entries);
    }

    @Override
    public List<LedgerEntryResponse> findByEntryDateAfter(LocalDateTime date) {
        DateValidator.validateNotInPast(date, "Date-time");
        List<LedgerEntry> entries = ledgerEntryRepository.findByEntryDateAfter(date);
        if(entries.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        	String msg = String.format("No LedgerEntry for endtry date after %s is found",
        			date.format(formatter));
        	throw new NoDataFoundException(msg);
        }
        return ledgerEntryMapper.toResponseList(entries);
    }

    @Override
    public List<LedgerEntryResponse> findByEntryDateAfterAndType(LocalDateTime date, LedgerType type) {
        DateValidator.validateNotInPast(date, "Date");
        validateLedgerType(type);
        List<LedgerEntry> entries = ledgerEntryRepository.findByEntryDateAfterAndType(date, type);
        if(entries.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        	String msg = String.format("No LedgerEntry for entry-date %s and type %s is found",
        			date.format(formatter),type);
        	throw new NoDataFoundException(msg);
        }
        return ledgerEntryMapper.toResponseList(entries);
    }

    @Override
    public List<LedgerEntryResponse> findByEntryDateBetweenAndAccount_Id(LocalDateTime start, LocalDateTime end,
            Long accountId) {
        DateValidator.validateRange(start, end);
        List<LedgerEntry> entries = ledgerEntryRepository
                .findByEntryDateBetweenAndAccount_Id(start, end, accountId);
        if(entries.isEmpty()) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        	String msg = String.format("No LedgerEntry for entry date between %s and %s is found", 
        			start.format(formatter), end.format(formatter));
        	throw new NoDataFoundException(msg);
        }
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
    
   private Account validateAccountId(Long accountId) {
	   if(accountId == null) {
		   throw new AccountNotFoundErrorException("Account ID must not be null");
	   }
	   return accountRepository.findById(accountId).orElseThrow(() -> new ValidationException("Account not found with id "+accountId));
   }
   
   private void validateMinAndMax(BigDecimal min, BigDecimal max) {
       if (min == null || max == null) {
           throw new IllegalArgumentException("Min i Max ne smeju biti null");
       }

       if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) <= 0) {
           throw new IllegalArgumentException("Min mora biti >= 0, a Max mora biti > 0");
       }

       if (min.compareTo(max) > 0) {
           throw new IllegalArgumentException("Min ne može biti veći od Max");
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
       validateLedgerType(request.type());
   }
   
   private void validateUpdateRequest(LedgerEntryRequest request) {
	   if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
           throw new LedgerEntryErrorException("Amount must be a positive value.");
       }
       validateBigDecimal(request.amount());
       validateString(request.description());
       validateLedgerType(request.type());
   }
}
