package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {

	List<Sales> findByCreatedAt(LocalDateTime date);
	List<Sales> findByTotalPrice(BigDecimal totalPrice);
	List<Sales> findByBuyer(Buyer buyer);
	Sales findSalesById(Long id);
	List<Sales> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
