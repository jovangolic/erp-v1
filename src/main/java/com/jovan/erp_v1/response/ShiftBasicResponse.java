package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

import com.jovan.erp_v1.model.Shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftBasicResponse {

	private Long id;
	private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    public ShiftBasicResponse(Shift sh) {
    	this.id = sh.getId();
    	this.startTime = sh.getStartTime();
    	this.endTime = sh.getEndTime();
    }
}
