package com.jovan.erp_v1.response;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.model.FiscalQuarter;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.request.FiscalQuarterResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// @AllArgsConstructor
public class FiscalYearResponse {

    private Long id;
    private int year;
    private LocalDate startDate;
    private LocalDate endDate;
    private FiscalYearStatus yearStatus;
    private FiscalQuarterStatus quarterStatus;
    private List<FiscalQuarterResponse> quarters;

    public FiscalYearResponse(FiscalYear year) {
        this.id = year.getId();
        this.startDate = year.getStartDate();
        this.endDate = year.getEndDate();
        this.yearStatus = year.getYearStatus();
        this.quarterStatus = year.getQuarterStatus();
        this.quarters = year.getQuarters().stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
    }

    public FiscalYearResponse(Long id, int year, LocalDate startDate, LocalDate endDate,
            FiscalYearStatus yearStatus, FiscalQuarterStatus quarterStatus,
            List<FiscalQuarterResponse> quarters) {
        this.id = id;
        this.year = year;
        this.startDate = startDate;
        this.endDate = endDate;
        this.yearStatus = yearStatus;
        this.quarterStatus = quarterStatus;
        this.quarters = quarters;
    }
}
