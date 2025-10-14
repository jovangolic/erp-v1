package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.search_request.GoodsSearchRequest;

public class GoodsSpecification {

	public static Specification<Goods> fromRequest(GoodsSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasGoodsName(req.name()))
				.and(hasUnitMeasure(req.unitMeasure()))
				.and(hasSupplierType(req.supplierType()))
				.and(hasStorageType(req.storageType()))
				.and(hasGoodsType(req.goodsType()))
				.and(hasStorageId(req.storageId()))
				.and(hasStorageIdRange(req.storageIdFrom(), req.storageIdTo()))
				.and(hasStorageName(req.storageName()))
				.and(hasStorageLoc(req.storageLoc()))
				.and(hasStorageCapacity(req.storageCapacity()))
				.and(hasStorageCapacityRange(req.storageCapacityMin(), req.storageCapacityMax()))
				.and(hasSupplyId(req.supplyId()))
				.and(hasSupplyIdRange(req.supplyIdFrom(), req.supplyIdTo()))
				.and(hasShelfId(req.shelfId()))
				.and(hasShelfIdRange(req.shelfIdFrom(), req.shelfIdTo()));
	}
	
	public static Specification<Goods> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<Goods> hasIdRange(Long from, Long to){
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
	
	public static Specification<Goods> hasShelfIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("shelf").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("shelf").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("shelf").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Goods> hasShelfId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("shelf").get("id"), id);
	}
	
	public static Specification<Goods> hasSupplyIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("supply").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("supply").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("supply").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Goods> hasSupplyId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("supply").get("id"), id);
	}
	
	public static Specification<Goods> hasStorageCapacityRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("storage").get("capacity"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("storage").get("capacity"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("storage").get("capacity"), max);
			}
			return null;
		};
	}
	
	public static Specification<Goods> hasStorageCapacity(BigDecimal bd){
		return(root, query, cb) -> bd == null ? null : cb.equal(root.get("storage").get("capacity"), bd);
	}
	
	public static Specification<Goods> hasStorageLoc(String loc){
		return(root, query, cb) -> loc == null ? null : 
			cb.like(cb.lower(root.get("storage").get("location")), "%" +loc.toLowerCase().trim() + "%");
	}
	
	public static Specification<Goods> hasStorageName(String name){
		return(root, query, cb) -> name == null ? null : 
			cb.like(cb.lower(root.get("storage").get("name")), "%" + name.toLowerCase().trim() + "%");
	}
	
	public static Specification<Goods> hasStorageIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("storage").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("storage").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("storage").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Goods> hasStorageId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("storage").get("id"), id);
	}
	
	public static Specification<Goods> hasGoodsType(GoodsType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("goodsType"), type); 
	}
	
	public static Specification<Goods> hasStorageType(StorageType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("storageType"), type);
	}
	
	public static Specification<Goods> hasSupplierType(SupplierType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("supplierType"), type);
	}
	
	public static Specification<Goods> hasUnitMeasure(UnitMeasure um){
		return(root, query, cb) -> um == null ? null : cb.equal(root.get("unitMeasure"), um);
	}
	
	public static Specification<Goods> hasGoodsName(String name){
		return(root, query, cb) -> name == null ? null :
			cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase().trim() + "%");
	}
}
