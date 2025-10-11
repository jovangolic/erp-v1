package com.jovan.erp_v1.statistics.capacity_planning;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CapacityPlanningMonthlyStatDTO {

	private Integer year;
    private Integer month;
    private Long count;
    
    public CapacityPlanningMonthlyStatDTO(Integer year, Integer month, Long count) {
    	this.year = year;
    	this.month = month;
    	this.count = count;
    }
}
