package com.jovan.erp_v1.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.AccountNotFoundErrorException;
import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.model.LedgerEntry;
import com.jovan.erp_v1.repository.AccountRepository;
import com.jovan.erp_v1.request.LedgerEntryRequest;
import com.jovan.erp_v1.response.LedgerEntryResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LedgerEntryMapper {

    private final AccountRepository accountRepository;

    public LedgerEntry toEntity(LedgerEntryRequest request) {
        LedgerEntry entry = new LedgerEntry();
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

    public void toUpdateEntity(LedgerEntry entry, LedgerEntryRequest request) {
        entry.setEntryDate(request.entryDate());
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be positive and not null");
        }
        entry.setDescription(request.description());
        Account acc = getAccountOrThrow(request.accountId());
        entry.setAccount(acc);
        entry.setType(request.type());
    }

    public LedgerEntryResponse toResponse(LedgerEntry entry) {
        return new LedgerEntryResponse(entry);
    }

    public List<LedgerEntryResponse> toResponseList(List<LedgerEntry> entries) {
        return entries.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Account getAccountOrThrow(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundErrorException("Account not found with id: " + id));
    }
}
