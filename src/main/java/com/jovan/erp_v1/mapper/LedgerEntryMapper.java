package com.jovan.erp_v1.mapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.AccountNotFoundErrorException;
import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.model.LedgerEntry;
import com.jovan.erp_v1.repository.AccountRepository;
import com.jovan.erp_v1.request.LedgerEntryRequest;
import com.jovan.erp_v1.response.LedgerEntryResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LedgerEntryMapper extends AbstractMapper<LedgerEntryRequest> {

    private final AccountRepository accountRepository;

    public LedgerEntry toEntity(LedgerEntryRequest request) {
        Objects.requireNonNull(request,"LedgerEntryRequest must not be null");
        validateIdForCreate(request, LedgerEntryRequest::id);
        return buildLedgerEntryFromRequest(new LedgerEntry(), request);
    }

    public LedgerEntry toUpdateEntity(LedgerEntry entry, LedgerEntryRequest request) {
    	Objects.requireNonNull(request,"LedgerEntryRequest must not be null");
    	Objects.requireNonNull(entry,"LedgerEntry must not be null");
    	validateIdForUpdate(request, LedgerEntryRequest::id);
    	return buildLedgerEntryFromRequest(entry, request);
    }
    
    private LedgerEntry buildLedgerEntryFromRequest(LedgerEntry entry, LedgerEntryRequest request) {
    	if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be positive and not null");
        }
    	entry.setEntryDate(request.entryDate());
    	entry.setDescription(request.description());
        Account acc = getAccountOrThrow(request.accountId());
        entry.setAccount(acc);
        entry.setType(request.type());
        return entry;
    }

    public LedgerEntryResponse toResponse(LedgerEntry entry) {
    	Objects.requireNonNull(entry,"LedgerEntry must not be null");
        return new LedgerEntryResponse(entry);
    }

    public List<LedgerEntryResponse> toResponseList(List<LedgerEntry> entries) {
    	if(entries == null || entries.isEmpty()) {
    		return Collections.emptyList();
    	}
        return entries.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Account getAccountOrThrow(Long id) {
    	if(id == null) {
    		throw new AccountNotFoundErrorException("Account ID must not be null");
    	}
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundErrorException("Account not found with id: " + id));
    }
}
