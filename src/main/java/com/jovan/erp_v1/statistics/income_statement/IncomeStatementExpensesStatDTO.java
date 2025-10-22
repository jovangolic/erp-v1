package com.jovan.erp_v1.statistics.income_statement;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IncomeStatementExpensesStatDTO {

	private Long count;
	private BigDecimal expenses;
	private Long fiscalYearId;
	private Integer fiscalYear; 

    public IncomeStatementExpensesStatDTO(Long count, BigDecimal expenses, Long fiscalYearId, Integer fiscalYear) {
        this.count = count;
        this.expenses = expenses;
        this.fiscalYearId = fiscalYearId;
        this.fiscalYear = fiscalYear;
    }
}
