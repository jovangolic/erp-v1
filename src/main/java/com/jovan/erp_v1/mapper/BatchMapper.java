package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Batch;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.request.BatchRequest;
import com.jovan.erp_v1.response.BatchResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class BatchMapper extends AbstractMapper<BatchRequest> {

	public Batch toEntity(BatchRequest request, Product product) {
		Objects.requireNonNull(request, "BatchRequest must not be null");
		Objects.requireNonNull(product, "Product must not be null");
		validateIdForCreate(request, BatchRequest::id);
		Batch b = new Batch();
		b.setId(request.id());
		b.setProduct(product);
		b.setQuantityProduced(request.quantityProduced());
		b.setExpiryDate(request.expiryDate());
		return b;
	}
	
	public Batch toEntityUpdate(Batch b,BatchRequest request, Product product) {
		Objects.requireNonNull(b, "Batch must not be null");
		Objects.requireNonNull(request, "BatchRequest must not be null");
		Objects.requireNonNull(product, "Product must not be null");
		validateIdForUpdate(request, BatchRequest::id);
		b.setProduct(product);
		b.setQuantityProduced(request.quantityProduced());
		b.setExpiryDate(request.expiryDate());
		return b;
	}
	
	public BatchResponse toResponse(Batch b) {
		Objects.requireNonNull(b, "Batch must not be null");
		return new BatchResponse(b);
	}
	
	public List<BatchResponse> toResponseList(List<Batch> b){
		if(b == null || b.isEmpty()) {
			return Collections.emptyList();
		}
		return b.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
