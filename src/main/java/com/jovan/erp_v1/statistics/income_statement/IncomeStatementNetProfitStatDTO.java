package com.jovan.erp_v1.statistics.income_statement;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IncomeStatementNetProfitStatDTO {

	private Long count;
	private BigDecimal netProfit;
	private Long fiscalYearId;
	private Integer fiscalYear;
	
	public IncomeStatementNetProfitStatDTO(Long count, BigDecimal netProfit, Long fiscalYearId, Integer fiscalYear) {
		this.count = count;
		this.netProfit = netProfit;
		this.fiscalYearId = fiscalYearId;
		this.fiscalYear = fiscalYear;
	}
}
