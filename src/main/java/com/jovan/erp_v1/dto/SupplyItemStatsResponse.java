package com.jovan.erp_v1.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
/**
 * Za metode u ISupplyItem interfejsu koje vracaju podatke tipa Long i BigDecimal
 */
public class SupplyItemStatsResponse {

	private Long procurementId;
	private Long count;
    private BigDecimal sum;
    private BigDecimal avg;
    private BigDecimal min;
    private BigDecimal max;

    public SupplyItemStatsResponse(Long procurementId, Long count, BigDecimal sum, BigDecimal avg, BigDecimal min, BigDecimal max) {
    	this.procurementId = procurementId;
        this.count = count;
        this.sum = sum;
        this.avg = avg;
        this.min = min;
        this.max = max;
    }
    
 // Factory method for count only
    public static SupplyItemStatsResponse ofCount(Long count) {
        return SupplyItemStatsResponse.builder()
                .count(count)
                .build();
    }

    // Factory method for sum only
    public static SupplyItemStatsResponse ofSum(BigDecimal sum) {
        return SupplyItemStatsResponse.builder()
                .sum(sum)
                .build();
    }

    // Factory method for average only
    public static SupplyItemStatsResponse ofAvg(BigDecimal avg) {
        return SupplyItemStatsResponse.builder()
                .avg(avg)
                .build();
    }

    // Factory method for min only
    public static SupplyItemStatsResponse ofMin(BigDecimal min) {
        return SupplyItemStatsResponse.builder()
                .min(min)
                .build();
    }

    // Factory method for max only
    public static SupplyItemStatsResponse ofMax(BigDecimal max) {
        return SupplyItemStatsResponse.builder()
                .max(max)
                .build();
    }
    
    //Factory method for procurementId only
    public static SupplyItemStatsResponse ofProcurIdAndCount(Long procurementId, Long count) {
        return SupplyItemStatsResponse.builder()
                .procurementId(procurementId)
                .count(count)
                .build();
    }
}
