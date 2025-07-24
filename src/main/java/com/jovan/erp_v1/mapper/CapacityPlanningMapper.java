package com.jovan.erp_v1.mapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.CapacityPlanning;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.request.CapacityPlanningRequest;
import com.jovan.erp_v1.response.CapacityPlanningResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class CapacityPlanningMapper extends AbstractMapper<CapacityPlanningRequest> {

    public CapacityPlanning toEntity(CapacityPlanningRequest request, WorkCenter workCenter) {
        Objects.requireNonNull(request, "CapacityPlanningRequest cannot be null");
        Objects.requireNonNull(workCenter, "WorkCenter cannot be null");
        CapacityPlanning cp = new CapacityPlanning();
        validateIdForCreate(request, CapacityPlanningRequest::id);
        cp.setWorkCenter(workCenter);
        cp.setDate(request.date());
        cp.setAvailableCapacity(request.availableCapacity());
        cp.setPlannedLoad(request.plannedLoad());
        cp.setRemainingCapacity(calculateRemainingCapacity(request.availableCapacity(), request.plannedLoad()));
        return cp;
    }

    public CapacityPlanning toUpdateEntity(CapacityPlanning cp, CapacityPlanningRequest request,
            WorkCenter workCenter) {
        Objects.requireNonNull(cp, "CapacityPlanning cannot be null");
        Objects.requireNonNull(request, "CapacityPlanningRequest cannot be null");
        Objects.requireNonNull(workCenter, "WorkCenter cannot be null");
        validateIdForUpdate(request, CapacityPlanningRequest::id);
        cp.setWorkCenter(workCenter);
        cp.setDate(request.date());
        cp.setAvailableCapacity(request.availableCapacity());
        cp.setPlannedLoad(request.plannedLoad());
        cp.setRemainingCapacity(calculateRemainingCapacity(request.availableCapacity(), request.plannedLoad()));
        return cp;
    }

    public CapacityPlanningResponse toResponse(CapacityPlanning cp) {
    	Objects.requireNonNull(cp, "CapacityPlanning must not be null");
        return new CapacityPlanningResponse(cp);
    }

    public List<CapacityPlanningResponse> toResponseList(List<CapacityPlanning> list) {
    	if(list == null || list.isEmpty()) {
    		return Collections.emptyList();
    	}
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }
    
    private BigDecimal calculateRemainingCapacity(BigDecimal available, BigDecimal planned) {
        BigDecimal remaining = available.subtract(planned);
        if (remaining.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Planned load ne može biti veći od available capacity.");
        }
        return remaining;
    }
}