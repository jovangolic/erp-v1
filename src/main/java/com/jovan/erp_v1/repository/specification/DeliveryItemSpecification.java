package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.DeliveryItemStatus;
import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.DeliveryItem;
import com.jovan.erp_v1.search_request.DeliveryItemSearchRequest;

public class DeliveryItemSpecification {

	public static Specification<DeliveryItem> fromRequest(DeliveryItemSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasProductId(req.productId()))
				.and(hasProductIdRange(req.productIdFrom(), req.productIdTo()))
				.and(hasCurrentQuantity(req.currentQuantity()))
				.and(hasCurrentQuantityRange(req.currentQuantityMin(), req.currentQuantityMax()))
				.and(hasProductName(req.productName()))
				.and(hasUnitMeasure(req.unitMeasure()))
				.and(hasSupplierType(req.supplierType()))
				.and(hasStorageType(req.storageType()))
				.and(hasGoodsType(req.goodsType()))
				.and(hasProductStorageId(req.productStorageId()))
				.and(hasProductStorageIdRange(req.productStorageIdFrom(), req.productStorageIdTo()))
				.and(hasProductSupplyId(req.productSupplyId()))
				.and(hasProductSupplyIdRange(req.productSupplyIdFrom(), req.productSupplyIdTo()))
				.and(hasProductShelfId(req.productShelfId()))
				.and(hasProductShelfIdRange(req.productShelfIdFrom(), req.productShelfIdTo()))
				.and(hasInboundDeliveryId(req.inboundDeliveryId()))
				.and(hasInboundDeliveryIdRange(req.inboundDeliveryIdFrom(), req.inboundDeliveryIdTo()))
				.and(hasInboundDeliveryDate(req.inboundDeliveryDate()))
				.and(hasInboundDeliveryDateBefore(req.inboundDeliveryDateBefore()))
				.and(hasInboundDeliveryDateAfter(req.inboundDeliveryDateAfter()))
				.and(hasInboundDeliveryDateRange(req.inboundDeliveryDateStart(), req.inboundDeliveryDateEnd()))
				.and(hasInboundSupplyId(req.inboundSupplyId()))
				.and(hasInboundSupplyIdRange(req.inboundSupplyIdFrom(), req.inboundSupplyIdTo()))
				.and(hasInboundDeliveryStatus(req.inboundDeliveryStatus()))
				.and(hasOutboundDeliveryId(req.outboundDeliveryId()))
				.and(hasOutboundDeliveryIdRange(req.outboundDeliveryIdFrom(), req.outboundDeliveryIdTo()))
				.and(hasOutboundDeliveryDate(req.outboundDeliveryDate()))
				.and(hasOutboundDeliveryDateBefore(req.outboundDeliveryDateBefore()))
				.and(hasOutboundDeliveryDateAfter(req.outboundDeliveryDateAfter()))
				.and(hasOutboundDeliveryDateRange(req.outboundDeliveryDateStart(), req.outboundDeliveryDateEnd()))
				.and(hasOutboundBuyerId(req.buyerId()))
				.and(hasOutboundBuyerIdRange(req.buyerIdFrom(), req.buyerIdTo()))
				.and(hasOutboundDeliveryStatus(req.outboundStatus()))
				.and(hasQuantity(req.quantity()))
				.and(hasQuantityRange(req.quantityMin(), req.quantityMax()))
				.and(hasStatus(req.status()))
				.and(hasConfirmed(req.confirmed()));
	}
	
	public static Specification<DeliveryItem> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<DeliveryItem> hasIdRange(Long from, Long to){
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
	
	public static Specification<DeliveryItem> hasQuantityRange(BigDecimal min, BigDecimal max){
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
	
	public static Specification<DeliveryItem> hasQuantity(BigDecimal q){
		return(root, query, cb) -> q == null ? null : cb.equal(root.get("quantity"), q);
	}
	
	public static Specification<DeliveryItem> hasOutboundDeliveryStatus(DeliveryStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("outboundDelivery").get("status"), status);
	}
	
	public static Specification<DeliveryItem> hasOutboundBuyerIdRange(Long from , Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("outboundDelivery").get("buyer").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("outboundDelivery").get("buyer").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("outboundDelivery").get("buyer").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<DeliveryItem> hasOutboundBuyerId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("outboundDelivery").get("buyer").get("id"), id); 
	}
	
	public static Specification<DeliveryItem> hasOutboundDeliveryDateRange(LocalDate st, LocalDate end){
		return(root, query, cb) -> {
			if(st != null && end != null) {
				return cb.between(root.get("outboundDelivery").get("deliveryDate"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("outboundDelivery").get("deliveryDate"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("outboundDelivery").get("deliveryDate"), end);
			}
			return null;
		};
	}
	
	public static Specification<DeliveryItem> hasOutboundDeliveryDateAfter(LocalDate aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("outboundDelivery").get("deliveryDate"), aft);
	}
	
	public static Specification<DeliveryItem> hasOutboundDeliveryDateBefore(LocalDate bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("outboundDelivery").get("deliveryDate"), bef);
	}
	
	public static Specification<DeliveryItem> hasOutboundDeliveryDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("outboundDelivery").get("deliveryDate"), ld);
	}
	
	public static Specification<DeliveryItem> hasOutboundDeliveryIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("outboundDelivery").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("outboundDelivery").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("outboundDelivery").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<DeliveryItem> hasOutboundDeliveryId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("outboundDelivery").get("id"), id);
	}
	
	public static Specification<DeliveryItem> hasInboundDeliveryStatus(DeliveryStatus status){
		return(root, query,cb) -> status == null ? null : cb.equal(root.get("inboundDelivery").get("status"), status);
	}
	
	public static Specification<DeliveryItem> hasInboundSupplyIdRange(Long from ,Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("inboundDelivery").get("supply").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("inboundDelivery").get("supply").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("inboundDelivery").get("supply").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<DeliveryItem> hasInboundSupplyId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("inboundDelivery").get("supply").get("id"), id);
	}
	
	public static Specification<DeliveryItem> hasInboundDeliveryDateRange(LocalDate st, LocalDate end){
		return(root, query, cb) -> {
			if(st != null && end != null) {
				return cb.between(root.get("inboundDelivery").get("deliveryDate"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("inboundDelivery").get("deliveryDate"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("inboundDelivery").get("deliveryDate"), end);
			}
			return null;
		};
	}
	
	public static Specification<DeliveryItem> hasInboundDeliveryDateAfter(LocalDate aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("inboundDelivery").get("deliveryDate"), aft);
	}
	
	public static Specification<DeliveryItem> hasInboundDeliveryDateBefore(LocalDate bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("inboundDelivery").get("deliveryDate"), bef);
	}
	
	public static Specification<DeliveryItem> hasInboundDeliveryDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("inboundDelivery").get("deliveryDate"), ld);
	}
	
	public static Specification<DeliveryItem> hasInboundDeliveryIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("inboundDelivery").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("inboundDelivery").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("inboundDelivery").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<DeliveryItem> hasInboundDeliveryId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("inboundDelivery").get("id"), id);
	}
	
	public static Specification<DeliveryItem> hasProductShelfIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("product").get("shelf").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("product").get("shelf").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("product").get("shelf").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<DeliveryItem> hasProductShelfId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("product").get("shelf").get("id"), id);
	}
	
	public static Specification<DeliveryItem> hasProductSupplyIdRange(Long from ,Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("product").get("supply").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("product").get("supply").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("product").get("supply").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<DeliveryItem> hasProductSupplyId(Long id){
		return(root, query, cb) -> cb.equal(root.get("product").get("supply").get("id"), id);
	}
	
	public static Specification<DeliveryItem> hasProductStorageIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("product").get("storage").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("product").get("storage").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("product").get("storage").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<DeliveryItem> hasProductStorageId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("product").get("storage").get("id"), id);
	}
	
	public static Specification<DeliveryItem> hasGoodsType(GoodsType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("product").get("goodsType"),type);
	}
	
	public static Specification<DeliveryItem> hasStorageType(StorageType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("product").get("storageType"), type);
	}
	
	public static Specification<DeliveryItem> hasSupplierType(SupplierType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("product").get("supplierType"), type);
	}
	
	public static Specification<DeliveryItem> hasUnitMeasure(UnitMeasure um){
		return(root, query, cb) -> um == null ? null : cb.equal(root.get("product").get("unitMeasure"), um);
	}
	
	public static Specification<DeliveryItem> hasProductName(String name){
		return(root, query, cb) -> name == null ? null : 
			cb.like(cb.lower(root.get("product").get("name")), "%" + name.toLowerCase() + "%");
	}
	
	public static Specification<DeliveryItem> hasCurrentQuantityRange(BigDecimal min, BigDecimal max){
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
	
	public static Specification<DeliveryItem> hasCurrentQuantity(BigDecimal cq){
		return(root, query, cb) -> cq == null ? null : cb.equal(root.get("product").get("currentQuantity"), cq);
	}
	
	public static Specification<DeliveryItem> hasProductIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("product").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("product").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("product").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<DeliveryItem> hasProductId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("product").get("id"), id);
	}
	
	public static Specification<DeliveryItem> hasStatus(DeliveryItemStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
	
	public static Specification<DeliveryItem> hasConfirmed(Boolean c){
		return(root, query, cb) -> c == null ? null : cb.equal(root.get("confirmed"), c);
	}
}
