package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.jovan.erp_v1.model.Procurement;


@Repository
public interface ProcurementRepository extends JpaRepository<Procurement, Long> {

	List<Procurement> findByDate(LocalDateTime date);
	List<Procurement> findByTotalCost(BigDecimal totalCost);
	@Query("SELECT p FROM Procurement p WHERE p.locdate BETWEEN :start AND :end")
	List<Procurement> findByDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
	@Query("SELECT p FROM Procurement p WHERE p.totalCost BETWEEN :min AND :max")
	List<Procurement> findByTotalCostBetween(@Param("min") BigDecimal min, @Param("max") BigDecimal max);
	@Query("SELECT SUM(p.totalCost) FROM Procurement p")
	BigDecimal sumTotalCost();
	List<Procurement> findByTotalCostGreaterThan(BigDecimal totalCost);
	List<Procurement> findByTotalCostLessThan(BigDecimal totalCost);
}
