package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryItemProductResponse {

	private Long id;
    private String name;
    private UnitMeasure unitMeasure;
    private BigDecimal currentQuantity;
    
    public DeliveryItemProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.unitMeasure = product.getUnitMeasure();
        this.currentQuantity = product.getCurrentQuantity();
    }
}
