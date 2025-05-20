package com.jovan.erp_v1.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyProcurementCostDTO {

    private YearMonth month;
    private BigDecimal totalCost;
}
