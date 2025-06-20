package com.jovan.erp_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.LedgerEntry;
import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.enumeration.LedgerType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {

    List<LedgerEntry> findByType(LedgerType type);

    List<LedgerEntry> findByAmountBetween(BigDecimal min, BigDecimal max);

    List<LedgerEntry> findByDescriptionContainingIgnoreCase(String keyword);

    @Query("SELECT l FROM LedgerEntry l WHERE l.entryDate BETWEEN :start AND :end")
    List<LedgerEntry> findByEntryDateBetween(@Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    List<LedgerEntry> findByAccount_Id(Long id);

    List<LedgerEntry> findByAccount_AccountNumber(String accountNumber);

    List<LedgerEntry> findByAccount_AccountName(String accountName);

    List<LedgerEntry> findByAccount_AccountNameContainingIgnoreCase(String name);

    List<LedgerEntry> findByAccount_Type(AccountType type);

    List<LedgerEntry> findByAccount_Balance(BigDecimal balance);

    @Query("SELECT l FROM LedgerEntry l WHERE DATE(l.entryDate) = :date")
    List<LedgerEntry> findByEntryDateEquals(@Param("date") LocalDate date);

    List<LedgerEntry> findByEntryDateBefore(LocalDateTime date);

    List<LedgerEntry> findByEntryDateAfter(LocalDateTime date);

    List<LedgerEntry> findByEntryDateAfterAndType(LocalDateTime date, LedgerType type);

    List<LedgerEntry> findByEntryDateBetweenAndAccount_Id(LocalDateTime start, LocalDateTime end, Long accountId);
}
