package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.GoTo;
import com.jovan.erp_v1.request.GoToRequest;
import com.jovan.erp_v1.response.GoToResponse;
import com.jovan.erp_v1.util.AbstractMapper;

@Component
public class GoToMapper extends AbstractMapper<GoToRequest> {

	
	public GoTo toEntity(GoToRequest request) {
		Objects.requireNonNull(request, "GoToRequest must not be null");
		validateIdForCreate(request, GoToRequest::id);
		GoTo gt = new GoTo();
		gt.setId(request.id());
		gt.setLabel(request.label());
		gt.setDescription(request.description());
		gt.setCategory(request.category());
		gt.setType(request.type());
		gt.setPath(request.path());
		gt.setIcon(request.icon());
		gt.setActive(request.active());
		gt.setRoles(request.roles());
		return gt;
	}
	
	public GoTo toEntityUpdate(GoTo gt, GoToRequest request) {
		Objects.requireNonNull(gt, "GoTo must not be null");
		Objects.requireNonNull(request, "GoToRequest must not be null");
		validateIdForUpdate(request, GoToRequest::id);
		gt.setLabel(request.label());
		gt.setDescription(request.description());
		gt.setCategory(request.category());
		gt.setType(request.type());
		gt.setPath(request.path());
		gt.setIcon(request.icon());
		gt.setActive(request.active());
		gt.setRoles(request.roles());
		return gt;
	}
	
	public GoToResponse toResponse(GoTo gt) {
		Objects.requireNonNull(gt, "GoTo must not be null");
		return new GoToResponse(gt);
	}
	
	public List<GoToResponse> toResponseList(List<GoTo> gt){
		if(gt == null || gt.isEmpty()) {
			return Collections.emptyList();
		}
		return gt.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
