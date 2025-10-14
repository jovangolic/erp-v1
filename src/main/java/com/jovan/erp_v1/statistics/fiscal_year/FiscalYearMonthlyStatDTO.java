package com.jovan.erp_v1.statistics.fiscal_year;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FiscalYearMonthlyStatDTO {

	private Integer year;
    private Integer month;
    private Long count;
    
    public FiscalYearMonthlyStatDTO(Integer year, Integer month, Long count) {
    	this.year = year;
    	this.month = month;
    	this.count = count;
    }
}
