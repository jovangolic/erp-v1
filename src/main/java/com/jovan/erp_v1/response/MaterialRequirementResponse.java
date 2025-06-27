package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.MaterialRequestStatus;
import com.jovan.erp_v1.model.MaterialRequirement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequirementResponse {

    private Long id;
    private ProductionOrderResponse productionOrderResponse;
    private MaterialResponse materialResponse;
    private BigDecimal requiredQuantity;
    private BigDecimal availableQuantity;
    private LocalDate requirementDate;
    private MaterialRequestStatus status;
    private BigDecimal shortage;

    public MaterialRequirementResponse(MaterialRequirement req) {
        this.id = req.getId();
        this.productionOrderResponse = new ProductionOrderResponse(req.getProductionOrder());
        this.materialResponse = new MaterialResponse(req.getMaterial());
        this.requiredQuantity = req.getRequiredQuantity();
        this.availableQuantity = req.getAvailableQuantity();
        this.requirementDate = req.getRequirementDate();
        this.status = req.getStatus();
        this.shortage = req.getShortage();
    }
}
