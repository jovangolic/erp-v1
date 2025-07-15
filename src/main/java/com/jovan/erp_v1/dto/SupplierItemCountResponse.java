package com.jovan.erp_v1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplierItemCountResponse {

	private Long supplierId;
    private Long itemCount;

    public SupplierItemCountResponse(Long supplierId, Long itemCount) {
        this.supplierId = supplierId;
        this.itemCount = itemCount;
    }
}
