package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.FiscalYearErrorException;
import com.jovan.erp_v1.model.BalanceSheet;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.repository.FiscalYearRepository;
import com.jovan.erp_v1.request.BalanceSheetRequest;
import com.jovan.erp_v1.response.BalanceSheetResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BalanceSheetMapper extends AbstractMapper<BalanceSheetRequest> {

    private final FiscalYearRepository fiscalYearRepository;

    public BalanceSheet toEntity(BalanceSheetRequest request) {
    	Objects.requireNonNull(request, "BalanceSheetRequest must not be null");
        validateIdForUpdate(request, BalanceSheetRequest::id);
        return buildBalanceSheetFromRequest(new BalanceSheet(), request);
    }

    public BalanceSheet toEntityUpdate(BalanceSheet sheet, BalanceSheetRequest request) {
        Objects.requireNonNull(request, "BalanceSheetRequest must not be null");
        Objects.requireNonNull(sheet, "BalanceSheet must not be null");
        validateIdForUpdate(request, BalanceSheetRequest::id);
        return buildBalanceSheetFromRequest(sheet, request);
    }
    
    private BalanceSheet buildBalanceSheetFromRequest(BalanceSheet bs, BalanceSheetRequest request) {
    	bs.setDate(request.date());
        bs.setTotalAssets(request.totalAssets());
        bs.setTotalLiabilities(request.totalLiabilities());
        bs.setTotalEquity(request.totalEquity());
        if (request.fiscalYearId() != null) {
            bs.setFiscalYear(fetchFiscalYear(request.fiscalYearId()));
        }
        return bs;
    }

    public BalanceSheetResponse toResponse(BalanceSheet sheet) {
    	Objects.requireNonNull(sheet,"Balance sheet must not be null");
        return new BalanceSheetResponse(sheet);
    }

    public List<BalanceSheetResponse> toResponseList(List<BalanceSheet> sheets) {
    	if(sheets == null || sheets.isEmpty()) {
    		return Collections.emptyList();
    	}
        return sheets.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private FiscalYear fetchFiscalYear(Long id) {
        return fiscalYearRepository.findById(id)
                .orElseThrow(() -> new FiscalYearErrorException("Fiscal-year not found with id: " + id));
    }
}
