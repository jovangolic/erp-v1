package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Defect;
import com.jovan.erp_v1.model.Inspection;
import com.jovan.erp_v1.model.InspectionDefect;
import com.jovan.erp_v1.request.InspectionDefectRequest;
import com.jovan.erp_v1.response.InspectionDefectResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class InspectionDefectMapper extends AbstractMapper<InspectionDefectRequest> {

	public InspectionDefect toEntity(InspectionDefectRequest request, Inspection inspection, Defect defect) {
		Objects.requireNonNull(request, "InspectionDefectRequest must not be null");
		Objects.requireNonNull(inspection, "Inspection must not be null");
		Objects.requireNonNull(defect, "Defect must not be null");
		validateIdForCreate(request, InspectionDefectRequest::id);
		InspectionDefect idef = new InspectionDefect();
		idef.setId(request.id());
		idef.setQuantityAffected(request.quantityAffected());
		idef.setInspection(inspection);
		idef.setDefect(defect);
		return idef;
	}
	
	public InspectionDefect toEntityUpdate(InspectionDefect idef, InspectionDefectRequest request, Inspection inspection, Defect defect) {
		Objects.requireNonNull(idef, "InspectionDefect must not be null");
		Objects.requireNonNull(request, "InspectionDefectRequest must not be null");
		Objects.requireNonNull(inspection, "Inspection must not be null");
		Objects.requireNonNull(defect, "Defect must not be null");
		validateIdForUpdate(request, InspectionDefectRequest::id);
		idef.setQuantityAffected(request.quantityAffected());
		idef.setInspection(inspection);
		idef.setDefect(defect);
		return idef;
	}
	
	public InspectionDefectResponse toResponse(InspectionDefect def) {
		Objects.requireNonNull(def, "InspectionDefect must not be null");
		return new InspectionDefectResponse(def);
	}
	
	public List<InspectionDefectResponse> toResponseList(List<InspectionDefect> def){
		if(def == null || def.isEmpty()) {
			return Collections.emptyList();
		}
		return def.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
