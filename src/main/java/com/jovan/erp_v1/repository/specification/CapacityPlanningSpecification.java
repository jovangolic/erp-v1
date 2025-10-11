package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.CapacityPlanningStatus;
import com.jovan.erp_v1.model.CapacityPlanning;
import com.jovan.erp_v1.search_request.CapacityPlanningSearchRequest;

public class CapacityPlanningSpecification {

	public static Specification<CapacityPlanning> fromRequest(CapacityPlanningSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasWorkCenterId(req.workCenterId()))
				.and(hasWorkCenterIdRange(req.workCenterIdFrom(), req.workCenterIdTo()))
				.and(hasWorkCenterName(req.workCenterName()))
				.and(hasWorkCenterLoc(req.workCenterLoc()))
				.and(hasWorkCenterCapacity(req.workCenterCapacity()))
				.and(hasWorkCenterCapacityRange(req.workCenterCapacityMin(), req.workCenterCapacityMax()))
				.and(hasLocalStorageId(req.localStorageId()))
				.and(hasLocalStorageIdRange(req.localStorageFrom(), req.localStorageTo()))
				
				.and(hasStatus(req.status()))
				.and(hasConfirmed(req.confirmed()));
	}
	
	public static Specification<CapacityPlanning> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<CapacityPlanning> hasIdRange(Long from ,Long to){
		return(root, query, cb) ->{
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
	
	public static Specification<CapacityPlanning> hasLocalStorageIdRange(Long from ,Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("workCenter").get("localStorage").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("workCenter").get("localStorage").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("workCenter").get("localStorage").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<CapacityPlanning> hasLocalStorageId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("workCenter").get("localStorage").get("id"), id);
	}
	
	public static Specification<CapacityPlanning> hasWorkCenterCapacityRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("workCenter").get("capacity"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("workCenter").get("capacity"), min);
			}
			else if(max != null){
				return cb.lessThanOrEqualTo(root.get("workCenter").get("capacity"), max);
			}
			return null;
		};
	}
	
	public static Specification<CapacityPlanning> hasWorkCenterCapacity(BigDecimal cp){
		return(root, query, cb) -> cp == null ? null : cb.equal(root.get("workCenter").get("capacity"), cp);
	}
	
	public static Specification<CapacityPlanning> hasWorkCenterLoc(String loc){
		return(root, query, cb) -> loc == null ? null : cb.equal(root.get("workCenter").get("location"), loc);
	}
	
	public static Specification<CapacityPlanning> hasWorkCenterName(String name){
		return(root, query, cb) -> name == null ? null : cb.equal(root.get("workCenter").get("name"), name);
	}
	
	public static Specification<CapacityPlanning> hasWorkCenterId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("workCenter").get("id"), id);
	}
	
	public static Specification<CapacityPlanning> hasWorkCenterIdRange(Long from ,Long to){
		return(root, query, cb) ->{
			if(from != null && to != null) {
				return cb.between(root.get("workCenter").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("workCenter").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("workCenter").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<CapacityPlanning> hasStatus(CapacityPlanningStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
	
	public static Specification<CapacityPlanning> hasConfirmed(Boolean c){
		return(root, query, cb) -> c == null ? null : cb.equal(root.get("confirmed"), c);
	}
}
