package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.enumeration.CapacityPlanningStatus;
import com.jovan.erp_v1.request.CapacityPlanningRequest;
import com.jovan.erp_v1.response.CapacityPlanningResponse;
import com.jovan.erp_v1.save_as.CapacityPlanningSaveAsRequest;
import com.jovan.erp_v1.search_request.CapacityPlanningSearchRequest;
import com.jovan.erp_v1.statistics.capacity_planning.CapacityPlanningAvailableCapacityStatDTO;
import com.jovan.erp_v1.statistics.capacity_planning.CapacityPlanningMonthlyStatDTO;
import com.jovan.erp_v1.statistics.capacity_planning.CapacityPlanningPlannedLoadStatDTO;

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
    
    //nove metode
    List<CapacityPlanningPlannedLoadStatDTO> countCapacityPlanningByPlannedLoad();
    List<CapacityPlanningAvailableCapacityStatDTO> countCapacityPlanningByAvailableCapacity();
    List<CapacityPlanningMonthlyStatDTO> countCapacityPlanningsByYearAndMonth();
    CapacityPlanningResponse trackCapacityPlanning(Long id);
    CapacityPlanningResponse confirmCapacityPlanning(Long id);
    CapacityPlanningResponse closeCapacityPlanning(Long id);
    CapacityPlanningResponse cancelCapacityPlanning(Long id);
    CapacityPlanningResponse changeStatus(Long id, CapacityPlanningStatus status);
    CapacityPlanningResponse saveCapacityPlanning(CapacityPlanningRequest request);
    CapacityPlanningResponse saveAs(CapacityPlanningSaveAsRequest request);
    List<CapacityPlanningResponse> saveAll(List<CapacityPlanningRequest> requests);
    List<CapacityPlanningResponse> generalSearch(CapacityPlanningSearchRequest request);
}
