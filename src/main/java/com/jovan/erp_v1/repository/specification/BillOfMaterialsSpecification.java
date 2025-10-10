package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.BillOfMaterialsStatus;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.BillOfMaterials;
import com.jovan.erp_v1.search_request.BillOfMaterialsSearchRequest;

public class BillOfMaterialsSpecification {
	
	public static Specification<BillOfMaterials> fromRequest(BillOfMaterialsSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasRangeId(req.idfrom(), req.idTo()))
				.and(hasParentProductId(req.productId()))
				.and(hasParentIdRange(req.productIdFrom(), req.productIdTo()))
				.and(hasParentCurrentQuantity(req.parentCurrentQuantity()))
				.and(hasParentCurrentQuantityRange(req.parentCurrentQuantityMin(), req.parentCurrentQuantityMax()))
				.and(hasParentCurrentQuantityLess(req.parentCurrentQuantity()))
				.and(hasParentCurrentQuantityGreater(req.parentCurrentQuantity()))
				.and(hasProductName(req.productName()))
				.and(hasProductUnitMeasure(req.productUnitMeasure()))
				.and(hasProductSupplierType(req.productSupplierType()))
				.and(hasProductStorageType(req.productStorageType()))
				.and(hasProductGoodsType(req.productGoodsType()))
				.and(hasParentStorageId(req.parentStorageId()))
				.and(hasParentStorageIdRange(req.parentStorageIdFrom(), req.parentStorageIdTo()))
				.and(hasParentSupplyId(req.parentSupplyId()))
				.and(hasParentSupplyIdRange(req.parentSupplyIdFrom(), req.parentSupplyIdTo()))
				.and(hasParentShelfId(req.parentShelfId()))
				.and(hasParentShelfIdRange(req.parentShelfIdFrom(), req.parentShelfIdTo()))
				.and(hasComponentId(req.componentId())
				.and(hasComponentIdRange(req.componentIdFrom(), req.componentIdTo()))
				.and(hasComponentCurrentQuantity(req.componentCurrentQuantity()))
				.and(hasComponentCurrentQuantityRange(req.componentCurrentQuantityMin(), req.componentCurrentQuantityMax()))
				.and(hasComponentCurrentQuantityLess(req.componentCurrentQuantityMin()))
				.and(hasComponentCurrentQuantityGreater(req.componentCurrentQuantityMax()))
				.and(hasComponentName(req.componentName()))
				.and(hasComponentUnitMeasure(req.componentUnitMeasure()))
				.and(hasComponentSupplierType(req.componentSupplierType()))
				.and(hasComponentStorageType(req.componentStorageType()))
				.and(hasComponentGoodsType(req.componentGoodsType()))
				.and(hasComponentStorageId(req.componentStorageId()))
				.and(hasComponentStorageIdRange(req.componentStorageIdFrom(), req.componentStorageIdTo()))
				.and(hasComponentSupplyId(req.componentSupplyId()))
				.and(hasComponentSupplyIdRange(req.componentSupplyIdFrom(), req.componentSupplyIdTo()))
				.and(hasComponentShelfId(req.componentShelfId()))
				.and(hasComponentShelfIdRange(req.componentShelfIdFrom(), req.componentShelfIdTo())))
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
	
