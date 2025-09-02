package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.TransactionAudit;
import com.jovan.erp_v1.response.TransactionAuditResponse;

@Component
public class TransactionAuditMapper {

	public TransactionAuditResponse toResponse(TransactionAudit ta) {
		Objects.requireNonNull(ta, "TransactionAudit must not be null");
		return new TransactionAuditResponse(ta);
	}
	
	public List<TransactionAuditResponse> toResponseList(List<TransactionAudit> ta){
		if(ta == null || ta.isEmpty()) {
			return Collections.emptyList();
		}
		return ta.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
