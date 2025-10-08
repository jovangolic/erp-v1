package com.jovan.erp_v1.response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.jovan.erp_v1.enumeration.BatchStatus;
import com.jovan.erp_v1.model.Batch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchResponse {

	private Long id;
	private String code;
	private ProductResponse productResponse;
	private Integer quantityProduced;
	private LocalDate productionDate;
	private LocalDate expiryDate;
	private List<InspectionResponse> inspections;
	private BatchStatus status;
	private Boolean confirmed;
	
	
	public BatchResponse(Batch b) {
		this.id = b.getId();
		this.code = b.getCode();
		this.productResponse = b.getProduct() != null ? new ProductResponse(b.getProduct()) : null;
		this.quantityProduced = b.getQuantityProduced();
		this.productionDate = b.getProductionDate();
		this.expiryDate = b.getExpiryDate();
		this.inspections = b.getInspections().stream()
				.map(InspectionResponse::new)
				.collect(Collectors.toList());
		this.confirmed = b.getConfirmed();
		this.status = b.getStatus();
	}
}
