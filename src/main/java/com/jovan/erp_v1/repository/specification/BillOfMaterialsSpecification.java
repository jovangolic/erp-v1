package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.model.BillOfMaterials;

public class BillOfMaterialsSpecification {

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
