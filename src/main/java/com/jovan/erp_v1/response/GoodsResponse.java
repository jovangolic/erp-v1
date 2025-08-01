package com.jovan.erp_v1.response;

import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.Goods;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoodsResponse {

	private Long id;
	private String name;
	private List<BarCodeResponse> barCodes;
	private UnitMeasure unitMeasure;
	private SupplierType supplierType;
	private StorageType storageType;
	private GoodsType goodsType;
	private String storageName;
	private Long supplyId;

	public GoodsResponse(Goods goods) {
		this.id = goods.getId();
		this.name = goods.getName();
		this.barCodes = goods.getBarCodes().stream()
				.map(BarCodeResponse::new)
				.collect(Collectors.toList());
		this.unitMeasure = goods.getUnitMeasure();
		this.supplierType = goods.getSupplierType();
		this.storageType = goods.getStorageType();
		this.goodsType = goods.getGoodsType();
		this.storageName = goods.getStorage().getName();
		this.supplyId = goods.getSupply() != null ? goods.getSupply().getId() : null; // sigurnosna provera
	}
}
