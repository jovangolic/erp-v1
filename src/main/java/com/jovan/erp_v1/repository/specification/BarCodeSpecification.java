package com.jovan.erp_v1.repository.specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.BarCodeStatus;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.BarCode;
import com.jovan.erp_v1.search_request.BarCodeSearchRequest;

import jakarta.persistence.criteria.Predicate;

public class BarCodeSpecification {

	public static Specification<BarCode> fromRequest(BarCodeSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasRangeId(req.idFrom(),req.idTo()))
				.and(hasCode(req.code()))
				.and(hasScannedAt(req.scannedAt()))
				.and(hasScannedAtBefore(req.scannedAtBefore()))
				.and(hasScannedAtAfter(req.scannedAtAfter()))
				.and(hasScannedRange(req.scannedAtStart(), req.scannedAtEnd()))
				.and(hasUserId(req.userId()))
				.and(hasUserIdRange(req.userIdFrom(), req.userIdTo()))
				.and(hasUserFullName(req.firstName(), req.lastName()))
				.and(hasGoodsId(req.goodsId()))
				.and(hasGoodsIdRange(req.goodsIdFrom(), req.goodsIdTo()))
				.and(hasGoodsName(req.goodsName()))
				.and(hasUnitMeasure(req.unitMeasure()))
				.and(hasSupplierType(req.supplierType()))
				.and(hasStorageType(req.storageType()))
				.and(hasGoodsType(req.goodsType()))
				.and(hasStorageId(req.storageId()))
				.and(hasStorageIdRange(req.storageIdFrom(), req.storageIdTo()))
				.and(hasSupplyId(req.supplyId()))
				.and(hasSupplyIdRange(req.supplyIdFrom(), req.supplyIdTo()))
				.and(hasShelfId(req.shelfId()))
				.and(hasShelfIdRange(req.shelfIdFrom(), req.shelfIdTo()))
				.and(hasStatus(req.status()))
				.and(hasConfirmed(req.confirmed()));
	}
	
	public static Specification<BarCode> hasId(Long id){
		return(root,query,cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<BarCode> hasRangeId(Long from, Long to){
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
	
	public static Specification<BarCode> hasStatus(BarCodeStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
	
	public static Specification<BarCode> hasConfirmed(Boolean c){
		return(root,query, cb) -> c == null ? null : cb.equal(root.get("confirmed"), c);
	}
	
	public static Specification<BarCode> hasShelfIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("goods").get("shelf").get("id"), from, to);
			}
			else if(from != null) {
				cb.greaterThanOrEqualTo(root.get("goods").get("shelf").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("goods").get("shelf").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<BarCode> hasShelfId(Long id){
		return(root,query, cb) -> id == null ? null : cb.equal(root.get("goods").get("shelf").get("id"), id);
	}
	
	public static Specification<BarCode> hasSupplyIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("goods").get("supply").get("id"), from, to);
			}
			else if(from != null) {
				cb.greaterThanOrEqualTo(root.get("goods").get("supply").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("goods").get("supply").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<BarCode> hasSupplyId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("goods").get("supply").get("id"), id);
	}
	
	public static Specification<BarCode> hasStorageIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("goods").get("storage").get("id"), from, to);
			}
			else if(from != null) {
				cb.greaterThanOrEqualTo(root.get("goods").get("storage").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("goods").get("storage").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<BarCode> hasStorageId(Long id){
		return(root,query, cb) -> id == null ? null : cb.equal(root.get("goods").get("storage").get("id"), id);
	}
	
	public static Specification<BarCode> hasGoodsType(GoodsType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("goods").get("goodsType"), type);
	}
	
	public static Specification<BarCode> hasStorageType(StorageType type){
		return(root,query, cb) -> type == null ? null : cb.equal(root.get("goods").get("storageType"), type);
	}
	
	public static Specification<BarCode> hasSupplierType(SupplierType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("goods").get("supplierType"), type);
	}
	
	public static Specification<BarCode> hasUnitMeasure(UnitMeasure um){
		return(root,query,cb) -> um == null ? null : cb.equal(root.get("goods").get("unitMeasure"), um);
	}
	
	public static Specification<BarCode> hasGoodsName(String name){
		return(root, query, cb) -> name == null ? null :
			cb.like(cb.lower(root.get("goods").get("goodsName")), "%" + name.toLowerCase() + "%");
	}
	
	public static Specification<BarCode> hasGoodsIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("goods").get("id"), from, to);
			}
			else if(from != null) {
				cb.greaterThanOrEqualTo(root.get("goods").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("goods").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<BarCode> hasGoodsId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("goods").get("id"), id);
	}
	
	public static Specification<BarCode> hasUserId(Long id){
		return(root,query,cb) -> id == null ? null : cb.equal(root.get("scannedBy").get("id"), id);
	}
	
	public static Specification<BarCode> hasUserFullName(String first, String last){
		return(root, query, cb) -> {
			if((first == null || first.isBlank()) && (last == null || last.isBlank())) return null;
			List<Predicate> predicates = new ArrayList<Predicate>();
			if(first != null && !first.isBlank()) {
				predicates.add(cb.like(cb.lower(root.get("scannedBy").get("firstName")), "%" + first.toLowerCase().trim() + "%"));
			}
			if(last != null && !last.isBlank()) {
				predicates.add(cb.like(cb.lower(root.get("scannedBy").get("lastName")), "%" + last.toLowerCase().trim() + "%"));
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	public static Specification<BarCode> hasUserIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("scannedBy").get("id"), from, to);
			}
			else if(from != null) {
				cb.greaterThanOrEqualTo(root.get("scannedBy").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("scannedBy").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<BarCode> hasScannedRange(LocalDateTime st, LocalDateTime e){
		return(root,query,cb) -> {
			if(st != null && e != null) {
				return cb.between(root.get("scannedAt"), st, e);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("scannedAt"), st);
			}
			else if(e != null) {
				return cb.lessThanOrEqualTo(root.get("scannedAt"), e);
			}
			return null;
		};
	}
	
	public static Specification<BarCode> hasScannedAt(LocalDateTime dt){
		return(root,query,cb) -> dt == null ? null : cb.equal(root.get("scannedAt"), dt);
	}
	
	public static Specification<BarCode> hasScannedAtAfter(LocalDateTime aft){
		return(root,query,cb) -> aft == null ? null : cb.greaterThan(root.get("scannedAt"), aft);
	}
	
	public static Specification<BarCode> hasScannedAtBefore(LocalDateTime bef){
		return(root,query,cb) -> bef == null ? null : cb.lessThan(root.get("scannedAt"), bef);
	}
	
	public static Specification<BarCode> hasCode(String code){
		return(root,query,cb) -> code == null ? null : 
			cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%");
	}
	
	
}
