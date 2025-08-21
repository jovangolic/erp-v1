package com.jovan.erp_v1.response;

import java.time.LocalDate;

import com.jovan.erp_v1.model.Batch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicBatchResponse {

	private Long id;
	private String code;
	private ProductResponse productResponse;
	private Integer quantityProduced;
	private LocalDate productionDate;
	private LocalDate expiryDate;
	
	public BasicBatchResponse(Batch b) {
		this.id = b.getId();
		this.code = b.getCode();
		this.productResponse = b.getProduct() != null ? new ProductResponse(b.getProduct()) : null;
		this.quantityProduced = b.getQuantityProduced();
		this.productionDate = b.getProductionDate();
		this.expiryDate = b.getExpiryDate();
	}
}
