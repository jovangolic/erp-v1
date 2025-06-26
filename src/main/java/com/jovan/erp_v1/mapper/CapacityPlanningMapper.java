package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.CapacityPlanning;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.request.CapacityPlanningRequest;
import com.jovan.erp_v1.response.CapacityPlanningResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CapacityPlanningMapper {

    public CapacityPlanning toEntity(CapacityPlanningRequest request, WorkCenter workCenter) {
        Objects.requireNonNull(request, "CapacityPlanningRequest cannot be null");
        Objects.requireNonNull(workCenter, "WorkCenter cannot be null");

        CapacityPlanning cp = new CapacityPlanning();
        cp.setWorkCenter(workCenter);
        cp.setDate(request.date());
        cp.setAvailableCapacity(request.availableCapacit());
        cp.setPlannedLoad(request.plannedLoad());
        cp.setRemainingCapacity(request.availableCapacit().subtract(request.plannedLoad()));
        return cp;
    }

    public CapacityPlanning toUpdateEntity(CapacityPlanning cp, CapacityPlanningRequest request,
            WorkCenter workCenter) {
        Objects.requireNonNull(cp, "CapacityPlanning cannot be null");
        Objects.requireNonNull(request, "CapacityPlanningRequest cannot be null");
        Objects.requireNonNull(workCenter, "WorkCenter cannot be null");

        cp.setWorkCenter(workCenter);
        cp.setDate(request.date());
        cp.setAvailableCapacity(request.availableCapacit());
        cp.setPlannedLoad(request.plannedLoad());
        cp.setRemainingCapacity(request.availableCapacit().subtract(request.plannedLoad()));
        return cp;
    }

    public CapacityPlanningResponse toResponse(CapacityPlanning cp) {
        return new CapacityPlanningResponse(cp);
    }

    public List<CapacityPlanningResponse> toResponseList(List<CapacityPlanning> list) {
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }
}