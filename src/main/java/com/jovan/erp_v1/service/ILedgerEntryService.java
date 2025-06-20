package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.enumeration.LedgerType;
import com.jovan.erp_v1.request.LedgerEntryRequest;
import com.jovan.erp_v1.response.LedgerEntryResponse;

public interface ILedgerEntryService {

    LedgerEntryResponse create(LedgerEntryRequest request);

    LedgerEntryResponse update(Long id, LedgerEntryRequest request);

    void delete(Long id);

    LedgerEntryResponse findOne(Long id);

    List<LedgerEntryResponse> findAll();

    List<LedgerEntryResponse> findByType(LedgerType type);

    List<LedgerEntryResponse> findByAmountBetween(BigDecimal min, BigDecimal max);

    List<LedgerEntryResponse> findByDescriptionContainingIgnoreCase(String keyword);

    List<LedgerEntryResponse> findByEntryDateBetween(LocalDateTime start, LocalDateTime end);

    List<LedgerEntryResponse> findByAccount_Id(Long id);

    List<LedgerEntryResponse> findByAccount_AccountNumber(String accountNumber);

    List<LedgerEntryResponse> findByAccount_AccountName(String accountName);

    List<LedgerEntryResponse> findByAccount_AccountNameContainingIgnoreCase(String name);

    List<LedgerEntryResponse> findByAccount_Type(AccountType type);

    List<LedgerEntryResponse> findByAccount_Balance(BigDecimal balance);

    List<LedgerEntryResponse> findByEntryDateEquals(LocalDate date);

    List<LedgerEntryResponse> findByEntryDateBefore(LocalDateTime date);

    List<LedgerEntryResponse> findByEntryDateAfter(LocalDateTime date);

    List<LedgerEntryResponse> findByEntryDateAfterAndType(LocalDateTime date, LedgerType type);

    List<LedgerEntryResponse> findByEntryDateBetweenAndAccount_Id(LocalDateTime start, LocalDateTime end,
            Long accountId);
}
