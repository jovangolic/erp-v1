package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.CapacityPlanning;
import com.jovan.erp_v1.statistics.capacity_planning.CapacityPlanningAvailableCapacityStatDTO;
import com.jovan.erp_v1.statistics.capacity_planning.CapacityPlanningMonthlyStatDTO;
import com.jovan.erp_v1.statistics.capacity_planning.CapacityPlanningPlannedLoadStatDTO;

@Repository
public interface CapacityPlanningRepository extends JpaRepository<CapacityPlanning, Long>, JpaSpecificationExecutor<CapacityPlanning> {

    List<CapacityPlanning> findByWorkCenter_Id(Long id);
    List<CapacityPlanning> findByWorkCenter_NameContainingIgnoreCase(String name);
    List<CapacityPlanning> findByWorkCenter_LocationContainingIgnoreCase(String location);
    List<CapacityPlanning> findByDateBetween(LocalDate start, LocalDate end);
    List<CapacityPlanning> findByDate(LocalDate date);
    List<CapacityPlanning> findByDateGreaterThanEqual(LocalDate date);
    List<CapacityPlanning> findByAvailableCapacity(BigDecimal availableCapacity);
    List<CapacityPlanning> findByAvailableCapacityGreaterThan(BigDecimal availableCapacity);
    List<CapacityPlanning> findByAvailableCapacityLessThan(BigDecimal availableCapacity);
    List<CapacityPlanning> findByPlannedLoad(BigDecimal plannedLoad);
    List<CapacityPlanning> findByPlannedLoadGreaterThan(BigDecimal plannedLoad);
    List<CapacityPlanning> findByPlannedLoadLessThan(BigDecimal plannedLoad);
    List<CapacityPlanning> findByPlannedLoadAndAvailableCapacity(BigDecimal plannedLoad, BigDecimal availableCapacity);
    List<CapacityPlanning> findByRemainingCapacity(BigDecimal remainingCapacity);
    @Query("SELECT c FROM CapacityPlanning c WHERE c.remainingCapacity < c.availableCapacity")
    List<CapacityPlanning> findWhereRemainingCapacityIsLessThanAvailableCapacity();
    @Query("SELECT c FROM CapacityPlanning c WHERE c.remainingCapacity > c.availableCapacity")
    List<CapacityPlanning> findWhereRemainingCapacityIsGreaterThanAvailableCapacity();
    @Query("SELECT c FROM CapacityPlanning c WHERE c.availableCapacity > 0 ORDER BY (c.plannedLoad / c.availableCapacity) DESC")
    List<CapacityPlanning> findAllOrderByUtilizationDesc();
    @Query("SELECT c FROM CapacityPlanning c WHERE c.plannedLoad > c.availableCapacity")
    List<CapacityPlanning> findWhereLoadExceedsCapacity();
    @Query("SELECT c FROM CapacityPlanning c WHERE (c.availableCapacity > 0 AND (c.plannedLoad / c.availableCapacity) > :threshold)")
    List<CapacityPlanning> findByUtilizationGreaterThan(@Param("threshold") BigDecimal threshold);

    //nove metode
    @Query("SELECT cp FROM CapacityPlanning cp WHERE cp.id = :id")
    Optional<CapacityPlanning> trackCapacityPlanning(@Param("id") Long id);
    
    @Query("SELECT new com.jovan.erp_v1.statistics.capacity_planning.CapacityPlanningPlannedLoadStatDTO(cp.plannedLoad, COUNT(cp)) "+
    		"FROM CapacityPlanning cp GROUP BY cp.plannedLoad")
    List<CapacityPlanningPlannedLoadStatDTO> countCapacityPlanningByPlannedLoad();
    
    @Query("SELECT new com.jovan.erp_v1.statistics.capacity_planning.CapacityPlanningAvailableCapacityStatDTO(cp.availableCapacity, COUNT(cp)) "+
    		"FROM CapacityPlanning cp GROUP BY cp.availableCapacity")
    List<CapacityPlanningAvailableCapacityStatDTO> countCapacityPlanningByAvailableCapacity();
    
    @Query("SELECT new com.jovan.erp_v1.statistics.capacity_planning.CapacityPlanningMonthlyStatDTO(" +
		       "CAST(FUNCTION('YEAR', cp.date) AS integer), "+
			   "CAST(FUNCTION('MONTH', cp.date) AS integer), "+
		       "COUNT(cp)) "+
			   "FROM CapacityPlanning cp "+
		       "GROUP BY FUNCTION('YEAR', cp.date), FUNCTION('MONTH', cp.date) "+
			   "ORDER BY FUNCTION('YEAR', cp.date), FUNCTION('MONTH', cp.date)")
    List<CapacityPlanningMonthlyStatDTO> countCapacityPlanningsByYearAndMonth();
    
    @Query("SELECT SUM(cp.availableCapacity) FROM CapacityPlanning cp")
    BigDecimal getTotalAvailableCapacity();
    @Query("SELECT SUM(cp.plannedLoad) FROM CapacityPlanning cp")
    BigDecimal getTotalPlannedLoad();
    @Query("SELECT SUM(cp.remainingCapacity) FROM CapacityPlanning cp")
    BigDecimal getTotalRemainingCapacity();
    @Query("SELECT AVG(cp.remainingCapacity) FROM CapacityPlanning cp")
    BigDecimal getAverageRemainingCapacity();
    
    
}
