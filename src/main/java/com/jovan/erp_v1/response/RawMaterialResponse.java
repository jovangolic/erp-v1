package com.jovan.erp_v1.response;

import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.model.BarCode;
import com.jovan.erp_v1.model.RawMaterial;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RawMaterialResponse {

	private Long id;
    private String name;
    private List<BarCodeResponse> barCodes;
    private String unitMeasure;
    private SupplierType supplierType;
    private StorageType storageType;
    private GoodsType goodsType;

    private Long storageId;
    private String storageName; // opciono

    private Long supplyId;
    private Integer currentQuantity;

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

        if (rawMaterial.getStorage() != null) {
            this.storageId = rawMaterial.getStorage().getId();
            this.storageName = rawMaterial.getStorage().getName();
        }

        if (rawMaterial.getSupply() != null) {
            this.supplyId = rawMaterial.getSupply().getId();
        }

        this.currentQuantity = rawMaterial.getCurrentQuantity();

        if (rawMaterial.getProduct() != null) {
            this.productId = rawMaterial.getProduct().getId();
            this.productName = rawMaterial.getProduct().getName();
        }
    }
	
}
