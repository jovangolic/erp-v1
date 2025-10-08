package com.jovan.erp_v1.repository.specification;


import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.BatchStatus;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.Batch;
import com.jovan.erp_v1.search_request.BatchSearchRequest;

public class BatchSpecification {

	public static Specification<Batch> fromRequest(BatchSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasCode(req.code()))
				.and(hasProductId(req.productId()))
				.and(hasProductIdRange(req.productIdFrom(), req.productIdTo()))
				.and(hasProductName(req.productName()))
				.and(hasCurrentQuantityLess(req.currentQuantity()))
				.and(hasCurrentQuantityGreater(req.currentQuantity()))
				.and(hasCurrentQuantityRange(req.currentQuantityMin(), req.currentQuantityMax()))
				.and(hasUnitMeasure(req.unitMeasure()))
				.and(hasStorageType(req.storageType()))
				.and(hasSupplierType(req.supplierType()))
				.and(hasGoodsType(req.goodsType()))
				.and(hasStorageId(req.storageId()))
				.and(hasStorageIdRange(req.storageIdFrom(), req.storageIdTo()))
				.and(hasSupplyId(req.supplyId()))
				.and(hasSupplyIdRange(req.supplyIdFrom(), req.supplyIdTo()))
				.and(hasShelfId(req.shelfId()))
				.and(hasShelfIdRange(req.shelfIdFrom(), req.shelfIdTo()))
				.and(hasCurrentQuantity(req.currentQuantity()))
				
				.and(hasConfirmed(req.confirmed()))
				.and(hasStatus(req.status()));
	}
	
	public static Specification<Batch> hasId(Long id){
		return(root,query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<Batch> hasIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from == null || to == null) return null;
			return cb.between(root.get("id"), from, to);
		};
	}
	
	public static Specification<Batch> hasStatus(BatchStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
	
	public static Specification<Batch> hasConfirmed(Boolean conf){
		return(root, query, cb) -> conf == null ? null : cb.equal(root.get("confirmed"), conf);
	}
	
	public static Specification<Batch> hasCurrentQuantity(BigDecimal cq){
		return(root, query, cb) -> cq == null ? null : cb.equal(root.get("product").get("currentQuantity"), cq);
	}
	
	public static Specification<Batch> hasShelfIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from == null || to == null) return null;
			return cb.between(root.get("product").get("shelf").get("id"), from, to);
		};
	}
	
	public static Specification<Batch> hasShelfId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("product").get("shelf").get("id"), id);
	}
	
	public static Specification<Batch> hasSupplyIdRange(Long from, Long to){
		return(root, query, cb) ->{
			if(from == null || to == null) return null;
			return cb.between(root.get("product").get("supply").get("id"), from, to);
		};
	}
	
	public static Specification<Batch> hasSupplyId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("product").get("supply").get("id"), id);
	}
	
	public static Specification<Batch> hasStorageIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from == null || to == null) return null;
			return cb.between(root.get("product").get("storage").get("id"), from, to);
		};
	}
	
	public static Specification<Batch> hasStorageId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("product").get("storage").get("id"), id);
	}
	
	public static Specification<Batch> hasGoodsType(GoodsType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("product").get("goodsType"), type);
	}
	
	public static Specification<Batch> hasSupplierType(SupplierType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("product").get("supplierType"), type);
	}
	
	public static Specification<Batch> hasStorageType(StorageType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("product").get("storageType"), type);
	}
	
	public static Specification<Batch> hasUnitMeasure(UnitMeasure um){
		return(root, query, cb) -> um == null ? null : cb.equal(root.get("product").get("unitMeasure"), um);
	}
	
	public static Specification<Batch> hasCurrentQuantityRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("product").get("currentQuantity"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("product").get("currentQuantity"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("product").get("currentQuantity"), max);
			}
			return null;
		};
	}
	
	public static Specification<Batch> hasCurrentQuantityGreater(BigDecimal cq){
		return(root, query, cb) -> cq == null ? null : cb.greaterThan(root.get("product").get("currentQuantity"), cq);
	}
	
	public static Specification<Batch> hasCurrentQuantityLess(BigDecimal cq){
		return(root, query, cb) -> cq == null ? null : cb.lessThan(root.get("product").get("currentQuantity"), cq);
	}
	
	public static Specification<Batch> hasProductName(String name){
		return(root, query, cb) -> name == null ? null :
			cb.like(cb.lower(root.get("product").get("name")), "%" + name.toLowerCase() + "%");
	}
	
	public static Specification<Batch> hasProductIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from == null || to == null) return null;
			return cb.between(root.get("product").get("id"), from, to);
		};
	}
	
	public static Specification<Batch> hasProductId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("product").get("id"), id);
	}
	
	public static Specification<Batch> hasCode(String code){
		return(root, query, cb) -> code == null ? null 
				: cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%");
	}
}
