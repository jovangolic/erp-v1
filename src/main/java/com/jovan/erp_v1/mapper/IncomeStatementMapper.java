package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.FiscalYearErrorException;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.model.IncomeStatement;
import com.jovan.erp_v1.repository.FiscalYearRepository;
import com.jovan.erp_v1.request.IncomeStatementRequest;
import com.jovan.erp_v1.response.FiscalQuarterResponse;
import com.jovan.erp_v1.response.FiscalYearResponse;
import com.jovan.erp_v1.response.IncomeStatementResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IncomeStatementMapper extends AbstractMapper<IncomeStatementRequest> {

    private final FiscalYearRepository fiscalYearRepository;

    public IncomeStatement toEntity(IncomeStatementRequest request) {
        Objects.requireNonNull(request, "IncomeStatementRequest must not be null");
        validateIdForCreate(request, IncomeStatementRequest::id);
        return  buildIncomeStatementFromRequest(new IncomeStatement(), request);
    }

    public IncomeStatement toEntityUpdate(IncomeStatement income, IncomeStatementRequest request) {
    	Objects.requireNonNull(request, "IncomeStatementRequest must not be null");
    	Objects.requireNonNull(income, "IncomeStatement must not be null");
    	validateIdForUpdate(request, IncomeStatementRequest::id);
    	return buildIncomeStatementFromRequest(income, request);
    }
    
    private IncomeStatement buildIncomeStatementFromRequest(IncomeStatement income, IncomeStatementRequest request) {
    	income.setPeriodStart(request.periodStart());
        income.setPeriodEnd(request.periodEnd());
        income.setTotalRevenue(request.totalRevenue());
        income.setTotalExpenses(request.totalExpenses());
        income.setNetProfit(request.netProfit());
        income.setFiscalYear(fetchFiscalYear(request.fiscalYearId()));
        return income;
    }

    public IncomeStatementResponse toResponse(IncomeStatement incomeStatement) {
    	Objects.requireNonNull(incomeStatement, "IncomeStatement must not be null");
        FiscalYear fiscalYear = incomeStatement.getFiscalYear();
        FiscalYearResponse fiscalYearResponse = new FiscalYearResponse(
                fiscalYear.getId(),
                fiscalYear.getYear(),
                fiscalYear.getStartDate(),
                fiscalYear.getEndDate(),
                fiscalYear.getYearStatus(),
                fiscalYear.getQuarterStatus(),
                fiscalYear.getQuarters().stream()
                        .map(FiscalQuarterResponse::new)
                        .collect(Collectors.toList()));

        return new IncomeStatementResponse(
                incomeStatement.getId(),
                incomeStatement.getPeriodStart(),
                incomeStatement.getPeriodEnd(),
                incomeStatement.getTotalRevenue(),
                incomeStatement.getTotalExpenses(),
                incomeStatement.getNetProfit(),
                fiscalYearResponse);
    }

    public List<IncomeStatementResponse> toResponseList(List<IncomeStatement> income) {
    	if(income == null || income.isEmpty()) {
    		return Collections.emptyList();
    	}
        return income.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private FiscalYear fetchFiscalYear(Long id) {
        return fiscalYearRepository.findById(id)
                .orElseThrow(() -> new FiscalYearErrorException("Fiscal-year not found with id: " + id));
    }
}
