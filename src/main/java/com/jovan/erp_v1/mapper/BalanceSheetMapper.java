package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.BalanceSheet;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.request.BalanceSheetRequest;
import com.jovan.erp_v1.response.BalanceSheetResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class BalanceSheetMapper extends AbstractMapper<BalanceSheetRequest> {

    public BalanceSheet toEntity(BalanceSheetRequest request, FiscalYear fiscalYear) {
    	Objects.requireNonNull(request, "BalanceSheetRequest must not be null");
    	Objects.requireNonNull(fiscalYear, "FiscalYear must not be null");
        validateIdForUpdate(request, BalanceSheetRequest::id);
        BalanceSheet bs = new BalanceSheet();
        bs.setId(request.id());
        bs.setDate(request.date());
        bs.setTotalAssets(request.totalAssets());
        bs.setTotalLiabilities(request.totalLiabilities());
        bs.setTotalEquity(request.totalEquity());
        bs.setFiscalYear(fiscalYear);
        return bs;
    }

    public BalanceSheet toEntityUpdate(BalanceSheet sheet, BalanceSheetRequest request, FiscalYear fiscalYear) {
        Objects.requireNonNull(request, "BalanceSheetRequest must not be null");
        Objects.requireNonNull(sheet, "BalanceSheet must not be null");
        Objects.requireNonNull(fiscalYear, "FiscalYear must not be null");
        validateIdForUpdate(request, BalanceSheetRequest::id);
        return buildBalanceSheetFromRequest(sheet, request, fiscalYear);
    }
    
    private BalanceSheet buildBalanceSheetFromRequest(BalanceSheet bs, BalanceSheetRequest request, FiscalYear fiscalYear) {
    	bs.setDate(request.date());
        bs.setTotalAssets(request.totalAssets());
        bs.setTotalLiabilities(request.totalLiabilities());
        bs.setTotalEquity(request.totalEquity());
        bs.setFiscalYear(fiscalYear);
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

}
