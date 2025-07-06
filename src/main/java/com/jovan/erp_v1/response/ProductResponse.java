package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.Product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    // private String barCode;
    private UnitMeasure unitMeasure;
    private BigDecimal currentQuantity;
    private List<RawMaterialResponse> composition;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        // this.barCode = product.getBarCode();
        this.unitMeasure = product.getUnitMeasure();
        this.currentQuantity = product.getCurrentQuantity();
        this.composition = product.getComposition() == null ? new ArrayList<>()
                : product.getComposition().stream()
                        .map(RawMaterialResponse::new)
                        .collect(Collectors.toList());
    }
}
