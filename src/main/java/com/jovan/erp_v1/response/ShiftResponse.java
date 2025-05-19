package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

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
    private Long shiftSupervisorId;
    private String shiftSupervisorUsername;
}
