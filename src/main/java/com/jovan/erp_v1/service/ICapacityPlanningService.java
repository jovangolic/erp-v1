package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.request.CapacityPlanningRequest;
import com.jovan.erp_v1.response.CapacityPlanningResponse;

public interface ICapacityPlanningService {

    CapacityPlanningResponse create(CapacityPlanningRequest request);
    CapacityPlanningResponse update(Long id, CapacityPlanningRequest request);
    void delete(Long id);
    CapacityPlanningResponse findOne(Long id);
    List<CapacityPlanningResponse> findAll();
    List<CapacityPlanningResponse> findByWorkCenter_Id(Long id);
    List<CapacityPlanningResponse> findByWorkCenter_NameContainingIgnoreCase(String name);
    List<CapacityPlanningResponse> findByWorkCenter_LocationContainingIgnoreCase(String location);
    List<CapacityPlanningResponse> findByDateBetween(LocalDate start, LocalDate end);
    List<CapacityPlanningResponse> findByDate(LocalDate date);
    List<CapacityPlanningResponse> findByDateGreaterThanEqual(LocalDate date);
    List<CapacityPlanningResponse> findByAvailableCapacity(BigDecimal availableCapacity);
    List<CapacityPlanningResponse> findByAvailableCapacityGreaterThan(BigDecimal availableCapacity);
    List<CapacityPlanningResponse> findByAvailableCapacityLessThan(BigDecimal availableCapacity);
    List<CapacityPlanningResponse> findByPlannedLoad(BigDecimal plannedLoad);
    List<CapacityPlanningResponse> findByPlannedLoadGreaterThan(BigDecimal plannedLoad);
    List<CapacityPlanningResponse> findByPlannedLoadLessThan(BigDecimal plannedLoad);
    List<CapacityPlanningResponse> findByPlannedLoadAndAvailableCapacity(BigDecimal plannedLoad,
            BigDecimal availableCapacity);
    List<CapacityPlanningResponse> findByRemainingCapacity(BigDecimal remainingCapacity);
    List<CapacityPlanningResponse> findWhereRemainingCapacityIsLessThanAvailableCapacity();
    List<CapacityPlanningResponse> findWhereRemainingCapacityIsGreaterThanAvailableCapacity();
    List<CapacityPlanningResponse> findAllOrderByUtilizationDesc();
    List<CapacityPlanningResponse> findWhereLoadExceedsCapacity();
    List<CapacityPlanningResponse> findByUtilizationGreaterThan(BigDecimal threshold);
}
