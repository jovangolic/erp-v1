package com.jovan.erp_v1.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.DriverStatus;
import com.jovan.erp_v1.model.Driver;
import com.jovan.erp_v1.search_request.DriverSearchRequest;

import jakarta.persistence.criteria.Predicate;

public class DriverSpecification {

	public static Specification<Driver> fromRequest(DriverSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasFirstName(req.firstName()))
				.and(hasLastName(req.lastName()))
				.and(hasFullName(req.firstName(), req.lastName()))
				.and(hasFullNameSecond(req.firstName(), req.lastName()))
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
	
	public static Specification<Driver> hasFullName(String first, String last){
		return(root, query, cb) -> {
			if((first == null || first.isBlank()) && (last == null || last.isBlank())) return null;
			// kreiraju se pojedinacni predikati (uslovi)
			Predicate firstNamePredicate = null;
			Predicate lastNamePredicate = null;
			if(first != null && !first.isBlank()) {
				firstNamePredicate = cb.like(cb.lower(root.get("firstName")), "%" + first.toLowerCase() + "%");
			}
			if(last != null && !last.isBlank()) {
				lastNamePredicate = cb.like(cb.lower(root.get("lastName")), "%" + last.toLowerCase() + "%");
			}
			// Kombinacija oba uslova – ako postoje oba, koristi se AND
			if(firstNamePredicate != null && lastNamePredicate != null) {
				cb.and(firstNamePredicate, lastNamePredicate);
			}
			else if(firstNamePredicate != null) {
				return firstNamePredicate;
			}
			else {
				return lastNamePredicate;
			}
			return null;
		}; 
	}
	
	public static Specification<Driver> hasFullNameSecond(String first, String last){
		return(root, query, cb) -> {
			if((first == null || first.isBlank()) && (last == null || last.isBlank())) return null;
			List<Predicate> predicates = new ArrayList<Predicate>();
			if(first != null && !first.isBlank()) {
				predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + first.toLowerCase().trim() + "%"));
			}
			if(last != null && !last.isBlank()) {
				predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + last.toLowerCase().trim() + "%"));
			}
			return cb.and(predicates.toArray(new Predicate[0]));
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
