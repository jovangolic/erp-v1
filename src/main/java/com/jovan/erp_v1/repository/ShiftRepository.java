package com.jovan.erp_v1.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Shift;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

	List<Shift> findByShiftSupervisorId(Long supervisorId);

    List<Shift> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
