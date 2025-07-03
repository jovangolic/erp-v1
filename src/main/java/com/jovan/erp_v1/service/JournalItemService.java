package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.exception.AccountNotFoundErrorException;
import com.jovan.erp_v1.exception.JournalItemErrorException;
import com.jovan.erp_v1.mapper.JournalItemMapper;
import com.jovan.erp_v1.model.JournalItem;
import com.jovan.erp_v1.repository.AccountRepository;
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

    @Transactional
    @Override
    public JournalItemResponse create(JournalItemRequest request) {
    	validateAccountExists(request.accountId());
        JournalItem item = journalItemMapper.toEntity(request);
        JournalItem saved = journalItemRepository.save(item);
        return journalItemMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public JournalItemResponse update(Long id, JournalItemRequest request) {
        JournalItem item = journalItemRepository.findById(id)
                .orElseThrow(() -> new JournalItemErrorException("JournalItem not found with id " + id));
        validateAccountExists(request.accountId());
        journalItemMapper.toUpdateEntity(item, request);
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
        return journalItemRepository.findAll().stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByAccount_Id(Long id) {
        return journalItemRepository.findByAccount_Id(id).stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByAccount_AccountNumber(String accountNumber) {
    	validateString(accountNumber, "accountNumber");
        return journalItemRepository.findByAccount_AccountNumber(accountNumber).stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByAccount_AccountName(String accountName) {
    	validateString(accountName, "accountName");
        return journalItemRepository.findByAccount_AccountName(accountName).stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByAccount_Type(AccountType type) {
    	validateAccountType(type);
        return journalItemRepository.findByAccount_Type(type).stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByDebitGreaterThan(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        return journalItemRepository.findByDebitGreaterThan(amount).stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByDebitLessThan(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        return journalItemRepository.findByDebitLessThan(amount).stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByCreditGreaterThan(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        return journalItemRepository.findByCreditGreaterThan(amount).stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByCreditLessThan(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        return journalItemRepository.findByCreditLessThan(amount).stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByDebit(BigDecimal debit) {
        if (debit == null || debit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Debit must be non-null and >= 0");
        }
        return journalItemRepository.findByDebit(debit).stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByCredit(BigDecimal credit) {
        if (credit == null || credit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Debit must be non-null and >= 0");
        }
        return journalItemRepository.findByCredit(credit).stream()
                .map(JournalItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalItemResponse> findByJournalEntry_Id(Long journalEntryId) {
    	validateJournalItemId(journalEntryId);
        return journalItemRepository.findByJournalEntry_Id(journalEntryId).stream()
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
		return journalItemRepository.findByJournalEntry_EntryDate(entryDate).stream()
				.map(JournalItemResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<JournalItemResponse> findByJournalEntry_EntryDateBetween(LocalDateTime entryDateStart,
			LocalDateTime entryDateEnd) {
		DateValidator.validateRange(entryDateStart, entryDateEnd);
		return journalItemRepository.findByJournalEntry_EntryDateBetween(entryDateStart, entryDateEnd).stream()
				.map(JournalItemResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<JournalItemResponse> findByJournalEntry_Description(String description) {
		validateString(description, "description");
		return journalItemRepository.findByJournalEntry_Description(description).stream()
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

	private void validateAccountId(Long accountId) {
		if(accountId == null) {
			throw new AccountNotFoundErrorException("Account ID must not be null");
		}
	}
	
	private void validateAccountExists(Long accountId) {
	    validateAccountId(accountId);
	    if (!accountRepository.existsById(accountId)) {
	        throw new AccountNotFoundErrorException("Account with ID not found " + accountId);
	    }
	}
}
