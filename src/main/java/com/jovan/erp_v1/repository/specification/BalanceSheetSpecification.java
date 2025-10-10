package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.model.BalanceSheet;
import com.jovan.erp_v1.search_request.BalanceSheetGeneralSearchRequest;

public class BalanceSheetSpecification {

	public static Specification<BalanceSheet> hasFiscalYearId(Long fiscalYearId){
		return (root,query, cb) -> 
		fiscalYearId == null ? null : cb.equal(root.get("fiscalYearId").get("id"),fiscalYearId);
	}
	
	public static Specification<BalanceSheet> hasDateBetween(LocalDate start, LocalDate end){
		return (root,query, cb) -> {
			if(start != null && end != null) {
				return cb.between(root.get("date"),start,end);
			}
			else if(start != null) {
				return cb.greaterThanOrEqualTo(root.get("date"), start);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("date"), end);
			}
			else {
				return null;
			}
		};
	}
	
	public static Specification<BalanceSheet> assetsGreaterThan(BigDecimal amount) {
        return (root, query, cb) ->
            amount == null ? null : cb.greaterThan(root.get("totalAssets"), amount);
    }

    public static Specification<BalanceSheet> isSolvent() {
        return (root, query, cb) ->
            cb.greaterThanOrEqualTo(root.get("totalAssets"), root.get("totalLiabilities"));
    }
    
    public static Specification<BalanceSheet> equityGreaterThan(BigDecimal amount) {
        return (root, query, cb) ->
            amount == null ? null : cb.greaterThan(root.get("totalEquity"), amount);
    }

    public static Specification<BalanceSheet> liabilitiesGreaterThan(BigDecimal amount) {
        return (root, query, cb) ->
            amount == null ? null : cb.greaterThan(root.get("totalLiabilities"), amount);
    }
    
    public static Specification<BalanceSheet> fromRequest(BalanceSheetGeneralSearchRequest req){
    	return Specification.where(hasId(req.id()))
    			.and(hasRangeId(req.idFrom(), req.idTo()))
    			.and(hasFiscalYearId(req.fiscalYearId()))
    			.and(hasFiscalYearRangeId(req.fiscalYearIdFrom(), req.fiscalYearIdTo()))
    			.and(hasYear(req.year()))
    			.and(hasYearRange(req.yearFrom(), req.yearTo()))
    			.and(hasYearStatus(req.yearStatus()))
    			.and(hasQuarterStatus(req.quarterStatus()))
    			.and(hasConfirmed(req.confirmed()))
    			.and(hasDateBetween(req.dateFrom(),req.dateTo()))
    			.and(hasDate(req.date()))
    			.and(hasDateBefore(req.dateBefore()))
    			.and(hasDateAfter(req.dateAfter()))
    			.and(hasTotalAssets(req.totalAssets()))
    			.and(hasTotalAssetsRange(req.totalAssetsFrom(), req.totalAssetsTo()))
    			.and(hasTotalAssetsLess(req.totalAssetsLessThan()))
    			.and(hasTotalAssetsGreater(req.totalAssetsGreater()))
    			.and(hasTotalLiabilities(req.totalLiabilities()))
    			.and(hasTotalLiabilitiesRange(req.totalLiabilitiesFrom(),req.totalLiabilitiesTo()))
    			.and(hasTotalLiabilitiesLess(req.totalLiabilitiesLess()))
    			.and(hasTotalLiabilitiesGreater(req.totalLiabilitiesGreater()))
    			.and(hasTotalEquity(req.totalEquity()))
    			.and(hasTotalEquityRange(req.totalEquityFrom(), req.totalEquityTo()))
    			.and(hasTotalEquityLess(req.totalEquityLess()))
    			.and(hasTotalEquityGreater(req.totalEquityGreater()))
    			.and(hasStartDateBefore(req.startDateBefore()))
    			.and(hasStartDateAfter(req.startDateAfter()))
    			.and(hasStartDate(req.startDate()))
    			.and(hasEndDateBefore(req.endDateBefore()))
    			.and(hasEndDateAfter(req.endDateAfter()))
    			.and(hasEndDate(req.endDate()))
    			.and(hasFiscalDateRange(req.startDate(), req.endDate()));
    }
    
    public static Specification<BalanceSheet> hasId(Long id){
    	return(root,query,cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }
    
    public static Specification<BalanceSheet> hasRangeId(Long from, Long to){
    	return(root,query,cb) -> {
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
    
    public static Specification<BalanceSheet> hasFiscalDateRange(LocalDate st, LocalDate end){
    	return(root,query,cb) -> {
    		if(st != null && end != null) {
    			return cb.between(root.get("fiscalYear").get("startDate"), st, end);
    		}
    		else if(st != null) {
    			return cb.greaterThanOrEqualTo(root.get("fiscalYear").get("startDate"), st);
    		}
    		else if(end != null) {
    			return cb.lessThanOrEqualTo(root.get("fiscalYear").get("endDate"), end);
    		}
    		return null;
    	};
    }
    
    public static Specification<BalanceSheet> hasEndDateBefore(LocalDate db){
    	return(root, query,cb) -> db == null ? null : cb.lessThan(root.get("fiscalYear").get("endDate"), db);
    }
    
    public static Specification<BalanceSheet> hasEndDateAfter(LocalDate da){
    	return(root, query,cb) -> da == null ? null : cb.greaterThan(root.get("fiscalYear").get("endDate"), da);
    }
    
    public static Specification<BalanceSheet> hasEndDate(LocalDate d){
    	return(root,query,cb) -> d == null ? null : cb.equal(root.get("fiscalYear").get("endDate"), d);
    }
    
    public static Specification<BalanceSheet> hasStartDateBefore(LocalDate db){
    	return(root, query,cb) -> db == null ? null : cb.lessThan(root.get("fiscalYear").get("startDate"), db);
    }
    
    public static Specification<BalanceSheet> hasStartDateAfter(LocalDate da){
    	return(root, query,cb) -> da == null ? null : cb.greaterThan(root.get("fiscalYear").get("startDate"), da);
    }
    
    public static Specification<BalanceSheet> hasStartDate(LocalDate d){
    	return(root,query,cb) -> d == null ? null : cb.equal(root.get("fiscalYear").get("startDate"), d);
    }
    
    public static Specification<BalanceSheet> hasTotalEquityLess(BigDecimal ls){
    	return(root,query,cb) -> ls == null ? null : cb.lessThan(root.get("totalEquity"), ls);
    }
    
    public static Specification<BalanceSheet> hasTotalEquityGreater(BigDecimal gt){
    	return(root,query,cb) -> gt == null ? null : cb.greaterThan(root.get("totalEquity"), gt);
    }
    
    public static Specification<BalanceSheet> hasTotalEquity(BigDecimal eq){
    	return(root,query,cb) -> eq == null ? null : cb.equal(root.get("totalEquity"), eq);
    }
    
    public static Specification<BalanceSheet> hasTotalEquityRange(BigDecimal from, BigDecimal to){
    	return(root,query,cb) -> {
    		if(from != null && to != null) {
    			return cb.between(root.get("totalEquity"), from, to);
    		}
    		else if(from != null) {
    			return cb.greaterThanOrEqualTo(root.get("totalEquity"), from);
    		}
    		else if(to != null) {
    			return cb.lessThanOrEqualTo(root.get("totalEquity"), to);
    		}
    		return null;
    	};
    }
    
    public static Specification<BalanceSheet> hasTotalLiabilitiesGreater(BigDecimal gt){
    	return(root,query,cb) -> gt == null ? null : cb.greaterThan(root.get("totalLiabilities"), gt);
    }
    
    public static Specification<BalanceSheet> hasTotalLiabilitiesLess(BigDecimal ls){
    	return(root,query,cb) -> ls == null ? null : cb.lessThan(root.get("totalLiabilities"), ls);
    }
    
    public static Specification<BalanceSheet> hasTotalLiabilities(BigDecimal t){
    	return(root,query,cb) -> t == null ? null : cb.equal(root.get("totalLiabilities"), t);
    }
    
    public static Specification<BalanceSheet> hasTotalLiabilitiesRange(BigDecimal from, BigDecimal to){
    	return(root,query,cb) -> {
    		if(from != null && to != null) {
    			return cb.between(root.get("totalLiabilities"), from, to);
    		}
    		else if(from != null) {
    			return cb.greaterThanOrEqualTo(root.get("totalLiabilities"), from);
    		}
    		else if(to != null) {
    			return cb.lessThanOrEqualTo(root.get("totalLiabilities"), to);
    		}
    		return null;
    	};
    }
    
    public static Specification<BalanceSheet> hasTotalAssetsGreater(BigDecimal gt){
    	return(root,query,cb) -> gt == null ? null : cb.greaterThan(root.get("totalAssets"), gt);
    }
    
    public static Specification<BalanceSheet> hasTotalAssetsLess(BigDecimal ls){
    	return(root,query,cb) -> ls == null ? null : cb.lessThan(root.get("totalAssets"), ls);
    }
    
    public static Specification<BalanceSheet> hasTotalAssetsRange(BigDecimal from, BigDecimal to){
    	return(root,query,cb) -> {
    		if(from != null && to != null) {
    			return cb.between(root.get("totalAssets"), from, to);
    		}
    		else if(from != null) {
    			return cb.greaterThanOrEqualTo(root.get("totalAssets"), from);
    		}
    		else if(to != null) {
    			return cb.lessThanOrEqualTo(root.get("totalAssets"), to);
    		}
    		return null;
    	};
    }
    
    public static Specification<BalanceSheet> hasTotalAssets(BigDecimal totalAssets){
    	return(root,query,cb) -> totalAssets == null ? null : cb.equal(root.get("totalAssets"), totalAssets);
    }
    
    public static Specification<BalanceSheet> hasDateBefore(LocalDate db){
    	return(root, query,cb) -> db == null ? null : cb.lessThan(root.get("date"), db);
    }
    
    public static Specification<BalanceSheet> hasDateAfter(LocalDate da){
    	return(root, query,cb) -> da == null ? null : cb.greaterThan(root.get("date"), da);
    }
    
    public static Specification<BalanceSheet> hasDate(LocalDate d){
    	return(root,query,cb) -> d == null ? null : cb.equal(root.get("date"), d);
    }
    
    public static Specification<BalanceSheet> hasConfirmed(Boolean confirmed){
		return(root,query,cb) -> confirmed == null ? null : cb.equal(root.get("confirmed"), confirmed);
	}
    
    public static Specification<BalanceSheet> hasQuarterStatus(FiscalQuarterStatus status){
    	return(root,query,cb) -> status == null ? null : cb.equal(root.get("fiscalYear").get("quarterStatus"), status);
    }
    
    public static Specification<BalanceSheet> hasYearStatus(FiscalYearStatus status){
    	return(root,query,cb) -> status == null ? null : cb.equal(root.get("fiscalYear").get("yearStatus"), status);
    }
    
    public static Specification<BalanceSheet> hasYearRange(Integer from, Integer to){
    	return(root,query,cb) -> {
	    	if(from != null && to != null) {
				return cb.between(root.get("fiscalYear").get("year"), from, to);
			}
			else if(from != null) {
				cb.greaterThanOrEqualTo(root.get("fiscalYear").get("year"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("fiscalYear").get("year"), to);
			}
			return null;
    	};
    }
    
    public static Specification<BalanceSheet> hasYear(Integer year){
    	return(root,query,cb) -> year == null ? null : cb.equal(root.get("fiscalYear").get("year"), year);
    }
    
    public static Specification<BalanceSheet> hasFiscalYearRangeId(Long from, Long to){
    	return(root,query,cb) -> {
	    	if(from != null && to != null) {
				return cb.between(root.get("fiscalYear").get("id"), from, to);
			}
			else if(from != null) {
				cb.greaterThanOrEqualTo(root.get("fiscalYear").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("fiscalYear").get("id"), to);
			}
			return null;
    	};
    }
}
