package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.model.BalanceSheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceSheetResponse {

    private Long id;
    private LocalDate date;
    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
    private BigDecimal totalEquity;
    private FiscalYearResponse fiscalYear;

    public BalanceSheetResponse(BalanceSheet sheet) {
        this.id = sheet.getId();
        this.date = sheet.getDate();
        this.totalAssets = sheet.getTotalAssets();
        this.totalLiabilities = sheet.getTotalLiabilities();
        this.totalEquity = sheet.getTotalEquity();
        this.fiscalYear = sheet.getFiscalYear() != null ? new FiscalYearResponse(sheet.getFiscalYear()) : null;
    }
}
