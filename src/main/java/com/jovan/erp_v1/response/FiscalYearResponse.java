package com.jovan.erp_v1.response;

import java.time.LocalDate;

import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.enumeration.FiscalYearTypeStatus;
import com.jovan.erp_v1.model.FiscalYear;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// @AllArgsConstructor
public class FiscalYearResponse {

    private Long id;
    private Integer year;
    private LocalDate startDate;
    private LocalDate endDate;
    private FiscalYearStatus yearStatus;
    private FiscalQuarterStatus quarterStatus;
    private List<FiscalQuarterResponse> quarters;
    private FiscalYearTypeStatus status;
    private Boolean confirmed;

    public FiscalYearResponse(FiscalYear year) {
        this.id = year.getId();
        this.startDate = year.getStartDate();
        this.endDate = year.getEndDate();
        this.yearStatus = year.getYearStatus();
        this.quarterStatus = year.getQuarterStatus();
        this.quarters = year.getQuarters().stream()
                .map(FiscalQuarterResponse::new)
                .collect(Collectors.toList());
        this.status = year.getStatus();
        this.confirmed = year.getConfirmed();
    }

    public FiscalYearResponse(Long id, Integer year, LocalDate startDate, LocalDate endDate,
            FiscalYearStatus yearStatus, FiscalQuarterStatus quarterStatus,
            List<FiscalQuarterResponse> quarters, FiscalYearTypeStatus status,Boolean confirmed) {
        this.id = id;
        this.year = year;
        this.startDate = startDate;
        this.endDate = endDate;
        this.yearStatus = yearStatus;
        this.quarterStatus = quarterStatus;
        this.quarters = quarters;
        this.status = status;
        this.confirmed = confirmed;
    }
}
