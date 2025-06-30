package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.FiscalQuarter;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.request.FiscalYearRequest;
import com.jovan.erp_v1.response.FiscalYearResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FiscalYearMapper {

    private final FiscalQuarterMapper fiscalQuarterMapper;

    public FiscalYear toEntity(FiscalYearRequest request) {
        FiscalYear y = new FiscalYear();
        y.setYear(request.year());
        y.setStartDate(request.startDate());
        y.setEndDate(request.endDate());
        y.setQuarterStatus(request.quarterStatus());
        y.setYearStatus(request.yearStatus());
        List<FiscalQuarter> quarters = request.quarters().stream()
                .map(fiscalRequest -> {
                    FiscalQuarter q = fiscalQuarterMapper.toEntity(fiscalRequest);
                    q.setFiscalYear(y);
                    return q;
                })
                .collect(Collectors.toList());
        y.setQuarters(quarters);
        return y;
    }

    public void updateEntity(FiscalYear year, FiscalYearRequest request) {
        year.setYear(request.year());
        year.setStartDate(request.startDate());
        year.setEndDate(request.endDate());
        year.setQuarterStatus(request.quarterStatus());
        year.setYearStatus(request.yearStatus());
        year.getQuarters().clear();
        List<FiscalQuarter> quarters = request.quarters().stream()
                .map(qReq -> {
                    FiscalQuarter q = new FiscalQuarter();
                    q.setId(qReq.id()); // ili ignoriši ako praviš nove kvartale
                    q.setQuarterStatus(qReq.quarterStatus());
                    q.setStartDate(qReq.startDate());
                    q.setEndDate(qReq.endDate());
                    q.setFiscalYear(year); // poveži nazad sa parent-om
                    return q;
                })
                .collect(Collectors.toList());
        year.setQuarters(quarters);
    }

    public FiscalYearResponse toResponse(FiscalYear year) {
        return new FiscalYearResponse(year);
    }

    public FiscalYearResponse toResponse2(FiscalYear fiscalYear) {
    	Objects.requireNonNull(fiscalYear,"FiscalYear must not be null");
        return new FiscalYearResponse(
                fiscalYear.getId(),
                fiscalYear.getYear(),
                fiscalYear.getStartDate(),
                fiscalYear.getEndDate(),
                fiscalYear.getYearStatus(),
                fiscalYear.getQuarterStatus(),
                fiscalQuarterMapper.toResponseList(fiscalYear.getQuarters()));
    }

    public List<FiscalYearResponse> toResponseList(List<FiscalYear> years) {
    	if(years == null || years.isEmpty()) {
    		return Collections.emptyList();
    	}
        return years.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
