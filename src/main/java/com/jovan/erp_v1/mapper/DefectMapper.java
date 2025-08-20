package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Defect;
import com.jovan.erp_v1.request.DefectRequest;
import com.jovan.erp_v1.response.DefectResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class DefectMapper extends AbstractMapper<DefectRequest> {

	public Defect toEntity(DefectRequest request) {
		Objects.requireNonNull(request, "DefectRequest must not be null");
		validateIdForCreate(request, DefectRequest::id);
		Defect d = new Defect();
		d.setId(request.id());
		d.setCode(request.code());
		d.setName(request.name());
		d.setDescription(request.description());
		d.setSeverity(request.severity());
		return d;
	}
	
	public Defect toEntityUpdate(Defect d, DefectRequest request) {
		Objects.requireNonNull(d, "Defect must not be null");
		Objects.requireNonNull(request, "DefectRequest must not be null");
		validateIdForUpdate(request, DefectRequest::id);
		d.setCode(request.code());
		d.setName(request.name());
		d.setDescription(request.description());
		d.setSeverity(request.severity());
		return d;
	}
	
	public DefectResponse toResponse(Defect d) {
		Objects.requireNonNull(d,"Defect must not be null");
		return new DefectResponse(d);
	}
	
	public List<DefectResponse> toResponseList(List<Defect> d){
		if(d == null || d.isEmpty()) {
			return Collections.emptyList();
		}
		return d.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
