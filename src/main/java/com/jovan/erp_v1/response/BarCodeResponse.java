package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

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
	private UserResponse scannedBy;
	private BasicGoodsForBarCodeResponse basicGoodsForBarCodeResponse;
	
	public BarCodeResponse(BarCode code) {
		this.id = code.getId();
		this.code = code.getCode();
		this.scannedAt = code.getScannedAt();
		this.scannedBy = code.getScannedBy() != null ? new UserResponse(code.getScannedBy()) : null;
		this.basicGoodsForBarCodeResponse = code.getGoods() != null ? new BasicGoodsForBarCodeResponse(code.getGoods()) : null;
	}
}
