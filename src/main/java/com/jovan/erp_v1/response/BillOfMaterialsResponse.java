package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.model.BillOfMaterials;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillOfMaterialsResponse {

    private Long id;
    private BasicProductResponse parentProduct;
    private BasicProductResponse component;
    private BigDecimal quantity;

    public BillOfMaterialsResponse(BillOfMaterials bom) {
        this.id = bom.getId();
        this.parentProduct = new BasicProductResponse(bom.getParentProduct());
        this.component = new BasicProductResponse(bom.getComponent());
        this.quantity = bom.getQuantity();
    }
}