	public static Specification<BillOfMaterials> hasComponentShelfIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("component").get("shelf").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("component").get("shelf").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("component").get("shelf").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<BillOfMaterials> hasComponentShelfId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("component").get("shelf").get("id"), id);
	}
	
	public static Specification<BillOfMaterials> hasComponentSupplyIdRange(Long from , Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("component").get("supply").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("component").get("supply").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("component").get("supply").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<BillOfMaterials> hasComponentSupplyId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("component").get("supply").get("id"), id);
	}
	
	public static Specification<BillOfMaterials> hasComponentStorageIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("component").get("storage").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("component").get("storage").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("component").get("storage").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<BillOfMaterials> hasComponentStorageId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("component").get("storage").get("id"), id);
	}
	
	public static Specification<BillOfMaterials> hasComponentGoodsType(GoodsType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("component").get("goodsType"), type);
	}
	
	public static Specification<BillOfMaterials> hasComponentStorageType(StorageType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("component").get("storageType"), type);
	}
	
	public static Specification<BillOfMaterials> hasComponentSupplierType(SupplierType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("component").get("supplierType"), type);
	}
	
	public static Specification<BillOfMaterials> hasComponentUnitMeasure(UnitMeasure um){
		return(root, query, cb) -> um == null  ? null : cb.equal(root.get("component").get("unitMeasure"), um);
	}
	
	public static Specification<BillOfMaterials> hasComponentName(String name){
		return(root, query, cb) -> name == null ? null : 
			cb.like(cb.lower(root.get("component").get("name")), "%" + name.toLowerCase() + "%");
	}
	
	public static Specification<BillOfMaterials> hasComponentCurrentQuantityGreater(BigDecimal bd){
		return(root, query, cb) -> bd == null ? null : cb.lessThan(root.get("component").get("currentQuantity"), bd);
	}
	
	public static Specification<BillOfMaterials> hasComponentCurrentQuantityLess(BigDecimal bd){
		return(root, query, cb) -> bd == null ? null : cb.lessThan(root.get("component").get("currentQuantity"), bd);
	}
	
	public static Specification<BillOfMaterials> hasComponentCurrentQuantityRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("component").get("currentQuantity"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("component").get("currentQuantity"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("component").get("currentQuantity"), max);
			}
			return null;
		};
	}
	
	public static Specification<BillOfMaterials> hasComponentCurrentQuantity(BigDecimal cq){
		return(root, query, cb) -> cq == null ? null : cb.equal(root.get("component").get("currentQuantity"), cq);
	}
	
	public static Specification<BillOfMaterials> hasParentShelfIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("parentProduct").get("shelf").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("parentProduct").get("shelf").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("parentProduct").get("shelf").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<BillOfMaterials> hasParentShelfId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("parentProduct").get("shelf").get("id"), id);
	}
	
	public static Specification<BillOfMaterials> hasParentSupplyIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("parentProduct").get("supply").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("parentProduct").get("supply").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("parentProduct").get("supply").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<BillOfMaterials> hasParentSupplyId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("parentProduct").get("supply").get("id"), id);
	}
	
	public static Specification<BillOfMaterials> hasParentStorageIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("parentProduct").get("storage").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("parentProduct").get("storage").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("parentProduct").get("storage").get("id"), to);
			}
			return null;
		};	
	}
	
	public static Specification<BillOfMaterials> hasParentStorageId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("parentProduct").get("storage").get("id"), id);
	}
	
	public static Specification<BillOfMaterials> hasProductGoodsType(GoodsType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("parentProduct").get("goodsType"), type);
	}
	
	public static Specification<BillOfMaterials> hasProductStorageType(StorageType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("parentProduct").get("storageType"), type);
	}
	
	public static Specification<BillOfMaterials> hasProductSupplierType(SupplierType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("parentProduct").get("supplierType"), type);
	}
	
	public static Specification<BillOfMaterials> hasProductUnitMeasure(UnitMeasure um){
		return(root, query, cb) -> um == null ? null : cb.equal(root.get("parentProduct").get("unitMeasure"), um);
	}
	
	public static Specification<BillOfMaterials> hasProductName(String name){
		return(root, query, cb) -> name == null ? null : 
			cb.like(cb.lower(root.get("parentProduct").get("name")), "%" + name.toLowerCase() + "%");
	}
	
	public static Specification<BillOfMaterials> hasParentCurrentQuantityGreater(BigDecimal max){
		return(root, query, cb) -> max == null ? null : 
			cb.greaterThan(root.get("parentProduct").get("currentQuantity"), max);
	}
	
	public static Specification<BillOfMaterials> hasParentCurrentQuantityLess(BigDecimal min){
		return(root, query, cb) -> min == null ? null : 
			cb.lessThan(root.get("parentProduct").get("currentQuantity"), min);
	}
	
	public static Specification<BillOfMaterials> hasParentCurrentQuantityRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("parentProduct").get("currentQuantity"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("parentProduct").get("currentQuantity"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("parentProduct").get("currentQuantity"), max);
			}
			return null;
		};
	}
	
	public static Specification<BillOfMaterials> hasParentCurrentQuantity(BigDecimal cq){
		return(root, query, cb) -> cq == null ? null : cb.equal(root.get("parentProduct").get("currentQuantity"), cq);
	}
	
	public static Specification<BillOfMaterials> hasRangeId(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("id"), from, to);
			}
			else if(from != null) {
				cb.greaterThanOrEqualTo(root.get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<BillOfMaterials> hasComponentIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("component").get("id"), from, to);
			}
			else if(from != null) {
				cb.greaterThanOrEqualTo(root.get("component").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("component").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<BillOfMaterials> hasParentIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("parentProduct").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("parentProduct").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("parentProduct").get("id"), to);
			}
			return null;
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
