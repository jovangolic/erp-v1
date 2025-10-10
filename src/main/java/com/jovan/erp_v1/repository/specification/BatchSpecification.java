package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;
import java.time.LocalDate;

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
				.and(hasQuantityProduced(req.quantityProduced()))
				.and(hasQuantityProducedRange(req.quantityProducedMin(), req.quantityProducedMax()))
				.and(hasConfirmed(req.confirmed()))
				.and(hasProductionDate(req.productionDate()))
				.and(hasProductionDateBefore(req.productionDateBefore()))
				.and(hasProductionDateAfter(req.productionDateAfter()))
				.and(hasProductionDateRange(req.productionDateStart(), req.productionDateEnd()))
				.and(hasExpiryDate(req.expiryDate()))
				.and(hasExpiryDateBefore(req.expiryDateBefore()))
				.and(hasExpiryDateAfter(req.expiryDateAfter()))
				.and(hasExpiryDateRange(req.expiryDateStart(), req.expiryDateEnd()))
				.and(hasStatus(req.status()));
	}
	
	public static Specification<Batch> hasId(Long id){
		return(root,query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<Batch> hasIdRange(Long from, Long to){
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
	
	public static Specification<Batch> hasExpiryDateRange(LocalDate min, LocalDate max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("expiryDate"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("expiryDate"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("expiryDate"), max);
			}
			return null;
		};
	}
	
	public static Specification<Batch> hasExpiryDateAfter(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.greaterThan(root.get("expiryDate"), ld);
	}
	
	public static Specification<Batch> hasExpiryDateBefore(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.lessThan(root.get("expiryDate"), ld);
	}
	
	public static Specification<Batch> hasExpiryDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("expiryDate"), ld);
	}

	public static Specification<Batch> hasProductionDateRange(LocalDate min, LocalDate max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("productionDate"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("productionDate"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("productionDate"), max);
			}
			return null;
		};
	}
	
	public static Specification<Batch> hasProductionDateAfter(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.greaterThan(root.get("productionDate"), ld);
	}
	
	public static Specification<Batch> hasProductionDateBefore(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.lessThan(root.get("productionDate"), ld);
	}
	
	public static Specification<Batch> hasProductionDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("productionDate"), ld);
	}
	
	public static Specification<Batch> hasQuantityProducedRange(Integer min, Integer max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("quantityProduced"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("quantityProduced"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("quantityProduced"), max);
			}
			return null;
		};
	}
	
	public static Specification<Batch> hasQuantityProduced(Integer qp){
		return(root, query, cb) -> qp == null ? null : cb.equal(root.get("quantityProduced"), qp);
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
			if(from != null && to != null) {
				return cb.between(root.get("product").get("shelf").get("id"), from, to);
			}
			else if(from != null) {
				cb.greaterThanOrEqualTo(root.get("product").get("shelf").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("product").get("shelf").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Batch> hasShelfId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("product").get("shelf").get("id"), id);
	}
	
	public static Specification<Batch> hasSupplyIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("product").get("supply").get("id"), from, to);
			}
			else if(from != null) {
				cb.greaterThanOrEqualTo(root.get("product").get("supply").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("product").get("supply").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Batch> hasSupplyId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("product").get("supply").get("id"), id);
	}
	
	public static Specification<Batch> hasStorageIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("product").get("storage").get("id"), from, to);
			}
			else if(from != null) {
				cb.greaterThanOrEqualTo(root.get("product").get("storage").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("product").get("storage").get("id"), to);
			}
			return null;
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
	
	
	public static Specification<Batch> hasProductName(String name){
		return(root, query, cb) -> name == null ? null :
			cb.like(cb.lower(root.get("product").get("name")), "%" + name.toLowerCase() + "%");
	}
	
	public static Specification<Batch> hasProductIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("product").get("id"), from, to);
			}
			else if(from != null) {
				cb.greaterThanOrEqualTo(root.get("product").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("product").get("id"), to);
			}
			return null;
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
