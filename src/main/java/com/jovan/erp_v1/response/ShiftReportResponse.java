package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftReportResponse {

	private Long id;
    private String description;
    private LocalDateTime createdAt;
    private Long createdById;
    private String createdByUsername;
    private Long relatedShiftId;
    private String filePath;
	
}
