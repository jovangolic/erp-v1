package com.jovan.erp_v1.response;


import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.RawMaterial;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RawMaterialResponse {

    private Long id;
    private String name;
    private List<BarCodeResponse> barCodes;
    private UnitMeasure unitMeasure;
    private SupplierType supplierType;
    private StorageType storageType;
    private GoodsType goodsType;
    private StorageBasicResponse storageBasicResponse;
    private SupplyResponse supplyResponse;
    private Long productId;
    private String productName; // opciono

    public RawMaterialResponse(RawMaterial rawMaterial) {
        this.id = rawMaterial.getId();
        this.name = rawMaterial.getName();
        this.barCodes = rawMaterial.getBarCodes().stream()
                .map(BarCodeResponse::new)
                .collect(Collectors.toList());
        this.unitMeasure = rawMaterial.getUnitMeasure();
        this.supplierType = rawMaterial.getSupplierType();
        this.storageType = rawMaterial.getStorageType();
        this.goodsType = rawMaterial.getGoodsType();
        this.storageBasicResponse = rawMaterial.getStorage() != null ? new StorageBasicResponse(rawMaterial.getStorage()) : null;
        this.supplyResponse = rawMaterial.getSupply() != null ? new SupplyResponse(rawMaterial.getSupply()) : null;
        if (rawMaterial.getProduct() != null) {
            this.productId = rawMaterial.getProduct().getId();
            this.productName = rawMaterial.getProduct().getName();
        }
    }

}
