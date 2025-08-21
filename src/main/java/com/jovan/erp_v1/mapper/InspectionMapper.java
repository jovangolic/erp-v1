package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Batch;
import com.jovan.erp_v1.model.Inspection;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.QualityCheck;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.request.InspectionRequest;
import com.jovan.erp_v1.response.InspectionResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class InspectionMapper extends AbstractMapper<InspectionRequest> {

	public Inspection toEntity(InspectionRequest request,Batch batch,Product product,User inspector,QualityCheck qualityCheck) {
		Objects.requireNonNull(request, "InspectionRequest must not be null");
		Objects.requireNonNull(batch, "Batch must not be null");
		Objects.requireNonNull(product, "Product must not be null");
		Objects.requireNonNull(inspector, "Inspector must not be null");
		Objects.requireNonNull(qualityCheck, "QualityCheck must not be null");
		validateIdForCreate(request, InspectionRequest::id);
		Inspection i = new Inspection();
		i.setId(request.id());
		i.setCode(request.code());
		i.setType(request.type());
		i.setBatch(batch);
		i.setProduct(product);
		i.setInspector(inspector);
		i.setQuantityInspected(request.quantityInspected());
		i.setQuantityAccepted(request.quantityAccepted());
		i.setQuantityRejected(request.quantityRejected());
		i.setNotes(request.notes());
		i.setResult(request.result());
		i.setQualityCheck(qualityCheck);
		return i;
	}
	
	public Inspection toEntityUpdate(Inspection i,InspectionRequest request,Batch batch,Product product,User inspector,QualityCheck qualityCheck) {
		Objects.requireNonNull(i, "Inspection must not be null");
		Objects.requireNonNull(request, "InspectionRequest must not be null");
		Objects.requireNonNull(batch, "Batch must not be null");
		Objects.requireNonNull(product, "Product must not be null");
		Objects.requireNonNull(inspector, "Inspector must not be null");
		Objects.requireNonNull(qualityCheck, "QualityCheck must not be null");
		validateIdForUpdate(request, InspectionRequest::id);
		i.setCode(request.code());
		i.setType(request.type());
		i.setBatch(batch);
		i.setProduct(product);
		i.setInspector(inspector);
		i.setQuantityInspected(request.quantityInspected());
		i.setQuantityAccepted(request.quantityAccepted());
		i.setQuantityRejected(request.quantityRejected());
		i.setNotes(request.notes());
		i.setResult(request.result());
		i.setQualityCheck(qualityCheck);
		return i;
	}
	
	public InspectionResponse toResponse(Inspection i) {
		Objects.requireNonNull(i, "Inspection must not be null");
		return new InspectionResponse(i);
	}
	
	public List<InspectionResponse> toResponseList(List<Inspection> i){
		if(i == null || i.isEmpty()) {
			return Collections.emptyList();
		}
		return i.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
