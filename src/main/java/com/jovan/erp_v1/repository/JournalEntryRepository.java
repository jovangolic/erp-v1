package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.JournalEntry;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    List<JournalEntry> findByDescription(String description);
    List<JournalEntry> findByEntryDateBetween(LocalDateTime start, LocalDateTime end);
    @Query("SELECT je FROM JournalEntry je WHERE DATE(je.entryDate) = :date")
    List<JournalEntry> findByEntryDateOn(@Param("date") LocalDate date);
    List<JournalEntry> findByEntryDateBefore(LocalDateTime dateTime);
    List<JournalEntry> findByEntryDateAfter(LocalDateTime dateTime);
    @Query("SELECT je FROM JournalEntry je WHERE FUNCTION('YEAR', je.entryDate) = :year")
    List<JournalEntry> findByYear(@Param("year") Integer year);
    List<JournalEntry> findByDescriptionAndEntryDateBetween(String description, LocalDateTime start, LocalDateTime end);

}
