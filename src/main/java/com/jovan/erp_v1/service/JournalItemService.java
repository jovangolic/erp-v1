package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.exception.AccountNotFoundErrorException;
import com.jovan.erp_v1.exception.JournalItemErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.JournalItemMapper;
import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.model.JournalEntry;
import com.jovan.erp_v1.model.JournalItem;
import com.jovan.erp_v1.repository.AccountRepository;
import com.jovan.erp_v1.repository.JournalEntryRepository;
import com.jovan.erp_v1.repository.JournalItemRepository;
import com.jovan.erp_v1.request.JournalItemRequest;
import com.jovan.erp_v1.request.JournalItemSearchRequest;
import com.jovan.erp_v1.response.JournalItemResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JournalItemService implements IJournalItemService {

    private final JournalItemRepository journalItemRepository;
    private final JournalItemMapper journalItemMapper;
    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;

    @Transactional
    @Override
    public JournalItemResponse create(JournalItemRequest request) {
    	Account account = validateAccountId(request.accountId());
    	validateBigDecimal(request.debit());
    	validateBigDecimal(request.credit());
    	JournalEntry entry = validateJournalEntryId(request.journalEntryId());
        JournalItem item = journalItemMapper.toEntity(request,account,entry);
        JournalItem saved = journalItemRepository.save(item);
        return journalItemMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public JournalItemResponse update(Long id, JournalItemRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        JournalItem item = journalItemRepository.findById(id)
                .orElseThrow(() -> new JournalItemErrorException("JournalItem not found with id " + id));
        Account account = item.getAccount();
        if(request.accountId() != null && (account.getId() == null || !request.accountId().equals(account.getId()))) {
        	account = validateAccountId(request.accountId());
        }
        validateBigDecimal(request.debit());
    	validateBigDecimal(request.credit());
    	JournalEntry entry = item.getJournalEntry();
    	if(request.journalEntryId() != null && (entry.getId() == null || !request.journalEntryId().equals(entry.getId()))) {
    		entry = validateJournalEntryId(request.journalEntryId());
    	}
        journalItemMapper.toUpdateEntity(item, request,account,entry);
        return journalItemMapper.toResponse(journalItemRepository.save(item));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!journalItemRepository.existsById(id)) {
            throw new JournalItemErrorException("JournalItem not found with id " + id);
        }
        journalItemRepository.deleteById(id);
    }

    @Override
    public JournalItemResponse findOne(Long id) {
        JournalItem item = journalItemRepository.findById(id)
                .orElseThrow(() -> new JournalItemErrorException("JournalItem not found with id " + id));
        return new JournalItemResponse(item);
    }

    @Override
    public List<JournalItemResponse> findAll() {
    	List<JournalItem> items = journalItemRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("JournalItem list is empty");
    	}
        return items.stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByAccount_Id(Long id) {
    	List<JournalItem> items = journalItemRepository.findByAccount_Id(id);
    	if(items.isEmpty()) {
    		String msg = String.format("No JournalItem for account-id %d is found", id);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByAccount_AccountNumber(String accountNumber) {
    	validateString(accountNumber, "accountNumber");
    	List<JournalItem> items = journalItemRepository.findByAccount_AccountNumber(accountNumber);
    	if(items.isEmpty()) {
    		String msg = String.format("No JournalItem for account-number %s is found", accountNumber);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByAccount_AccountName(String accountName) {
    	validateString(accountName, "accountName");
    	List<JournalItem> items = journalItemRepository.findByAccount_AccountName(accountName);
    	if(items.isEmpty()) {
    		if(items.isEmpty()) {
        		String msg = String.format("No JournalItem for account-name %s is found", accountName);
        		throw new NoDataFoundException(msg);
        	}
    	}
        return items.stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByAccount_Type(AccountType type) {
    	validateAccountType(type);
    	List<JournalItem> items = journalItemRepository.findByAccount_Type(type);
    	if(items.isEmpty()) {
    		if(items.isEmpty()) {
        		String msg = String.format("No JournalItem for account-type %s is found", type);
        		throw new NoDataFoundException(msg);
        	}
    	}
        return items.stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByDebitGreaterThan(BigDecimal amount) {
        validateBigDecimal(amount);
        List<JournalItem> items = journalItemRepository.findByDebitGreaterThan(amount);
        if(items.isEmpty()) {
    		String msg = String.format("No JournalItem for debit greater than %s is found", amount);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByDebitLessThan(BigDecimal amount) {
        validateBigDecimalNonNegative(amount);
        List<JournalItem> items = journalItemRepository.findByDebitLessThan(amount);
        if(items.isEmpty()) {
    		String msg = String.format("No JournalItem for debit less than %s is found", amount);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByCreditGreaterThan(BigDecimal amount) {
        validateBigDecimal(amount);
        List<JournalItem> items = journalItemRepository.findByCreditGreaterThan(amount);
        if(items.isEmpty()) {
    		String msg = String.format("No JournalItem for credit greater than %s is found", amount);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByCreditLessThan(BigDecimal amount) {
        validateBigDecimalNonNegative(amount);
        List<JournalItem> items = journalItemRepository.findByCreditLessThan(amount);
        if(items.isEmpty()) {
    		String msg = String.format("No JournalItem for credit less than %s is found", amount);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByDebit(BigDecimal debit) {
        validateBigDecimal(debit);
        List<JournalItem> items = journalItemRepository.findByDebit(debit);
        if(items.isEmpty()) {
    		String msg = String.format("No JournalItem for debit %s is found", debit);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByCredit(BigDecimal credit) {
        validateBigDecimal(credit);
        List<JournalItem> items = journalItemRepository.findByCredit(credit);
        if(items.isEmpty()) {
    		String msg = String.format("No JournalItem for credit %s is found", credit);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByJournalEntry_Id(Long journalEntryId) {
    	validateJournalItemId(journalEntryId);
    	List<JournalItem> items = journalItemRepository.findByJournalEntry_Id(journalEntryId);
    	if(items.isEmpty()) {
    		String msg = String.format("No JurnalItem for journal-entry-id %d is found", journalEntryId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> search(JournalItemSearchRequest request) {
        List<JournalItem> items = journalItemRepository.findAll(); 
        return items.stream()
                .filter(item -> request.debit() == null || item.getDebit().compareTo(request.debit()) == 0)
                .filter(item -> request.credit() == null || item.getCredit().compareTo(request.credit()) == 0)
                .filter(item -> request.accountId() == null ||
                        (item.getAccount() != null && item.getAccount().getId().equals(request.accountId())))
                .filter(item -> {
                    LocalDateTime entryDate = item.getJournalEntry() != null ? item.getJournalEntry().getEntryDate()
                            : null;
                    boolean after = request.fromDate() == null
                            || (entryDate != null && !entryDate.isBefore(request.fromDate()));
                    boolean before = request.toDate() == null
                            || (entryDate != null && !entryDate.isAfter(request.toDate()));
                    return after && before;
                })
                .map(journalItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
	public List<JournalItemResponse> findByJournalEntry_EntryDate(LocalDateTime entryDate) {
		DateValidator.validateNotNull(entryDate, "Date and Time");
		List<JournalItem> items = journalItemRepository.findByJournalEntry_EntryDate(entryDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No JournalItem for journal entry date %s is found",
					entryDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(JournalItemResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<JournalItemResponse> findByJournalEntry_EntryDateBetween(LocalDateTime entryDateStart,
			LocalDateTime entryDateEnd) {
		DateValidator.validateRange(entryDateStart, entryDateEnd);
		List<JournalItem> items = journalItemRepository.findByJournalEntry_EntryDateBetween(entryDateStart, entryDateEnd);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No JournalItem for journal entry date between %s and %s is found",
					entryDateStart.format(formatter),entryDateEnd.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(JournalItemResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<JournalItemResponse> findByJournalEntry_Description(String description) {
		validateString(description, "description");
		List<JournalItem> items = journalItemRepository.findByJournalEntry_Description(description);
		if(items.isEmpty()) {
			String msg = String.format("No JournalItem for journal-entry description %s is found", description);
			throw new NoDataFoundException(msg);
		}
		return items.stream()
				.map(JournalItemResponse::new)
				.collect(Collectors.toList());
	}
	
	private void validateString(String str, String fieldName) {
		if(str == null || str.trim().isEmpty()) {
			throw new IllegalArgumentException(fieldName+" string must not be null or empty");
		}
	}
	
	private void validateJournalItemId(Long journalEntryId) {
		if(journalEntryId == null) {
			throw new JournalItemErrorException("JournalEntry ID must not be null");
		}
	}
	
	private void validateAccountType(AccountType type) {
		if(type == null) {
			throw new IllegalArgumentException("AccountType type must not be null");
		}
	}

	private Account validateAccountId(Long accountId) {
		if(accountId == null) {
			throw new AccountNotFoundErrorException("Account ID must not be null");
		}
		return accountRepository.findById(accountId).orElseThrow(() -> new ValidationException("Account not found with id "+accountId));
	}
	
	private void validateAccountExists(Long accountId) {
	    validateAccountId(accountId);
	    if (!accountRepository.existsById(accountId)) {
	        throw new AccountNotFoundErrorException("Account with ID not found " + accountId);
	    }
	}
	
	private JournalEntry validateJournalEntryId(Long journalEntryId) {
		if(journalEntryId == null) {
			throw new ValidationException("JounralEntry ID must not be null");
		}
		return journalEntryRepository.findById(journalEntryId).orElseThrow(() -> new ValidationException("JournalEntry not found with id "+journalEntryId));
	}
	
	private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }
	
	private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
}
