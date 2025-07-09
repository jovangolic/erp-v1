package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.exception.WorkCenterErrorException;
import com.jovan.erp_v1.model.ShiftPlanning;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.model.WorkCenter;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.repository.WorkCenterRepository;
import com.jovan.erp_v1.request.ShiftPlanningRequest;
import com.jovan.erp_v1.response.ShiftPlanningResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShiftPlanningMapper extends AbstractMapper<ShiftPlanningRequest> {

    private final WorkCenterRepository workCenterRepository;
    private final UserRepository userRepository;

    public ShiftPlanning toEntity(ShiftPlanningRequest request) {
        Objects.requireNonNull(request, "ShiftPlanningRequest must not be null");
        validateIdForCreate(request, ShiftPlanningRequest::id);
        return buildShiftPlanningFromRequest(new ShiftPlanning(), request);
    }

    public ShiftPlanning toUpdateEntity(ShiftPlanning sp, ShiftPlanningRequest request) {
        Objects.requireNonNull(sp, "ShiftPlanning must not be null");
        Objects.requireNonNull(request, "ShiftPlanningRequest must not be null");
        validateIdForUpdate(request, ShiftPlanningRequest::id);
        return buildShiftPlanningFromRequest(sp, request);
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

    private ShiftPlanning buildShiftPlanningFromRequest(ShiftPlanning sp, ShiftPlanningRequest request) {
        sp.setWorkCenter(fetchWorkCenter(request.workCenterId()));
        sp.setEmployee(fetchUser(request.userId()));
        sp.setDate(request.date());
        sp.setShiftType(request.shiftType());
        sp.setAssigned(request.assigned());
        return sp;
    }

    private WorkCenter fetchWorkCenter(Long workCenterId) {
        if (workCenterId == null) {
            throw new WorkCenterErrorException("WorkCenter can't be null");
        }
        return workCenterRepository.findById(workCenterId)
                .orElseThrow(() -> new WorkCenterErrorException("WorkCenter not found with id " + workCenterId));
    }

    private User fetchUser(Long userId) {
        if (userId == null) {
            throw new UserNotFoundException("User ID can't be null");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }
}
