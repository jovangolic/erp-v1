package com.jovan.erp_v1.repository.specification;

import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalQuarterTypeStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.model.FiscalQuarter;
import com.jovan.erp_v1.search_request.FiscalQuarterSearchRequest;

import jakarta.persistence.criteria.Predicate;

public class FiscalQuarterSpecification {

	public static Specification<FiscalQuarter> fromRequest(FiscalQuarterSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasStartDate(req.startDate()))
				.and(hasStartDateBefore(req.startDateBefore()))
				.and(hasStartDateAfter(req.startDateAfter()))
				.and(hasEndDate(req.endDate()))
				.and(hasEndDateBefore(req.endDateBefore()))
				.and(hasEndDateAfter(req.endDateAfter()))
				.and(hasDateTimeRange(req.startDate(), req.endDate()))
				.and(hasFiscalYearId(req.fiscalYearId()))
				.and(hasFiscalYearIdRange(req.fiscalYearIdFrom(), req.fiscalYearIdTo()))
				.and(hasFiscalYear(req.year()))
				.and(hasFiscalYearRange(req.yearFrom(), req.yearTo()))
				.and(hasFiscalStartDate(req.fiscalStartDate()))
				.and(hasFiscalStartDateBefore(req.fiscalStartDateBefore()))
				.and(hasFiscalStartDateAfter(req.fiscalStartDateAfter()))
				.and(hasFiscalEndDate(req.fiscalEndDate()))
				.and(hasFiscalEndDateBefore(req.fiscalEndDateAfter()))
				.and(hasFiscalEndDateAfter(req.fiscalEndDateAfter()))
				.and(hasFiscalDateRange(req.fiscalStartDate(), req.fiscalEndDate()))
				.and(hasFiscalYearStatus(req.yearStatus()))
				.and(hasFiscalYearQuarterStatus(req.fiscalQuarterStatus()))
				.and(hasFiscalQuarterStatus(req.quarterStatus()))
				.and(hasTypeStatus(req.status()))
				.and(hasConfirmed(req.confirmed()));
	}
	
	public static Specification<FiscalQuarter> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<FiscalQuarter> hasIdRange(Long from, Long to){
		return(root, query,cb) -> {
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
	
	public static Specification<FiscalQuarter> hasFiscalYearQuarterStatus(FiscalQuarterStatus quarterStatus){
		return(root, query, cb) -> quarterStatus == null ? null : cb.equal(root.get("fiscalYear").get("quarterStatus"), quarterStatus);
	}
	
	public static Specification<FiscalQuarter> hasFiscalYearStatus(FiscalYearStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("fiscalYear").get("yearStatus"), status);
	}
	
	public static Specification<FiscalQuarter> hasFiscalDateRange(LocalDate start, LocalDate end){
		return(root, query, cb) -> {
			if(start == null && end == null) return null;
			Predicate predicate = cb.conjunction();
			if(start != null) {
				predicate = cb.and(predicate,cb.greaterThanOrEqualTo(root.get("fiscalYear").get("startDate"), start));
			}
			if(end != null) {
				predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("fiscalYear").get("endDate"), end));
			}
			return predicate;
		};
	}
	
	public static Specification<FiscalQuarter> hasFiscalEndDateAfter(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.greaterThan(root.get("fiscalYear").get("endDate"), ld);
	}
	
	public static Specification<FiscalQuarter> hasFiscalEndDateBefore(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.lessThan(root.get("fiscalYear").get("endDate"), ld);
	}
	
	public static Specification<FiscalQuarter> hasFiscalEndDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("fiscalYear").get("endDate"), ld);
	}
	
	public static Specification<FiscalQuarter> hasFiscalStartDateAfter(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.greaterThan(root.get("fiscalYear").get("id"), ld);
	}
	
	public static Specification<FiscalQuarter> hasFiscalStartDateBefore(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.lessThan(root.get("fiscalYear").get("startDate"), ld);
	}
	
	public static Specification<FiscalQuarter> hasFiscalStartDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("fiscalYear").get("startDate"), ld);
	}
	
	public static Specification<FiscalQuarter> hasFiscalYear(Integer year){
		return(root, query, cb) -> year == null ? null : cb.equal(root.get("fiscalYear").get("year"), year);
	}
	
	public static Specification<FiscalQuarter> hasFiscalYearRange(Integer from ,Integer to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("fiscalYear").get("year"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("fiscalYear").get("year"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("fiscalYear").get("year"), to);
			}
			return null;
		};
	}
	
	public static Specification<FiscalQuarter> hasFiscalYearId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("fiscalYear").get("id"), id);
	}
	
	public static Specification<FiscalQuarter> hasFiscalYearIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("fiscalYear").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("fiscalYear").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("fiscalYear").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<FiscalQuarter> hasDateTimeRange(LocalDate start, LocalDate end){
		return(root, query, cb) -> {
			if(start == null  && end == null) {
				return null;
			}
			Predicate predicate = cb.conjunction();
	        if (start != null) {
	            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("startDate"), start));
	        }
	        if (end != null) {
	            predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("endDate"), end));
	        }
	        return predicate;
		};
	}
	
	public static Specification<FiscalQuarter> hasEndDateAfter(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.greaterThan(root.get("endDate"), ld);
	}
	
	public static Specification<FiscalQuarter> hasEndDateBefore(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.lessThan(root.get("endDate"), ld);
	}
	
	public static Specification<FiscalQuarter> hasEndDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("endDate"), ld);
	}
	
	public static Specification<FiscalQuarter> hasStartDateAfter(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.greaterThan(root.get("startDate"), ld);
	}
	
	public static Specification<FiscalQuarter> hasStartDateBefore(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.lessThan(root.get("startDate"), ld);
	}
	
	public static Specification<FiscalQuarter> hasStartDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("startDate"), ld);
	}
	
	public static Specification<FiscalQuarter> hasFiscalQuarterStatus(FiscalQuarterStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("quarterStatus"), status);
	}
	
	public static Specification<FiscalQuarter> hasConfirmed(Boolean conf){
		return(root, query, cb) -> conf == null ? null : cb.equal(root.get("confirmed"), conf);
	}
	
	public static Specification<FiscalQuarter> hasTypeStatus(FiscalQuarterTypeStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
}
