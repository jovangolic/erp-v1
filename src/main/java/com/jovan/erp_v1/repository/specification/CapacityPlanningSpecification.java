package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;
import java.time.LocalDate;

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
				.and(hasDate(req.date()))
				.and(hasDateBefore(req.dateBefore()))
				.and(hasDateAfter(req.dateAfter()))
				.and(hasDateBetween(req.dateStart(), req.dateEnd()))
				.and(hasAvailableCapacity(req.availableCapacity()))
				.and(hasAvailableCapacityRange(req.availableCapacityMin(), req.availableCapacityMax()))
				.and(hasPlannedLoad(req.plannedLoad()))
				.and(hasPlannedLoadRange(req.plannedLoadMin(), req.plannedLoadMax()))
				.and(hasRemainingCapacity(req.remainingCapacity()))
				.and(hasRemainingCapacityRange(req.remainingCapacityMin(), req.remainingCapacityMax()))
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
	
	public static Specification<CapacityPlanning> hasRemainingCapacity(BigDecimal m){
		return(root, query, cb) -> m == null ? null : cb.equal(root.get("remainingCapacity"), m);
	}
	
	public static Specification<CapacityPlanning> hasRemainingCapacityRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("remainingCapacity"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("remainingCapacity"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("remainingCapacity"), max);
			}
			return null;
		};
	}
	
	public static Specification<CapacityPlanning> hasPlannedLoadRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("plannedLoad"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("plannedLoad"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("plannedLoad"), max);
			}
			return null;
		};
	}
	
	public static Specification<CapacityPlanning> hasPlannedLoad(BigDecimal pl){
		return(root, query, cb) -> pl == null ? null : cb.equal(root.get("plannedLoad"), pl);
	}
	
	public static Specification<CapacityPlanning> hasAvailableCapacityRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("availableCapacity"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("availableCapacity"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("availableCapacity"), max);
			}
			return null;
		};
	}
	
	public static Specification<CapacityPlanning> hasAvailableCapacity(BigDecimal av){
		return(root, query, cb) -> av == null ? null : cb.equal(root.get("availableCapacity"), av);
	}
	
	public static Specification<CapacityPlanning> hasDateBetween(LocalDate start, LocalDate end){
		return(root, query, cb) -> {
			if(start != null && end != null) {
				return cb.between(root.get("date"), start, end);
			}
			else if(start != null) {
				return cb.greaterThanOrEqualTo(root.get("date"), start);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("date"),end);
			}
			return null;
		};
	}
	
	public static Specification<CapacityPlanning> hasDateAfter(LocalDate da){
		return(root, query, cb) -> da == null ? null : cb.greaterThan(root.get("date"), da);
	}
	
	public static Specification<CapacityPlanning> hasDateBefore(LocalDate db){
		return(root, query, cb) -> db == null ? null : cb.lessThan(root.get("date"), db);
	}
	
	public static Specification<CapacityPlanning> hasDate(LocalDate d){
		return(root, query, cb) -> d == null  ? null : cb.equal(root.get("date"), d);
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
