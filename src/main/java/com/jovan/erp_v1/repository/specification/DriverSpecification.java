package com.jovan.erp_v1.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.DriverStatus;
import com.jovan.erp_v1.model.Driver;
import com.jovan.erp_v1.search_request.DriverSearchRequest;

public class DriverSpecification {

	public static Specification<Driver> fromRequest(DriverSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasFirstName(req.firstName()))
				.and(hasLastName(req.lastName()))
				.and(hasPhone(req.phone()))
				.and(hasStatus(req.status()))
				.and(hasConfirmed(req.confirmed()));
	}
	
	public static Specification<Driver> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<Driver> hasIdRange(Long from, Long to){
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
	
	public static Specification<Driver> hasPhone(String phone){
		return(root, query, cb) -> phone == null ? null : cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%");
	}
	
	public static Specification<Driver> hasLastName(String name){
		return(root, query, cb) -> name == null ? null : cb.like(cb.lower(root.get("lastName")), "%" + name.toLowerCase() + "%");
	}
	
	public static Specification<Driver> hasFirstName(String name){
		return(root, query, cb) -> name == null ? null : cb.like(cb.lower(root.get("firstName")), "%" + name.toLowerCase() + "%");
	}
	
	public static Specification<Driver> hasConfirmed(Boolean conf){
		return(root, query, cb) -> conf == null ? null : cb.equal(root.get("confirmed"), conf);
	}
	
	public static Specification<Driver> hasStatus(DriverStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
}
