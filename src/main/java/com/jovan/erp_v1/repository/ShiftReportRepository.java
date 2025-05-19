package com.jovan.erp_v1.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.ShiftReport;

@Repository
public interface ShiftReportRepository extends JpaRepository<ShiftReport, Long> {

	List<ShiftReport> findByCreatedById(Long userId);

    List<ShiftReport> findByRelatedShiftId(Long shiftId);

    List<ShiftReport> findByCreatedAtAfter(LocalDateTime date);
}
