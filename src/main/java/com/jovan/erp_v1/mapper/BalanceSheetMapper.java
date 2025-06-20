package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.FiscalYearErrorException;
import com.jovan.erp_v1.model.BalanceSheet;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.repository.FiscalYearRepository;
import com.jovan.erp_v1.request.BalanceSheetRequest;
import com.jovan.erp_v1.response.BalanceSheetResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BalanceSheetMapper {

    private final FiscalYearRepository fiscalYearRepository;

    public BalanceSheet toEntity(BalanceSheetRequest request) {
        BalanceSheet sheet = new BalanceSheet();
        sheet.setDate(request.date());
        sheet.setTotalAssets(request.totalAssets());
        sheet.setTotalEquity(request.totalEquity());
        sheet.setTotalLiabilities(request.totalLiabilities());
        sheet.setFiscalYear(fetchFiscalYear(request.fiscalYearId()));
        return sheet;
    }

    public void toEntityUpdate(BalanceSheet sheet, BalanceSheetRequest request) {
        sheet.setDate(request.date());
        sheet.setTotalAssets(request.totalAssets());
        sheet.setTotalLiabilities(request.totalLiabilities());
        sheet.setTotalEquity(request.totalEquity());
        if (request.fiscalYearId() != null) {
            sheet.setFiscalYear(fetchFiscalYear(request.fiscalYearId()));
        }
    }

    public BalanceSheetResponse toResponse(BalanceSheet sheet) {
        return new BalanceSheetResponse(sheet);
    }

    public List<BalanceSheetResponse> toResponseList(List<BalanceSheet> sheets) {
        return sheets.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private FiscalYear fetchFiscalYear(Long id) {
        return fiscalYearRepository.findById(id)
                .orElseThrow(() -> new FiscalYearErrorException("Fiscal-year not found with id: " + id));
    }
}
