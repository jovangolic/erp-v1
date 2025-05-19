package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.SupplyItem;

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
}
