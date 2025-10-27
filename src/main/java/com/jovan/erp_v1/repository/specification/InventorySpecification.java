package com.jovan.erp_v1.repository.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.enumeration.InventoryTypeStatus;
import com.jovan.erp_v1.model.Inventory;
import com.jovan.erp_v1.search_request.InventorySearchRequest;

import jakarta.persistence.criteria.Predicate;

public class InventorySpecification {

	public static Specification<Inventory> fromRequest(InventorySearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasStorageEmployeeId(req.storageEmployeeId()))
				.and(hasStorageEmployeeIdRange(req.storageEmployeeIdFrom(), req.storageEmployeeIdTo()))
				.and(hasEmployeeFullName(req.employeeFirstName(), req.employeeLastName()))
				.and(hasEmployeeEmail(req.employeeEmail()))
				.and(hasEmployeePhoneNumber(req.employeePhoneNumber()))
				.and(hasStorageForemanId(req.storageForemanId()))
				.and(hasStorageForemanIdRange(req.storageForemanIdFrom(), req.storageForemanIdTo()))
				.and(hasForemanFullName(req.foremanFirstName(), req.foremanLastName()))
				.and(hasForemanEmail(req.foremanEmail()))
				.and(hasForemanPhoneNumber(req.foremanPhoneNumber()))
				.and(hasDate(req.date()))
				.and(hasDateBefore(req.dateBefore()))
				.and(hasDateAfter(req.dateAfter()))
				.and(hasDateRange(req.dateBefore(), req.dateAfter()))
				.and(hasAligned(req.aligned()))
				.and(hasConfirmed(req.confirmed()))
				.and(hasTypeStatus(req.typeStatus()))
				.and(hasStatus(req.status()));
	}
	
	public static Specification<Inventory> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<Inventory> hasIdRange(Long from, Long to){
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
	
	public static Specification<Inventory> hasDateRange(LocalDate st, LocalDate end){
		return(root, query, cb) -> {
			if(st != null && end != null) {
				return cb.between(root.get("date"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("date"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("date"), end);
			}
			return null;
		};
	}
	
	public static Specification<Inventory> hasDateAfter(LocalDate aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("date"), aft);
	}
	
	public static Specification<Inventory> hasDateBefore(LocalDate bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("date"), bef);
	}
	
	public static Specification<Inventory> hasDate(LocalDate ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("date"), ld);
	}
	
	public static Specification<Inventory> hasForemanPhoneNumber(String phone){
		return(root, query, cb) ->{
			if(phone == null || phone.isBlank()) return null;
			return cb.like(cb.lower(root.get("storageForeman").get("phoneNumber")), "%" + phone.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Inventory> hasForemanEmail(String email){
		return(root, query, cb) -> {
			if(email == null || email.isBlank()) return null;
			return cb.like(cb.lower(root.get("storageForeman").get("email")), "%" + email.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Inventory> hasForemanFullName(String first, String last){
		return(root, query, cb) -> {
			if((first == null || first.isBlank()) && (last == null || last.isBlank())) return null;
			Predicate firstName = null;
			Predicate lastName = null;
			if(first != null && !first.isBlank()) {
				firstName = cb.like(cb.lower(root.get("storageForeman").get("firstName")), "%" + first.toLowerCase().trim() + "%");
			}
			if(last != null && !last.isBlank()) {
				lastName = cb.like(cb.lower(root.get("storageForeman").get("lastName")), "%" + last.toLowerCase().trim() + "%");
			}
			if(firstName != null && lastName != null) {
				cb.and(firstName, lastName);
			}
			else if(firstName != null) {
				return firstName;
			}
			else {
				return lastName;
			}
			return null;
		};
	}
	
	public static Specification<Inventory> hasStorageForemanIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("storageForeman").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("storageForeman").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("storageForeman").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Inventory> hasStorageForemanId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("storageForeman").get("id"), id);
	}
	
	public static Specification<Inventory> hasEmployeePhoneNumber(String phone){
		return(root, query, cb) ->{
			if(phone == null || phone.isBlank()) return null;
			return cb.like(cb.lower(root.get("storageEmployee").get("phoneNumber")), "%" + phone.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Inventory> hasEmployeeEmail(String email){
		return(root, query, cb) -> {
			if(email == null || email.isBlank()) return null;
			return cb.like(cb.lower(root.get("storageEmployee").get("email")), "%" + email.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Inventory> hasEmployeeFullName(String first, String last){
		return(root, query, cb) -> {
			if((first == null || first.isBlank()) && (last == null || last.isBlank())) return null;
			Predicate firstName = null;
			Predicate lastName = null;
			if(first != null && !first.isBlank()) {
				firstName = cb.like(cb.lower(root.get("storageEmployee").get("firstName")), "%" + first.toLowerCase().trim() + "%");
			}
			if(last != null && !last.isBlank()) {
				lastName = cb.like(cb.lower(root.get("storageEmployee").get("lastName")), "%" + last.toLowerCase().trim() + "%");
			}
			if(firstName != null && lastName != null) {
				cb.and(firstName, lastName);
			}
			else if(firstName != null) {
				return firstName;
			}
			else {
				return lastName;
			}
			return null;
		};
	}
	
	public static Specification<Inventory> hasStorageEmployeeIdRange(Long from , Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("storageEmployee").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("storageEmployee").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("storageEmployee").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Inventory> hasStorageEmployeeId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("storageEmployee").get("id"), id);
	}
	
	public static Specification<Inventory> hasStatus(InventoryStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
	
	public static Specification<Inventory> hasTypeStatus(InventoryTypeStatus type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("typeStatus"), type);
	}
	
	public static Specification<Inventory> hasConfirmed(Boolean conf){
		return(root, query, cb) -> conf == null ? null : cb.equal(root.get("confirmed"), conf);
	}
	
	public static Specification<Inventory> hasAligned(Boolean al){
		return(root, query, cb) -> al == null ? null : cb.equal(root.get("aligned"), al);
	}
}
