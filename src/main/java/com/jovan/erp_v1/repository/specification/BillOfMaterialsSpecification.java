package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.BillOfMaterialsStatus;
import com.jovan.erp_v1.model.BillOfMaterials;
import com.jovan.erp_v1.search_request.BillOfMaterialsSearchRequest;

public class BillOfMaterialsSpecification {
	
	public static Specification<BillOfMaterials> fromRequest(BillOfMaterialsSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasRangeId(req.idfrom(), req.idTo()))
				
				.and(hasQuantity(req.quantity()))
				.and(hasQuantityRange(req.quantityMin(), req.quantityMax()))
				.and(hasQuantityLess(req.quantityMin()))
				.and(hasQuantityGreater(req.quantityMax()))
				.and(hasStatus(req.status()))
				.and(hasConfirmed(req.confirmed()));
	}
	
	public static Specification<BillOfMaterials> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<BillOfMaterials> hasRangeId(Long from, Long to){
		return(root,query, cb) -> {
			if(from == null || to == null) return null;
			return cb.between(root.get("id"), from, to);
		};
	}
	
	public static Specification<BillOfMaterials> hasQuantityGreater(BigDecimal max){
		return(root, query, cb) -> max == null ? null : cb.greaterThan(root.get("quantity"), max);
	}
	
	public static Specification<BillOfMaterials> hasQuantityLess(BigDecimal min){
		return(root, query, cb) -> min == null ? null : cb.lessThan(root.get("quantity"), min);
	}
	
	public static Specification<BillOfMaterials> hasQuantityRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("quantity"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("quantity"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("quantity"), max);
			}
			return null;
		};
	}
	
	public static Specification<BillOfMaterials> hasQuantity(BigDecimal q){
		return(root, query, cb) -> q == null ? null : cb.equal(root.get("quantity"), q);
	}
	
	public static Specification<BillOfMaterials> hasStatus(BillOfMaterialsStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
	
	public static Specification<BillOfMaterials> hasConfirmed(Boolean c){
		return(root, query, cb) -> c == null ? null : cb.equal(root.get("c"), c);
	}

	public static Specification<BillOfMaterials> hasParentProductId(Long parentProductId) {
        return (root, query, cb) -> 
        parentProductId == null ? null : cb.equal(root.get("parentProduct").get("id"), parentProductId);
}

	public static Specification<BillOfMaterials> hasComponentId(Long componentId) {
	    return (root, query, cb) -> 
	        componentId == null ? null : cb.equal(root.get("component").get("id"), componentId);
	}
	
	public static Specification<BillOfMaterials> quantityGreaterThanOrEqual(BigDecimal minQuantity) {
	    return (root, query, cb) ->
	        minQuantity == null ? null : cb.greaterThanOrEqualTo(root.get("quantity"), minQuantity);
	}
	
	public static Specification<BillOfMaterials> quantityLessThanOrEqual(BigDecimal maxQuantity) {
	    return (root, query, cb) ->
	        maxQuantity == null ? null : cb.lessThanOrEqualTo(root.get("quantity"), maxQuantity);
}
}
