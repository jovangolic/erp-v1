package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.enumeration.IncomeStatementStatus;
import com.jovan.erp_v1.model.IncomeStatement;
import com.jovan.erp_v1.search_request.IncomeStatementSearchRequest;

import jakarta.persistence.criteria.Predicate;

public class IncomeStatementSpecification {

	public static Specification<IncomeStatement> fromRequest(IncomeStatementSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasPeriodStart(req.periodStart()))
				.and(hasPeriodStartBefore(req.periodStartBefore()))
				.and(hasPeriodStartAfter(req.periodStartAfter()))
				.and(hasPeriodEnd(req.periodEnd()))
				.and(hasPeriodEndBefore(req.periodEndBefore()))
				.and(hasPeriodEndAfter(req.periodEndAfter()))
				.and(hasPeriodRange(req.periodStart(), req.periodEnd()))
				.and(hasTotalRevenue(req.totalRevenue()))
				.and(hasTotalRevenueRange(req.totalRevenueMin(), req.totalRevenueMax()))
				.and(hasTotalExpenses(req.totalExpenses()))
				.and(hasTotalExpensesRange(req.totalExpensesMin(), req.totalExpensesMax()))
				.and(hasNetProfit(req.netProfit()))
				.and(hasNetProfitRange(req.netProfitMin(), req.netProfitMax()))
				.and(hasFiscalYearId(req.fiscalYearId()))
				.and(hasFiscalYearIdRange(req.fiscalYearIdFrom(), req.fiscalYearIdTo()))
				.and(hasYear(req.year()))
				.and(hasYearRange(req.yearFrom(), req.yearTo()))
				.and(hasStartDate(req.startDate()))
				.and(hasStartDateBefore(req.startDateBefore()))
				.and(hasStartDateAfter(req.startDateAfter()))
				.and(hasEndDate(req.endDate()))
				.and(hasEndDateBefore(req.endDateBefore()))
				.and(hasEndDateAfter(req.endDateAfter()))
				.and(hasFiscalYearDate(req.startDate(), req.endDate()))
				.and(hasFiscalYearStatus(req.yearStatus()))
				.and(hasFiscalQuarterStatus(req.quarterStatus()))
				.and(hasConfirmed(req.confirmed()))
				.and(hasIncomeStatus(req.status()));
	}
	
	public static Specification<IncomeStatement> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<IncomeStatement> hasIdRange(Long from, Long to){
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
	
	public static Specification<IncomeStatement> hasFiscalQuarterStatus(FiscalQuarterStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("fiscalYear").get("quarterStatus"), status);
	}
	
	public static Specification<IncomeStatement> hasFiscalYearStatus(FiscalYearStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("fiscalYear").get("yearStatus"), status);
	}
	
	public static Specification<IncomeStatement> hasFiscalYearDate(LocalDate start, LocalDate end){
		return(root, query, cb) -> {
			if(start == null && end == null) return null;
			Predicate predicate = cb.conjunction();
			if(start != null) {
				predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("fiscalYear").get("starDate"), start));
			}
			if(end != null) {
				predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("fiscalYear").get("endDate"), end));
			}
			return null;
		};
	}
	
	public static Specification<IncomeStatement> hasEndDateAfter(LocalDate aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("fiscalYear").get("endDate"), aft);
	}
	
	public static Specification<IncomeStatement> hasEndDateBefore(LocalDate bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("fiscalYear").get("endDate"), bef);
	}
	
	public static Specification<IncomeStatement> hasEndDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("fiscalYear").get("endDate"), ld);
	}
	
	public static Specification<IncomeStatement> hasStartDateAfter(LocalDate aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("fiscalYear").get("startDate"), aft);
	}
	
	public static Specification<IncomeStatement> hasStartDateBefore(LocalDate bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("fiscalYear").get("startDate"), bef);
	}
	
	public static Specification<IncomeStatement> hasStartDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("fiscalYear").get("startDate"), ld);
	}
	
	public static Specification<IncomeStatement> hasYearRange(Integer min, Integer max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("fiscalYear").get("year"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("fiscalYear").get("year"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("fiscalYear").get("year"), max);
			}
			return null;
		};
	}
	
	public static Specification<IncomeStatement> hasYear(Integer y){
		return(root, query, cb) -> y == null ? null : cb.equal(root.get("fiscalYear").get("year"), y);
	}
	
	public static Specification<IncomeStatement> hasFiscalYearIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("fiscalYear").get("id"), from, to);
			}
			else if(from  != null) {
				return cb.greaterThanOrEqualTo(root.get("fiscalYear").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("fiscalYear").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<IncomeStatement> hasFiscalYearId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("fiscalYear").get("id"), id);
	}
	
	public static Specification<IncomeStatement> hasIncomeStatus(IncomeStatementStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
	
	public static Specification<IncomeStatement> hasConfirmed(Boolean c){
		return(root, query, cb) -> c == null ? null : cb.equal(root.get("confirmed"), c);
	}
	
	public static Specification<IncomeStatement> hasNetProfitRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("netProfit"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("netProfit"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("netProfit"), max);
			}
			return null;
		};
	}
	
	public static Specification<IncomeStatement> hasNetProfit(BigDecimal bd){
		return(root, query, cb) -> bd == null ? null : cb.equal(root.get("netProfit"), bd);
	}
	
	public static Specification<IncomeStatement> hasTotalExpensesRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("totalExpenses"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("totalExpenses"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("totalExpenses"), max);
			}
			return null;
		};
	}
	
	public static Specification<IncomeStatement> hasTotalExpenses(BigDecimal bd){
		return(root, query, cb) -> bd == null ? null : cb.equal(root.get("totalExpenses"), bd);
	}
	
	public static Specification<IncomeStatement> hasTotalRevenueRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("totalRevenue"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("totalRevenue"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("totalRevenue"), max);
			}
			return null;
		};
	}
	
	public static Specification<IncomeStatement> hasTotalRevenue(BigDecimal bd){
		return(root, query, cb) -> bd == null ? null : cb.equal(root.get("totalRevenue"), bd);
	}
	
	public static Specification<IncomeStatement> hasPeriodRange(LocalDate from, LocalDate to){
		return(root, query, cb) -> {
			if(from == null && to == null) return null;
			Predicate predicate = cb.conjunction();
			if(from != null) {
				predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("periodStart"), from));
			}
			if(to != null) {
				predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("periodEnd"), to));
			}
			return predicate;
		};
	}
	
	public static Specification<IncomeStatement> hasPeriodEndAfter(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.greaterThan(root.get("periodEnd"), ld);
	}
	
	public static Specification<IncomeStatement> hasPeriodEndBefore(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.lessThan(root.get("periodEnd"), ld);
	}
	
	public static Specification<IncomeStatement> hasPeriodEnd(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("periodEnd"), ld);
	}
	
	public static Specification<IncomeStatement> hasPeriodStartAfter(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.greaterThan(root.get("periodStart"), ld);
	}
	
	public static Specification<IncomeStatement> hasPeriodStartBefore(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.lessThan(root.get("periodStart"), ld);
	}
	
	public static Specification<IncomeStatement> hasPeriodStart(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("periodStart"), ld);
	}
}
