package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.FiscalQuarter;
import com.jovan.erp_v1.request.FiscalQuarterRequest;
import com.jovan.erp_v1.response.FiscalQuarterResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FiscalQuarterMapper extends AbstractMapper<FiscalQuarterRequest> {

    public FiscalQuarter toEntity(FiscalQuarterRequest request) {
    	Objects.requireNonNull(request,"FiscalQuarterRequest must not be null");
    	validateIdForCreate(request, FiscalQuarterRequest::id);
    	return buildFiscalQuarterFromRequest(new FiscalQuarter(), request);
    }

    public FiscalQuarter updateEntity(FiscalQuarter q, FiscalQuarterRequest request) {
    	Objects.requireNonNull(request,"FiscalQuarterRequest must not be null");
    	Objects.requireNonNull(q,"FiscalQuarter must not be null");
    	validateIdForUpdate(request, FiscalQuarterRequest::id);
    	return buildFiscalQuarterFromRequest(q, request);
        
    }
    
    private FiscalQuarter buildFiscalQuarterFromRequest(FiscalQuarter q, FiscalQuarterRequest request) {
    	q.setQuarterStatus(request.quarterStatus());
        q.setStartDate(request.startDate());
        q.setEndDate(request.endDate());
        return q;
    }

    public FiscalQuarterResponse toResponse(FiscalQuarter quarter) {
    	Objects.requireNonNull(quarter,"FiscalQuarter ne sme biti null");
        return new FiscalQuarterResponse(quarter);
    }

    public List<FiscalQuarterResponse> toResponseList(List<FiscalQuarter> quarters) {
    	if(quarters == null || quarters.isEmpty()) {
    		return Collections.emptyList();
    	}
        return quarters.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
