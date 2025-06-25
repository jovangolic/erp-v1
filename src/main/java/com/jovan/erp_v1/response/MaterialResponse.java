package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.model.Material;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialResponse {

    private Long id;
    private String code;
    private String name;
    private UnitOfMeasure unit;
    private BigDecimal currentStock;
    private StorageBasicResponse storage;
    private BigDecimal reorderLevel;

    public MaterialResponse(Material m) {
        this.id = m.getId();
        this.code = m.getCode();
        this.name = m.getName();
        this.unit = m.getUnit();
        this.currentStock = m.getCurrentStock();
        this.storage = new StorageBasicResponse(m.getStorage());
        this.reorderLevel = m.getReorderLevel();
    }
}
