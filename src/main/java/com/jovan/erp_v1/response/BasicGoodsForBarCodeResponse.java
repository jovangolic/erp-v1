package com.jovan.erp_v1.response;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.Goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicGoodsForBarCodeResponse {

	private Long id;
	private String name;
	private UnitMeasure unitMeasure;
	private SupplierType supplierType;
	private StorageType storageType;
	private GoodsType goodsType;
	private StorageBasicResponse storageBasicResponse;
	private SupplyForBarCodesResponse supplyForBarCodesResponse;
	private ShelfResponse shelfResponse;
	
	public BasicGoodsForBarCodeResponse(Goods g) {
		this.id = g.getId();
		this.name = g.getName();
		this.unitMeasure = g.getUnitMeasure();
		this.supplierType = g.getSupplierType();
		this.storageType = g.getStorageType();
		this.goodsType = g.getGoodsType();
		this.storageBasicResponse = g.getStorage() != null ? new StorageBasicResponse(g.getStorage()) : null;
		this.supplyForBarCodesResponse = g.getSupplierType() != null ? new SupplyForBarCodesResponse(g.getSupply()) : null;
		this.shelfResponse = g.getShelf() != null ? new ShelfResponse(g.getShelf()) : null;
	}
}
