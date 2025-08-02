package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.ShiftPlanning;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.model.WorkCenter;

import com.jovan.erp_v1.request.ShiftPlanningRequest;
import com.jovan.erp_v1.response.ShiftPlanningResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class ShiftPlanningMapper extends AbstractMapper<ShiftPlanningRequest> {


    public ShiftPlanning toEntity(ShiftPlanningRequest request,WorkCenter wc,User employee) {
        Objects.requireNonNull(request, "ShiftPlanningRequest must not be null");
        Objects.requireNonNull(wc, "WorkCenter must not be null");
        Objects.requireNonNull(employee, "Employee must not be null");
        validateIdForCreate(request, ShiftPlanningRequest::id);
        ShiftPlanning sp = new ShiftPlanning();
        sp.setId(request.id());
        sp.setWorkCenter(wc);
        sp.setEmployee(employee);
        sp.setShiftType(request.shiftType());
        sp.setAssigned(request.assigned());
        return sp;
    }

    public ShiftPlanning toUpdateEntity(ShiftPlanning sp, ShiftPlanningRequest request, WorkCenter wc,User employee) {
        Objects.requireNonNull(sp, "ShiftPlanning must not be null");
        Objects.requireNonNull(request, "ShiftPlanningRequest must not be null");
        Objects.requireNonNull(wc, "WorkCenter must not be null");
        Objects.requireNonNull(employee, "Employee must not be null");
        validateIdForUpdate(request, ShiftPlanningRequest::id);
        return buildShiftPlanningFromRequest(sp, request,wc, employee);
    }

    public ShiftPlanningResponse toResponse(ShiftPlanning sp) {
    	Objects.requireNonNull(sp, "ShiftPlanning must not be null");
        return new ShiftPlanningResponse(sp);
    }

    public List<ShiftPlanningResponse> toResponseList(List<ShiftPlanning> sp) {
    	if(sp == null || sp.isEmpty()) {
    		return Collections.emptyList();
    	}
        return sp.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private ShiftPlanning buildShiftPlanningFromRequest(ShiftPlanning sp, ShiftPlanningRequest request, WorkCenter wc,User employee) {
        sp.setWorkCenter(wc);
        sp.setEmployee(employee);
        sp.setShiftType(request.shiftType());
        sp.setAssigned(request.assigned());
        return sp;
    }

}
