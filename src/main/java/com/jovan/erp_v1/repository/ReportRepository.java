package com.jovan.erp_v1.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.ReportType;
import com.jovan.erp_v1.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByType(ReportType reportType);

    List<Report> findByGeneratedAt(LocalDateTime generatedAt);
}
