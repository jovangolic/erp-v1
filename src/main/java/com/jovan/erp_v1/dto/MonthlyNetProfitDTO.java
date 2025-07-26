package com.jovan.erp_v1.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MonthlyNetProfitDTO {

	private Integer month;
    private Integer year;
    private BigDecimal netProfit;

    public MonthlyNetProfitDTO(Integer month, Integer year, BigDecimal netProfit) {
        this.month = month;
        this.year = year;
        this.netProfit = netProfit;
    }
}
