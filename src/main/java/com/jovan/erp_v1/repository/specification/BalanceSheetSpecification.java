package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.model.BalanceSheet;

public class BalanceSheetSpecification {

	public static Specification<BalanceSheet> hasFiscalYearId(Long fiscalYearId){
		return (root,query, cb) -> 
		fiscalYearId == null ? null : cb.equal(root.get("fiscalYearId").get("id"),fiscalYearId);
	}
	
	public static Specification<BalanceSheet> dateBetween(LocalDate start, LocalDate end){
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
}
