package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.model.BarCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarCodeResponse {

	private Long id;
	private String code;
	private LocalDateTime scannedAt;
	private String scannedBy;
	private Long goodsId;
    private String goodsName;
    private GoodsType goodsType;
	
	public BarCodeResponse(BarCode code) {
		this.id = code.getId();
		this.code = code.getCode();
		this.scannedAt = code.getScannedAt();
		this.scannedBy = code.getScannedBy();
		this.goodsId = code.getGoods().getId();
		this.goodsName = code.getGoods().getName();
		this.goodsType = code.getGoods().getGoodsType();
	}
	
}
