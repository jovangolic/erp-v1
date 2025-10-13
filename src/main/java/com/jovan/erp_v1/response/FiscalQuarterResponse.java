package com.jovan.erp_v1.response;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalQuarterTypeStatus;
import com.jovan.erp_v1.model.FiscalQuarter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiscalQuarterResponse {

    private Long id;
    private FiscalQuarterStatus quarterStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private FiscalQuarterTypeStatus status;
    private Boolean confirmed;

    public FiscalQuarterResponse(FiscalQuarter q) {
        this.id = q.getId();
        this.quarterStatus = q.getQuarterStatus();
        this.startDate = q.getStartDate();
        this.endDate = q.getEndDate();
        this.status = q.getStatus();
        this.confirmed = q.getConfirmed();
    }
}
