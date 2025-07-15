package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.SupplyItem;
import com.jovan.erp_v1.request.CostSumByProcurement;

@Repository
public interface SupplyItemRepository extends JpaRepository<SupplyItem, Long> {

	List<SupplyItem> findByProcurementId(Long procurementId);
    List<SupplyItem> findBySupplierId(Long supplierId);
    List<SupplyItem> findByCostBetween(BigDecimal min, BigDecimal max);
 // Pretraga stavki nabavke po datumu nabavke (Procurement)
    @Query("SELECT si FROM SupplyItem si JOIN si.procurement p WHERE p.date BETWEEN :startDate AND :endDate")
    List<SupplyItem> findByProcurementDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    // Pretraga stavki nabavke prema ceni i datumu
    @Query("SELECT si FROM SupplyItem si JOIN si.procurement p WHERE p.date BETWEEN :startDate AND :endDate AND si.cost BETWEEN :minCost AND :maxCost")
    List<SupplyItem> findByProcurementDateAndCostBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("minCost") BigDecimal minCost,
        @Param("maxCost") BigDecimal maxCost);
    // Pretraga stavki nabavke po ID-u nabavke (Procurement) i ID-u isporučioca (Vendor)
    @Query("SELECT si FROM SupplyItem si JOIN si.procurement p JOIN si.supplier s WHERE p.id = :procurementId AND s.id = :vendorId")
    List<SupplyItem> findByProcurementAndVendor(@Param("procurementId") Long procurementId, @Param("vendorId") Long vendorId);
 // Pretraga stavki nabavke prema Vendor-u, Procurement-u i ceni
    @Query("SELECT si FROM SupplyItem si JOIN si.procurement p JOIN si.supplier s WHERE s.id = :supplierId AND p.id = :procurementId AND si.cost > :minCost")
    List<SupplyItem> findByVendorAndProcurementAndCost(
        @Param("supplierId") Long supplierId,
        @Param("procurementId") Long procurementId,
        @Param("minCost") BigDecimal minCost);
 // Pretraga stavki sa filtrima za datum i cenu
    @Query("SELECT si FROM SupplyItem si JOIN si.procurement p WHERE p.date >= :startDate AND p.date <= :endDate AND si.cost BETWEEN :minCost AND :maxCost")
    List<SupplyItem> findByDateAndCost(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("minCost") BigDecimal minCost,
        @Param("maxCost") BigDecimal maxCost);
 // Pretraga sa JOIN-om i uslovima za isporučioca, nabavku i cenu
    @Query("SELECT si FROM SupplyItem si JOIN si.procurement p JOIN si.supplier s WHERE s.name = :supplierName AND p.date BETWEEN :startDate AND :endDate AND si.cost < :maxCost")
    List<SupplyItem> findBySupplierNameAndProcurementDateAndMaxCost(
        @Param("supplierName") String supplierName,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("maxCost") BigDecimal maxCost);
    
    //nove metode
    List<SupplyItem> findBySupplier_NameContainingIgnoreCase(String supplierName);
	@Query("SELECT si FROM SupplyItem si WHERE LOWER(si.supplier.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%'))")
	List<SupplyItem> findBySupplier_PhoneNumberLikeIgnoreCase(@Param("phoneNumber") String phoneNumber);
	@Query("SELECT si FROM SupplyItem si WHERE LOWER(si.supplier.email) LIKE LOWER(CONCAT('%', :email, '%'))")
	List<SupplyItem> findBySupplier_EmailLikeIgnoreCase(@Param("email") String email);
	List<SupplyItem> findBySupplier_Address(String address);
	@Query("SELECT si FROM SupplyItem si WHERE LOWER(si.supplier.name) LIKE LOWER(CONCAT('%', :supplierName, '%')) AND si.cost BETWEEN :minCost AND :maxCost")
	List<SupplyItem> findBySupplierNameContainingAndCostBetween(
	     @Param("supplierName") String supplierName,
	     @Param("minCost") BigDecimal minCost,
	     @Param("maxCost") BigDecimal maxCost);
	@Query("SELECT si FROM SupplyItem si JOIN si.procurement p WHERE LOWER(si.supplier.name) LIKE LOWER(CONCAT('%', :supplierName, '%')) AND p.date BETWEEN :start AND :end")
	List<SupplyItem> findBySupplierNameAndProcurementDateBetween(
	     @Param("supplierName") String supplierName,
	     @Param("start") LocalDateTime start,
	     @Param("end") LocalDateTime end);
	@Query("SELECT si FROM SupplyItem si WHERE si.supplier.address = :address AND si.cost > :minCost")
	List<SupplyItem> findByAddressAndMinCost(
	     @Param("address") String address,
	     @Param("minCost") BigDecimal minCost);
	@Query("SELECT si FROM SupplyItem si WHERE si.supplier.phoneNumber = :phoneNumber AND si.cost = :cost")
	List<SupplyItem> findByPhoneNumberAndCost(
	     @Param("phoneNumber") String phoneNumber,
	     @Param("cost") BigDecimal cost);
	@Query("SELECT si FROM SupplyItem si WHERE SIZE(si.procurement.supplies) = :count")
	List<SupplyItem> findByProcurementSupplyItemCount(@Param("count") Integer count);
	List<SupplyItem> findByCost(BigDecimal cost);
	List<SupplyItem> findByCostGreaterThan(BigDecimal cost);
	List<SupplyItem> findByCostLessThan(BigDecimal cost);
	List<SupplyItem> findByProcurement_TotalCost(BigDecimal totalCost);
	List<SupplyItem> findByProcurement_TotalCostGreaterThan(BigDecimal totalCost);
	List<SupplyItem> findByProcurement_TotalCostLessThan(BigDecimal totalCost);
	@Query(" SELECT si FROM SupplyItem si WHERE si.procurement.totalCost > :minCost")
	List<SupplyItem> findByProcurementTotalCostGreaterThan(@Param("minCost") BigDecimal minCost);
	@Query("SELECT si FROM SupplyItem si WHERE si.procurement.date = :date")
	List<SupplyItem> findByProcurementDate(@Param("date") LocalDateTime date);
	@Query("SELECT si FROM SupplyItem si WHERE  (SELECT COUNT(s) FROM SupplyItem s WHERE s.procurement.id = si.procurement.id) <> (SELECT COUNT(isl) FROM ItemSales isl WHERE isl.procurement.id = si.procurement.id)")
	List<SupplyItem> findBySupplyAndSalesCountMismatch();
	@Query("SELECT COUNT(si) FROM SupplyItem si")
	Long countAllSupplyItems();
	@Query("SELECT COUNT(si) FROM SupplyItem si WHERE si.procurement.id = :procurementId")
	Long countByProcurementId(@Param("procurementId") Long procurementId);@Query("SELECT SUM(si.cost) FROM SupplyItem si")
	BigDecimal sumAllCosts();
	@Query("SELECT AVG(si.cost) FROM SupplyItem si")
	BigDecimal averageCost();
	@Query("SELECT MIN(si.cost) FROM SupplyItem si")
	BigDecimal minCost();
	@Query("SELECT MAX(si.cost) FROM SupplyItem si")
	BigDecimal maxCost();
	@Query("SELECT si.procurement.id, SUM(si.cost) FROM SupplyItem si GROUP BY si.procurement.id")
	List<Object[]> sumCostGroupedByProcurement();
	@Query("SELECT si.supplier.id, COUNT(si) FROM SupplyItem si GROUP BY si.supplier.id")
	List<Object[]> countBySupplier();
	@Query("SELECT si.procurement.id, AVG(si.cost) FROM SupplyItem si GROUP BY si.procurement.id")
	List<Object[]> avgCostByProcurement();
	@Query("SELECT ProcurementGlobalStatsResponse(COUNT(si), SUM(si.cost), AVG(si.cost), MIN(si.cost), MAX(si.cost)) psr FROM SupplyItem si")
	Object[] procurementStats();
	@Query("SELECT ProcurementStatsPerEntityResponse(" +
		       "si.procurement.id, COUNT(si), SUM(si.cost), AVG(si.cost), MIN(si.cost), MAX(si.cost)) " +
		       "psr FROM SupplyItem si " +
		       "GROUP BY si.procurement.id")
	List<Object[]> procurementPerEntityStats();
	@Query("SELECT CostSumByProcurement(si.procurement.id, SUM(si.cost)) csp FROM SupplyItem si GROUP BY si.procurement.id")
	List<CostSumByProcurement> findCostSumGroupedByProcurement();
	@Query("SELECT si FROM SupplyItem si WHERE si.procurement.id IN ( SELECT si2.procurement.id FROM SupplyItem si2 GROUP BY si2.procurement.id HAVING SUM(si2.cost) > :minTotal)")
	List<SupplyItem> findByProcurementWithSupplyCostOver(@Param("minTotal") BigDecimal minTotal);
	@Query("SELECT si FROM SupplyItem si WHERE si.supplier.id IN ( SELECT si2.supplier.id FROM SupplyItem si2 GROUP BY si2.supplier.id HAVING COUNT(si2) > :minCount )")
	List<SupplyItem> findBySupplierWithMoreThanNItems(@Param("minCount") Long minCount);
	
	 
	 //boolean
	 boolean existsBySupplierId(Long supplierId);
	 boolean existsByCostBetween(BigDecimal min, BigDecimal max);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si JOIN si.procurement p WHERE p.date BETWEEN :startDate AND :endDate")
	 boolean existsByProcurementDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si JOIN si.procurement p JOIN si.supplier s WHERE p.id = :procurementId AND s.id = :vendorId")
	 boolean existsByProcurementAndVendor(@Param("procurementId") Long procurementId, @Param("vendorId") Long vendorId);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si JOIN si.procurement p JOIN si.supplier s WHERE s.id = :supplierId AND p.id = :procurementId AND si.cost > :minCost")
	 boolean existsByVendorAndProcurementAndCost(@Param("supplierId") Long supplierId, @Param("procurementId") Long procurementId, @Param("minCost") BigDecimal minCost);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si JOIN si.procurement p WHERE p.date >= :startDate AND p.date <= :endDate AND si.cost BETWEEN :minCost AND :maxCost")
	 boolean existsByDateAndCost(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("minCost") BigDecimal minCost, @Param("maxCost") BigDecimal maxCost);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si JOIN si.procurement p JOIN si.supplier s WHERE s.name = :supplierName AND p.date BETWEEN :startDate AND :endDate AND si.cost < :maxCost")
	 boolean existsBySupplierNameAndProcurementDateAndMaxCost(@Param("supplierName") String supplierName, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("maxCost") BigDecimal maxCost);
	 boolean existsBySupplier_NameContainingIgnoreCase(String supplierName);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si WHERE LOWER(si.supplier.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%'))")
	 boolean existsBySupplier_PhoneNumberLikeIgnoreCase(@Param("phoneNumber") String phoneNumber);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si WHERE LOWER(si.supplier.email) LIKE LOWER(CONCAT('%', :email, '%'))")
	 boolean existsBySupplier_EmailLikeIgnoreCase(@Param("email") String email);
	 boolean existsBySupplier_Address(String address);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si WHERE LOWER(si.supplier.name) LIKE LOWER(CONCAT('%', :supplierName, '%')) AND si.cost BETWEEN :minCost AND :maxCost")
	 boolean existsBySupplierNameContainingAndCostBetween(@Param("supplierName") String supplierName, @Param("minCost") BigDecimal minCost, @Param("maxCost") BigDecimal maxCost);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si JOIN si.procurement p WHERE LOWER(si.supplier.name) LIKE LOWER(CONCAT('%', :supplierName, '%')) AND p.date BETWEEN :start AND :end")
	 boolean existsBySupplierNameAndProcurementDateBetween(@Param("supplierName") String supplierName, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si WHERE si.supplier.address = :address AND si.cost > :minCost")
	 boolean existsByAddressAndMinCost(@Param("address") String address, @Param("minCost") BigDecimal minCost);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si WHERE si.supplier.phoneNumber = :phoneNumber AND si.cost = :cost")
	 boolean existsByPhoneNumberAndCost(@Param("phoneNumber") String phoneNumber, @Param("cost") BigDecimal cost);
	 
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si WHERE (SELECT COUNT(s) FROM SupplyItem s WHERE s.procurement.id = si.procurement.id) = :count")
	 boolean existsByProcurementSupplyItemCount(@Param("count") Integer count);
	 boolean existsByCost(BigDecimal cost);
	 boolean existsByCostGreaterThan(BigDecimal cost);
	 boolean existsByCostLessThan(BigDecimal cost);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si WHERE si.procurement.totalCost = :totalCost")
	 boolean existsByProcurement_TotalCost(@Param("totalCost") BigDecimal totalCost);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si WHERE si.procurement.totalCost >= :totalCost")
	 boolean existsByProcurement_TotalCostGreaterThan(@Param("totalCost") BigDecimal totalCost);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si WHERE si.procurement.totalCost <= :totalCost")
	 boolean existsByProcurement_TotalCostLessThan(@Param("totalCost") BigDecimal totalCost);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si")
	 boolean existsAnySupplyItem();
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si WHERE si.procurement.id = :procurementId")
	 boolean existsByProcurementId(@Param("procurementId") Long procurementId);
	 @Query("SELECT (AVG(si.cost) > :value) FROM SupplyItem si")
	 boolean avgCostGreaterThan(@Param("value") BigDecimal value);
	 @Query("SELECT COUNT(si) > 0 FROM SupplyItem si GROUP BY si.supplier.id HAVING COUNT(si) > :minCount")
	 boolean existsSupplierWithMoreThanNItems(@Param("minCount") Long minCount);@Query("SELECT COUNT(si.procurement.id) > 0 FROM SupplyItem si GROUP BY si.procurement.id HAVING SUM(si.cost) > :minTotal")
	 boolean existsProcurementWithSupplyCostOver(@Param("minTotal") BigDecimal minTotal);
	 
}
