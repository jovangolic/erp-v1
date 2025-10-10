package com.jovan.erp_v1.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.BuyerStatus;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.search_request.BuyerSearchRequest;

public class BuyerSpecification {

	public static Specification<Buyer> fromRequest(BuyerSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasCompanyName(req.companyName()))
				.and(hasPib(req.pib()))
				.and(hasAddress(req.address()))
				.and(hasContactPerson(req.contactPerson()))
				.and(hasEmail(req.email()))
				.and(hasPhoneNumber(req.phoneNumber()))
				.and(hasStatus(req.status()))
				.and(hasConfirmed(req.confirmed()));
	}
	
	public static Specification<Buyer> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<Buyer> hasIdRange(Long from, Long to){
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
	
	public static Specification<Buyer> hasPhoneNumber(String num){
		return(root, query, cb) -> num == null ? null : 
			cb.like(cb.lower(root.get("phoneNumber")), "%" + num.toLowerCase() + "%");
	}
	
	public static Specification<Buyer> hasEmail(String em){
		return(root, query, cb) -> em == null ? null :
			cb.like(cb.lower(root.get("email")), "%" +em.toLowerCase() + "%");
	}
	
	public static Specification<Buyer> hasContactPerson(String cnt){
		return(root, query, cb) -> cnt == null ? null : 
			cb.like(cb.lower(root.get("contactPerson")), "%" + cnt.toLowerCase() + "%");
	}
	
	public static Specification<Buyer> hasAddress(String adr){
		return(root, query, cb) -> adr == null ? null : 
			cb.like(cb.lower(root.get("address")), "%" + adr.toLowerCase() + "%");
	}
	
	public static Specification<Buyer> hasPib(String pib){
		return(root, query, cb) -> pib == null ? null :
			cb.like(cb.lower(root.get("pib")), "%" + pib.toLowerCase() + "%");
	}
	
	public static Specification<Buyer> hasCompanyName(String name){
		return(root, query, cb) -> name == null ? null :
			cb.like(cb.lower(root.get("companyName")), "%" + name.toLowerCase() + "%");
	}
	
	public static Specification<Buyer> hasConfirmed(Boolean c){
		return(root, query, cb) -> c == null ? null : cb.equal(root.get("confirmed"), c);
	}
	
	public static Specification<Buyer> hasStatus(BuyerStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
}
