package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.dto.DailySalesDTO;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {

	List<Sales> findByCreatedAt(LocalDateTime date);

	List<Sales> findByTotalPrice(BigDecimal totalPrice);

	List<Sales> findByBuyer(Buyer buyer);

	Sales findSalesById(Long id);

	List<Sales> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

	@Query("SELECT new com.example.dto.DailySalesDTO(DATE(s.createdAt), SUM(s.totalPrice)) " +
			"FROM Sales s WHERE s.createdAt >= :startDate GROUP BY DATE(s.createdAt) ORDER BY DATE(s.createdAt)")
	List<DailySalesDTO> getDailySalesLast7Days(@Param("startDate") LocalDate startDate);
}
