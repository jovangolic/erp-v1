package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.jovan.erp_v1.enumeration.IncomeStatementStatus;
import com.jovan.erp_v1.model.IncomeStatement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeStatementResponse {

    private Long id;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private BigDecimal totalRevenue;
    private BigDecimal totalExpenses;
    private BigDecimal netProfit;
    private FiscalYearResponse response;
    private IncomeStatementStatus status;
    private Boolean confirmed;

    public IncomeStatementResponse(IncomeStatement income) {
        this.id = income.getId();
        this.periodStart = income.getPeriodStart();
        this.periodEnd = income.getPeriodEnd();
        this.totalRevenue = income.getTotalRevenue();
        this.totalExpenses = income.getTotalExpenses();
        this.netProfit = income.getNetProfit();
        this.response = new FiscalYearResponse(income.getFiscalYear());
        this.status = income.getStatus();
        this.confirmed = income.getConfirmed();
    }
}
