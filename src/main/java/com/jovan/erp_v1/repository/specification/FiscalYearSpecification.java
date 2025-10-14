package com.jovan.erp_v1.repository.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.search_request.FiscalYearSearchRequest;

import jakarta.persistence.criteria.Predicate;

public class FiscalYearSpecification {

	public static Specification<FiscalYear> fromRequest(FiscalYearSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasYear(req.year()))
				.and(hasYearRange(req.yearFrom(), req.yearTo()))
				.and(hasStartDate(req.startDate()))
				.and(hasStartDateBefore(req.startDateBefore()))
				.and(hasStartDateAfter(req.startDateAfter()))
				.and(hasEndDate(req.endDate()))
				.and(hasEndDateBefore(req.endDateBefore()))
				.and(hasEndDateAfter(req.endDateBefore()))
				.and(hasDateRange(req.startDate(), req.endDate()))
				;
	}
	
	public static Specification<FiscalYear> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<FiscalYear> hasIdRange(Long from, Long to){
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
	
	public static Specification<FiscalYear> hasDateRange(LocalDate st, LocalDate end){
		return(root, query, cb) -> {
			if(st == null && end == null) return null;
			Predicate predicate = cb.conjunction();
			if(st != null) {
				predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("startDate"), st));
			}
			if(end != null) {
				predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("endDate"), end));
			}
			return predicate;
		};
	}
	
	public static Specification<FiscalYear> hasEndDateAfter(LocalDate aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("endDate"), aft);
	}
	
	public static Specification<FiscalYear> hasEndDateBefore(LocalDate bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("endDate"), bef);
	}
	
	public static Specification<FiscalYear> hasEndDate(LocalDate end){
		return(root, query, cb) -> end == null ? null : cb.equal(root.get("endDate"), end);
	}
	
	public static Specification<FiscalYear> hasStartDateAfter(LocalDate aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("startDate"), aft);
	}
	
	public static Specification<FiscalYear> hasStartDateBefore(LocalDate bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("startDate"), bef);
	}
	
	public static Specification<FiscalYear> hasStartDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("startDate"), ld);
	}
	
	public static Specification<FiscalYear> hasYearRange(Integer from, Integer to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("year"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("year"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("year"), to);
			}
			return null;
		};
	}
	
	public static Specification<FiscalYear> hasYear(Integer year){
		return(root, query, cb) -> year == null ? null : cb.equal(root.get("year"), year);
	}
}
