package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.dto.DailySalesDTO;
import com.jovan.erp_v1.model.Sales;

import jakarta.persistence.MapKeyColumn;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long>, JpaSpecificationExecutor<Sales> {

	List<Sales> findByCreatedAt(LocalDateTime date);

	List<Sales> findByTotalPrice(BigDecimal totalPrice);

	//List<Sales> findByBuyer(Buyer buyer);

	Sales findSalesById(Long id);

	List<Sales> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

	@Query("SELECT s.createdAt.toLocalDate() AS date, SUM(s.totalPrice) FROM Sales s WHERE s.createdAt >= :fromDate GROUP BY s.createdAt.toLocalDate()")
	@MapKeyColumn(name = "date")
	List<DailySalesDTO> getDailySalesLast7Days(@Param("fromDate") LocalDate fromDate);

	@Query("SELECT SUM(s.totalPrice) FROM Sales s")
	BigDecimal sumAllSalesRevenue();
	
	//nove metode
	List<Sales> findByBuyer_Id(Long buyerId);
	List<Sales> findByBuyer_CompanyNameContainingIgnoreCase(String buyerCompanyName);
	List<Sales> findByBuyer_PibContainingIgnoreCase(String buyerPib);
	List<Sales> findByBuyer_AddressContainingIgnoreCase(String buyerAddress);
	List<Sales> findByBuyer_ContactPerson(String contactPerson);
	List<Sales> findByBuyer_EmailContainingIgnoreCase(String buyerEmail);
	List<Sales> findByBuyer_PhoneNumber(String buyerPhoneNumber);
	List<Sales> findByTotalPriceGreaterThan(BigDecimal totalPrice);
	List<Sales> findByTotalPriceLessThan(BigDecimal totalPrice);
	boolean existsByBuyer_Pib(String pib);
	
}
