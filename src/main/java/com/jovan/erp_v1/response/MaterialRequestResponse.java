package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.MaterialRequestStatus;
import com.jovan.erp_v1.model.MaterialRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequestResponse {

    private Long id;
    private WorkCenterResponse workCenterResponse;
    private MaterialResponse materialResponse;
    private BigDecimal quantity;
    private LocalDate requestDate;
    private LocalDate neededBy;
    private MaterialRequestStatus status;

    public MaterialRequestResponse(MaterialRequest request) {
        this.id = request.getId();
        this.workCenterResponse = new WorkCenterResponse(request.getRequestingWorkCenter());
        this.materialResponse = new MaterialResponse(request.getMaterial());
        this.quantity = request.getQuantity();
        this.requestDate = request.getRequestDate();
        this.neededBy = request.getNeededBy();
        this.status = request.getStatus();
    }
}
