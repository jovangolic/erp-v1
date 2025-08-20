package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.QualityCheck;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.request.QualityCheckRequest;
import com.jovan.erp_v1.response.QualityCheckResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class QualityCheckMapper extends AbstractMapper<QualityCheckRequest> {

	public QualityCheck toEntity(QualityCheckRequest request, User inspector) {
		Objects.requireNonNull(request, "QualityCheckRequest must not be null");
		Objects.requireNonNull(inspector, "Inspector must not be null");
		validateIdForCreate(request, QualityCheckRequest::id);
		QualityCheck qc = new QualityCheck();
		qc.setId(request.id());
		qc.setInspector(inspector);
		qc.setReferenceType(request.referenceType());
		qc.setReferenceId(request.referenceId());
		qc.setCheckType(request.checkType());
		qc.setStatus(request.status());
		qc.setNotes(request.notes());
		return qc;
	}
	
	public QualityCheck toEntityUpdate(QualityCheck qc, QualityCheckRequest request, User inspector) {
		Objects.requireNonNull(qc, "QualityCheck must not be null");
		Objects.requireNonNull(request, "QualityCheckRequest must not be null");
		Objects.requireNonNull(inspector, "Inspector must not be null");
		validateIdForUpdate(request, QualityCheckRequest::id);
		qc.setInspector(inspector);
		qc.setReferenceType(request.referenceType());
		qc.setReferenceId(request.referenceId());
		qc.setCheckType(request.checkType());
		qc.setStatus(request.status());
		qc.setNotes(request.notes());
		return qc;
	}
	
	public QualityCheckResponse toResponse(QualityCheck qc) {
		Objects.requireNonNull(qc, "QualityCheck must not be null");
		return new QualityCheckResponse(qc);
	}
	
	public List<QualityCheckResponse> toResponseList(List<QualityCheck> qc){
		if(qc == null || qc.isEmpty()) {
			return Collections.emptyList();
		}
		return qc.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
