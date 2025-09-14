package com.jovan.erp_v1.statistics.defects;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *Defect statistic for month status and their count
 */
@Data
@NoArgsConstructor
public class DefectMonthlyStatDTO {

	private Integer year;
    private Integer month;
    private Long count;
    
    public DefectMonthlyStatDTO(Integer year, Integer month, Long count) {
        this.year = year;
        this.month = month;
        this.count = count;
    }
}
