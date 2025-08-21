package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.QualityStandard;
import com.jovan.erp_v1.request.QualityStandardRequest;
import com.jovan.erp_v1.response.QualityStandardResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class QualityStandardMapper extends AbstractMapper<QualityStandardRequest> {

	public QualityStandard toEntity(QualityStandardRequest request, Product product) {
		Objects.requireNonNull(request, "QualityStandardRequest must not be null");
		Objects.requireNonNull(product, "Product must not be null");
		validateIdForCreate(request, QualityStandardRequest::id);
		QualityStandard qs = new QualityStandard();
		qs.setId(request.id());
		qs.setProduct(product);
		qs.setDescription(request.description());
		qs.setMinValue(request.minValue());
		qs.setMaxValue(request.maxValue());
		qs.setUnit(request.unit());
		return qs;
	}
	
	public QualityStandard toEntityUpdate(QualityStandard qs,QualityStandardRequest request, Product product) {
		Objects.requireNonNull(qs, "QualityStandard must not be null");
		Objects.requireNonNull(request, "QualityStandardRequest must not be null");
		Objects.requireNonNull(product, "Product must not be null");
		validateIdForUpdate(request, QualityStandardRequest::id);
		qs.setProduct(product);
		qs.setDescription(request.description());
		qs.setMinValue(request.minValue());
		qs.setMaxValue(request.maxValue());
		qs.setUnit(request.unit());
		return qs;
	}
	
	public QualityStandardResponse toResponse(QualityStandard qs) {
		Objects.requireNonNull(qs, "QualityStandard must not be null");
		return new QualityStandardResponse(qs);
	}
	
	public List<QualityStandardResponse> toResponseList(List<QualityStandard> qs){
		if(qs == null || qs.isEmpty()) {
			return Collections.emptyList();
		}
		return qs.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
