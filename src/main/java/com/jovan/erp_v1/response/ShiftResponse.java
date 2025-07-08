package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

import com.jovan.erp_v1.model.Shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftResponse {

	private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ShiftSupervisorResponse shiftSupervisorResponse;
    
    public ShiftResponse(Shift shift) {
    	this.id = shift.getId();
    	this.startTime = shift.getStartTime();
    	this.endTime = shift.getEndTime();
    	this.shiftSupervisorResponse = shift.getShiftSupervisor() != null ? new ShiftSupervisorResponse(shift.getShiftSupervisor()) : null;
    }
}
