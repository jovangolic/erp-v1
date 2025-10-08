package com.jovan.erp_v1.statistics.batch;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BatchMonthlyStatDTO {

	private Integer year;
    private Integer month;
    private Long count;
    
    public BatchMonthlyStatDTO(Integer year, Integer month, Long count) {
    	this.year = year;
    	this.month = month;
    	this.count = count;
    }
}
