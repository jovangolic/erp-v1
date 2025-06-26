package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.CapacityPlanning;

@Repository
public interface CapacityPlanningRepository extends JpaRepository<CapacityPlanning, Long> {

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

}
