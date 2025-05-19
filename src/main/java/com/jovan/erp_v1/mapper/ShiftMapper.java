package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.ShiftRequest;
import com.jovan.erp_v1.response.ShiftResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShiftMapper {

	private final UserRepository userRepository;

    public Shift toEntity(ShiftRequest request) {
        Shift shift = new Shift();
        shift.setStartTime(request.startTime());
        shift.setEndTime(request.endTime());

        if (request.shiftSupervisorId() != null) {
            userRepository.findById(request.shiftSupervisorId())
                .ifPresent(shift::setShiftSupervisor);
        }

        return shift;
    }

    public ShiftResponse toDto(Shift shift) {
        ShiftResponse response = new ShiftResponse();
        response.setId(shift.getId());
        response.setStartTime(shift.getStartTime());
        response.setEndTime(shift.getEndTime());

        if (shift.getShiftSupervisor() != null) {
            response.setShiftSupervisorId(shift.getShiftSupervisor().getId());
            response.setShiftSupervisorUsername(shift.getShiftSupervisor().getEmail());
        }

        return response;
    }
    
    public List<ShiftResponse> toList(List<Shift> responses){
    	return responses.stream()
    			.map(this::toDto)
    			.collect(Collectors.toList());
    }
	
}
