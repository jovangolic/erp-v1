package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.CapacityPlanningStatus;
import com.jovan.erp_v1.model.CapacityPlanning;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapacityPlanningResponse {

    private Long id;
    private WorkCenterResponse workCenterResponse;
    private LocalDate date;
    private BigDecimal availableCapacity;
    private BigDecimal plannedLoad;
    private BigDecimal remainingCapacity;
    private CapacityPlanningStatus status;
    private Boolean confirmed;

    public CapacityPlanningResponse(CapacityPlanning cp) {
        this.id = cp.getId();
        this.workCenterResponse = new WorkCenterResponse(cp.getWorkCenter());
        this.date = cp.getDate();
        this.availableCapacity = cp.getAvailableCapacity();
        this.plannedLoad = cp.getPlannedLoad();
        this.remainingCapacity = cp.getRemainingCapacity();
        this.status = cp.getStatus();
        this.confirmed = cp.getConfirmed();
    }
}
