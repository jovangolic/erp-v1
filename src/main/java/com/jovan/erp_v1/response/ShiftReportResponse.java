package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

import com.jovan.erp_v1.model.ShiftReport;

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
    private ShiftResponse shiftResponse;
    private String filePath;
    
	public ShiftReportResponse(ShiftReport r) {
		this.id = r.getId();		this.description = r.getDescription();
		this.createdAt = r.getCreatedAt();
		this.shiftResponse = r.getRelatedShift() != null ? new ShiftResponse(r.getRelatedShift()) : null;
		this.filePath = r.getFilePath();
	}
}
