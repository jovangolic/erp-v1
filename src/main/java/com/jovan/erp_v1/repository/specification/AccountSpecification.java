package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.AccountStatus;
import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.search_request.AccountSearchRequest;

public class AccountSpecification {

	public static Specification<Account> fromRequest(AccountSearchRequest req){
		return Specification.where(hasId(req.id())
				.and(hasAccountIdRange(req.accountIdFrom(), req.accountIdTo()))
				.and(hasAccountNumber(req.accountNumber()))
				.and(hasAccountName(req.accountName()))
				.and(hasBalance(req.balance()))
				.and(hasBalanceRange(req.balanceFrom(), req.balanceTo()))
				.and(hasAccountStatus(req.status()))
				.and(hasConfirmed(req.confirmed()))
				.and(hasAccountType(req.type())));
	}
	
	public static Specification<Account> hasId(Long accountId){
		return(root,query,cb) -> accountId == null ? null : cb.equal(root.get("id"), accountId);
	}
	
	public static Specification<Account> hasAccountIdRange(Long idFrom, Long idTo){
		return(root,query,cb) -> {
			if(idFrom == null || idTo == null) return null;
			return(cb.between(root.get("id"),idFrom, idTo));
		};
	}
	
	public static Specification<Account> hasAccountStatus(AccountStatus status){
		return(root,query,cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
	
	public static Specification<Account> hasConfirmed(Boolean confirmed){
		return(root,query,cb) -> confirmed == null ? null : cb.equal(root.get("confirmed"), confirmed);
	}
	
	public static Specification<Account> hasAccountType(AccountType type){
		return(root,query,cb) -> type == null ? null : cb.equal(root.get("type"), type);
	}
	
	public static Specification<Account> hasAccountNumber(String accNum){
		return(root, query,cb) -> accNum == null ? null 
				: cb.like(cb.lower(root.get("accountNumber")), "%" + accNum.toLowerCase() + "%");
	}
	
	public static Specification<Account> hasAccountName(String accName){
		return(root, query,cb) -> accName == null ? null 
				: cb.like(cb.lower(root.get("accountName")), "%" + accName.toLowerCase() + "%");
	}
	
	public static Specification<Account> hasBalance(BigDecimal balance){
		return(root,query,cb) -> balance == null ? null : cb.equal(root.get("balance"), balance);
	}
	
	public static Specification<Account> hasBalanceRange(BigDecimal from, BigDecimal to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("balance"), from, to);
			}
			if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("balance"), from);
			}
			if(to != null) {
				return cb.lessThanOrEqualTo(root.get("balance"), to);
			}
			return null;
		};
	}
}
