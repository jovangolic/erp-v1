package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.TransactionMapper;
import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.model.Transaction;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.AccountRepository;
import com.jovan.erp_v1.repository.TransactionRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.TransactionRequest;
import com.jovan.erp_v1.response.TransactionResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

	private final TransactionRepository transactionRepository;
	private TransactionMapper transactionMapper;
	private final AccountRepository accountRepository;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public TransactionResponse create(TransactionRequest request) {
		validateBigDecimal(request.amount());
		validateTransactionType(request.transactionType());
		Account sourceAcc = validateSourceAccountId(request.sourceAccountId());
		Account targetAcc = validateTargetAccountId(request.targetAccountId());
		User user = validateUserId(request.userId());
		Transaction t = transactionMapper.toEntity(request, sourceAcc, targetAcc, user);
		Transaction saved = transactionRepository.save(t);
		return new TransactionResponse(saved);
	}

	@Transactional
	@Override
	public TransactionResponse update(Long id, TransactionRequest request) {
		if (!request.id().equals(id)) {
			throw new ValidationException("ID in path and body do not match");
		}
		Transaction t = transactionRepository.findById(id).orElseThrow(() -> new ValidationException("Transaction not found with id "+id));
		validateBigDecimal(request.amount());
		validateTransactionType(request.transactionType());
		Account sourceAcc = t.getSourceAccount();
		if(request.sourceAccountId() != null && (sourceAcc.getId() == null || !request.sourceAccountId().equals(sourceAcc.getId()))) {
			sourceAcc = validateSourceAccountId(request.sourceAccountId());
		}
		Account targetAcc = t.getTargetAccount();
		if(request.targetAccountId() != null && (targetAcc.getId() == null || !request.targetAccountId().equals(targetAcc.getId()))) {
			targetAcc = validateTargetAccountId(request.targetAccountId());
		}
		User user = t.getUser();
		if(request.userId() != null && (user.getId() == null || !request.userId().equals(user.getId()))) {
			user = validateUserId(request.userId());
		}
		transactionMapper.toEntityUpdate(t, request, sourceAcc, targetAcc, user);
		Transaction saved = transactionRepository.save(t);
		return new TransactionResponse(saved);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if(!transactionRepository.existsById(id)) {
			throw new ValidationException("Transaction not found with id "+id);
		}
		transactionRepository.deleteById(id);
	}

	@Override
	public TransactionResponse findOne(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByAmount(BigDecimal amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByAmountGreaterThan(BigDecimal amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByAmountLessThan(BigDecimal amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByAmountBetween(BigDecimal amountMin, BigDecimal amountMax) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByTransactionDate(LocalDateTime transactionDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByTransactionDateAfter(LocalDateTime transactionDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByTransactionDateBefore(LocalDateTime transactionDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByTransactionDateBetween(LocalDateTime transactionDateStart,
			LocalDateTime transactionDateEnd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByTransactionType(TransactionType transactionType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByTransactionTypeAndTransactionDate(TransactionType transactionType,
			LocalDateTime transactionDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByTransactionTypeAndTransactionDateAfter(TransactionType transactionType,
			LocalDateTime transactionDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByTransactionTypeAndTransactionDateBefore(TransactionType transactionType,
			LocalDateTime transactionDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByTransactionTypeAndTransactionDateBetween(TransactionType transactionType,
			LocalDateTime transactionDateStart, LocalDateTime transactionDateEnd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(
			String firstName, String lastName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByUserEmailLikeIgnoreCase(String userEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findBySourceAccountId(Long sourceAccountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findBySourceAccountAccountNumberContainingIgnoreCase(String accountNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findBySourceAccountAccountName(String accountName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findBySourceAccountType(AccountType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findBySourceAccountAccountNumberContainingIgnoreCaseAndSourceAccountAccountNameContainingIgnoreCase(
			String accountNumber, String accountName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByTargetAccountId(Long sourceAccountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByTargetAccountAccountNumberContainingIgnoreCase(String accountNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByTargetAccountAccountName(String accountName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByTargetAccountType(AccountType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByTargetAccountAccountNumberContainingIgnoreCaseAndTargetAccountAccountNameContainingIgnoreCase(
			String accountNumber, String accountName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByAmountBetweenAndTransactionDateBetween(BigDecimal min, BigDecimal max,
			LocalDateTime start, LocalDateTime end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findByUserIdAndTransactionDateBetween(Long userId, LocalDateTime start,
			LocalDateTime end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionResponse> findBySourceAccountIdAndTransactionTypeAndTransactionDateBetween(Long accountId,
			TransactionType transactionType, LocalDateTime start, LocalDateTime end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal sumOfOutgoingTransactions(Long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal sumOfIncomingTransactions(Long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsBySourceAccount_AccountNumberContainingIgnoreCase(String accountNumber) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existsBySourceAccount_AccountNameContainingIgnoreCase(String accountName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existsByTargetAccount_AccountNumberContainingIgnoreCase(String accountNumber) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existsByTargetAccount_AccountNameContainingIgnoreCase(String accountName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existsBySourceAccountIdAndTargetAccountId(Long sourceId, Long targetId) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void validateTransactionType(TransactionType transactionType) {
		Optional.ofNullable(transactionType)
			.orElseThrow(() -> new ValidationException("TransactionType transactionType must not be null"));
	}
	
	private void validateAccountType(AccountType type) {
		Optional.ofNullable(type)
			.orElseThrow(() -> new ValidationException("AccountType type must not be null"));
	}
	
	private void validateString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Tekstualni karakter ne sme biti null ili prazan");
        }
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
    
    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }
    
    private Account validateSourceAccountId(Long sourceAccId) {
    	if(sourceAccId == null) {
    		throw new ValidationException("SourceAccount ID must not be null");
    	}
    	return accountRepository.findById(sourceAccId).orElseThrow(() -> new ValidationException("SourceAccount not found with id "+sourceAccId));
    }
    
    private Account validateTargetAccountId(Long targetAccId) {
    	if(targetAccId == null) {
    		throw new ValidationException("TargetAccount ID must not be null");
    	}
    	return accountRepository.findById(targetAccId).orElseThrow(() -> new ValidationException("TargetAccount not found with id "+targetAccId));
    }
    
    private User validateUserId(Long userId) {
    	if(userId == null) {
    		throw new ValidationException("User ID must not be null");
    	}
    	return userRepository.findById(userId).orElseThrow(() -> new ValidationException("User not found with id "+userId));
    }
}
