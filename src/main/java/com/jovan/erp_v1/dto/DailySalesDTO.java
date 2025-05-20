package com.jovan.erp_v1.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DailySalesDTO {

    private LocalDate date;
    private BigDecimal totalSales;

    public DailySalesDTO(LocalDate date, BigDecimal totalSales) {
        this.date = date;
        this.totalSales = totalSales;
    }
}
