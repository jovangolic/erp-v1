package com.jovan.erp_v1.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.model.ConfirmationDocument;
import com.jovan.erp_v1.search_request.ConfirmationDocumentSearchRequest;

public class ConfirmationDocumentSpecification {
	
	public static Specification<ConfirmationDocument> fromRequest(ConfirmationDocumentSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()));
	}
	
	public static Specification<ConfirmationDocument> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<ConfirmationDocument> hasIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("id"), to);
			}
			return null;
		};
	}
}
