package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.InboundDeliveryStatus;
import com.jovan.erp_v1.model.InboundDelivery;
import com.jovan.erp_v1.search_request.InboundDeliverySearchRequest;

public class InboundDeliverySpecification {

	public static Specification<InboundDelivery> fromRequest(InboundDeliverySearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasDeliveryDate(req.deliveryDate()))
				.and(hasDeliveryDateBefore(req.deliveryDateBefore()))
				.and(hasDeliveryDateAfter(req.deliveryDateAfter()))
				.and(hasDeliveryDateRange(req.deliveryDateStart(), req.deliveryDateEnd()))
				.and(hasSupplyId(req.supplyId()))
				.and(hasSupplyIdRange(req.supplyIdFrom(), req.supplyIdTo()))
				.and(hasSupplyStorageId(req.storageId()))
				.and(hasSupplyStorageIdRange(req.storageIdFrom(), req.storageIdTo()))
				.and(hasQuantity(req.quantity()))
				.and(hasQuantityRange(req.quantityMin(), req.quantityMax()))
				.and(hasSupplyUpdates(req.updates()))
				.and(hasSupplyUpdatesBefore(req.updatesBefore()))
				.and(hasSupplyUpdatesAfter(req.updatesAfter()))
				.and(hasSupplyUpdatesRange(req.updatesStart(), req.updatesEnd()))
				.and(hasDeliveryStatus(req.status()))
				.and(hasConfirmed(req.confirmed()))
				.and(hasInboundDeliveryStatus(req.inboundStatus()));
	}
	
	public static Specification<InboundDelivery> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<InboundDelivery> hasIdRange(Long from, Long to){
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
	
	public static Specification<InboundDelivery> hasConfirmed(Boolean c){
		return(root, query, cb) -> c == null ? null : cb.equal(root.get("confirmed"), c);
	}
	
	public static Specification<InboundDelivery> hasInboundDeliveryStatus(InboundDeliveryStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("inboundStatus"), status);
	}
	
	public static Specification<InboundDelivery> hasDeliveryStatus(DeliveryStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
	
	public static Specification<InboundDelivery> hasSupplyUpdatesRange(LocalDateTime min, LocalDateTime max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("supply").get("updates"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("supply").get("updates"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("supply").get("updates"), max);
			}
			return null;
		};
	}
	
	public static Specification<InboundDelivery> hasSupplyUpdatesAfter(LocalDateTime aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("supply").get("updates"), aft);
	}
	
	public static Specification<InboundDelivery> hasSupplyUpdatesBefore(LocalDateTime bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("supply").get("updates"), bef);
	}
	
	public static Specification<InboundDelivery> hasSupplyUpdates(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("supply").get("updates"), ld);
	}
	
	public static Specification<InboundDelivery> hasQuantityRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("supply").get("quantity"), min, max);
			}
			else if (min != null) {
				return cb.greaterThanOrEqualTo(root.get("supply").get("quantity"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("supply").get("quantity"), max);
			}
			return null;
		};
	}
	
	public static Specification<InboundDelivery> hasQuantity(BigDecimal q){
		return(root, query, cb) -> q == null ? null : cb.equal(root.get("supply").get("quantity"), q);
	}
	
	public static Specification<InboundDelivery> hasSupplyStorageIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("supply").get("storage").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("supply").get("storage").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("supply").get("storage").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<InboundDelivery> hasSupplyStorageId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("supply").get("storage").get("id"), id);
	}
	
	public static Specification<InboundDelivery> hasSupplyIdRange(Long from, Long to){
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
	
	public static Specification<InboundDelivery> hasSupplyId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("supply").get("id"), id);
	}
	
	public static Specification<InboundDelivery> hasDeliveryDateRange(LocalDate st, LocalDate end){
		return(root, query, cb) -> {
			if(st != null && end != null) {
				return cb.between(root.get("deliveryDate"), st,end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("deliveryDate"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("deliveryDate"), end);
			}
			return null;
		};
	}
	
	public static Specification<InboundDelivery> hasDeliveryDateAfter(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.greaterThan(root.get("delvieryDate"), ld);
	}
	
	public static Specification<InboundDelivery> hasDeliveryDateBefore(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.lessThan(root.get("deliveryDate"), ld);
	}
	
	public static Specification<InboundDelivery> hasDeliveryDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("deliveryDate"), ld);
	}
}
