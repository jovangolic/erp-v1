package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.model.JournalItem;

@Repository
public interface JournalItemRepository extends JpaRepository<JournalItem, Long> {

    List<JournalItem> findByAccount_Id(Long id);

    List<JournalItem> findByAccount_AccountNumber(String accountNumber);

    List<JournalItem> findByAccount_AccountName(String accountName);

    List<JournalItem> findByAccount_Type(AccountType type);

    List<JournalItem> findByDebitGreaterThan(BigDecimal amount);

    List<JournalItem> findByDebitLessThan(BigDecimal amount);

    List<JournalItem> findByCreditGreaterThan(BigDecimal amount);

    List<JournalItem> findByCreditLessThan(BigDecimal amount);

    List<JournalItem> findByDebit(BigDecimal debit);

    List<JournalItem> findByCredit(BigDecimal credit);

    List<JournalItem> findByJournalEntry_Id(Long journalEntryId);
}
