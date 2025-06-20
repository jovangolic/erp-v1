package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.request.JournalItemRequest;
import com.jovan.erp_v1.response.JournalItemResponse;

public interface IJournalItemService {

    JournalItemResponse create(JournalItemRequest request);

    JournalItemResponse update(Long id, JournalItemRequest request);

    void delete(Long id);

    JournalItemResponse findOne(Long id);

    List<JournalItemResponse> findAll();

    List<JournalItemResponse> findByAccount_Id(Long id);

    List<JournalItemResponse> findByAccount_AccountNumber(String accountNumber);

    List<JournalItemResponse> findByAccount_AccountName(String accountName);

    List<JournalItemResponse> findByAccount_Type(AccountType type);

    List<JournalItemResponse> findByDebitGreaterThan(BigDecimal amount);

    List<JournalItemResponse> findByDebitLessThan(BigDecimal amount);

    List<JournalItemResponse> findByCreditGreaterThan(BigDecimal amount);

    List<JournalItemResponse> findByCreditLessThan(BigDecimal amount);

    List<JournalItemResponse> findByDebit(BigDecimal debit);

    List<JournalItemResponse> findByCredit(BigDecimal credit);

    List<JournalItemResponse> findByJournalEntry_Id(Long journalEntryId);
}
