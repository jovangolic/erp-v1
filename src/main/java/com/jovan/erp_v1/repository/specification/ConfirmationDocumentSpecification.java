package com.jovan.erp_v1.repository.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.ConfirmationDocumentStatus;
import com.jovan.erp_v1.model.ConfirmationDocument;
import com.jovan.erp_v1.search_request.ConfirmationDocumentSearchRequest;

import jakarta.persistence.criteria.Predicate;

public class ConfirmationDocumentSpecification {
	
	public static Specification<ConfirmationDocument> fromRequest(ConfirmationDocumentSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasCreatedAt(req.createdAt()))
				.and(hasCreatedAtBefore(req.createdAtBefore()))
				.and(hasCreatedAtAfter(req.createdAtAfter()))
				.and(hasCreatedAtRange(req.createdAtStart(), req.createdAtEnd()))
				.and(hasCreatedById(req.createdById()))
				.and(hasCreatedByIdRange(req.createdByIdFrom(), req.createdByIdTo()))
				.and(hasFullName(req.firstName(), req.lastName()))
				.and(hasShiftId(req.shiftId()))
				.and(hasShiftIdRange(req.shiftIdFrom(), req.shiftIdTo()))
				.and(shiftStartTime(req.startTime()))
				.and(shiftStartTimeBefore(req.startTimeBefore()))
				.and(shiftStartTimeAfter(req.startTimeAfter()))
				.and(shiftEndTime(req.endTime()))
				.and(shiftEndTimeBefore(req.endTimeBefore()))
				.and(shiftEndTimeAfter(req.endTimeAfter()))
				.and(hasShiftTimeRange(req.startTime(), req.endTime()))
				.and(hasShiftSupervisorId(req.shiftSupervisorId()))
				.and(hasShiftSupervisorIdRange(req.shiftSupervisorIdFrom(), req.shiftSupervisorIdTo()))
				.and(hasConfirmationDocumentStatus(req.status()))
				.and(hasStatus(req.status()))
				.and(hasConfirmed(req.confirmed()));
	}
	
	public static Specification<ConfirmationDocument> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<ConfirmationDocument> hasIdRange(Long from, Long to){
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
	
	public static Specification<ConfirmationDocument> hasConfirmationDocumentStatus(ConfirmationDocumentStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
	
	public static Specification<ConfirmationDocument> hasShiftSupervisorIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("shift").get("shiftSupervisor").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("shift").get("shiftSupervisor").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("shift").get("shiftSupervisor").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<ConfirmationDocument> hasShiftSupervisorId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("shift").get("shiftSupervisor").get("id"), id);
	}
	
	public static Specification<ConfirmationDocument> hasShiftTimeRange(LocalDateTime str, LocalDateTime end){
		return(root, query, cb) -> {
			if(str != null && end != null) {
				return cb.between(root.get("shift").get("endTime"), str, end);
			}
			else if(str != null) {
				return cb.greaterThanOrEqualTo(root.get("shift").get("endTime"), str);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("shift").get("endTime"), end);
			}
			return null;
		};
	}
	
	public static Specification<ConfirmationDocument> shiftEndTimeAfter(LocalDateTime aft){
		return(root,query, cb) -> aft == null ? null : cb.greaterThan(root.get("shift").get("endTime"), aft);
	}
	
	public static Specification<ConfirmationDocument> shiftEndTimeBefore(LocalDateTime bef){
		return(root, query, cb) -> bef == null ? null : cb.equal(root.get("shift").get("endTime"), bef);
	}
	
	public static Specification<ConfirmationDocument> shiftEndTime(LocalDateTime end){
		return(root, query, cb) -> end == null ? null : cb.equal(root.get("shift").get("endTime"), end);
	}
	
	public static Specification<ConfirmationDocument> shiftStartTimeAfter(LocalDateTime aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("shift").get("startTime"), aft);
	}
	
	public static Specification<ConfirmationDocument> shiftStartTimeBefore(LocalDateTime bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("shift").get("startTime"), bef);
	}
	
	public static Specification<ConfirmationDocument> shiftStartTime(LocalDateTime str){
		return(root, query, cb) -> str == null ? null : cb.equal(root.get("shift").get("startTime"), str);
	}
	
	public static Specification<ConfirmationDocument> hasShiftIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("shift").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("shift").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("shift").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<ConfirmationDocument> hasShiftId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("shift").get("id"), id);
	}
	
	public static Specification<ConfirmationDocument> hasFullName(String fullName) {
	    return (root, query, cb) -> {
	        if (fullName == null || fullName.isBlank()) {
	            return null;
	        }
	        // Pretvori u mala slova i podeli po razmaku
	        String[] parts = fullName.trim().toLowerCase().split("\\s+");
	        // Ako je samo jedno ime ili prezime
	        if (parts.length == 1) {
	            String namePart = "%" + parts[0] + "%";
	            return cb.or(
	                cb.like(cb.lower(root.get("createdBy").get("firstName")), namePart),
	                cb.like(cb.lower(root.get("createdBy").get("lastName")), namePart)
	            );
	        }
	        // Ako je uneto i ime i prezime (ili više delova)
	        String firstName = "%" + parts[0] + "%";
	        String lastName = "%" + parts[parts.length - 1] + "%";

	        return cb.and(
	            cb.like(cb.lower(root.get("createdBy").get("firstName")), firstName),
	            cb.like(cb.lower(root.get("createdBy").get("lastName")), lastName)
	        );
	    };
	}
	
	public static Specification<ConfirmationDocument> hasFullName(String first, String last){
		return(root, query, cb) -> {
			if((first == null || first.isBlank()) && (last == null || last.isBlank())) return null;
			// kreiraju se pojedinacni predikati (uslovi)
			Predicate firstNamePredicate = null;
			Predicate lastNamePredicate = null;
			if(first != null && !first.isBlank()) {
				firstNamePredicate = cb.like(cb.lower(root.get("createdBy").get("firstName")), "%" + first.toLowerCase().trim() + "%");
			}
			if(last != null && !last.isBlank()) {
				lastNamePredicate = cb.like(cb.lower(root.get("createdBy").get("lastName")), "%" + last.toLowerCase().trim() + "%");
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
	
	public static Specification<ConfirmationDocument> hasCreatedByIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("createdById").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("createdById").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("createdById").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<ConfirmationDocument> hasCreatedById(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("createdById").get("id"), id);
	}
	
	public static Specification<ConfirmationDocument> hasCreatedAtRange(LocalDateTime str, LocalDateTime end){
		return(root, query, cb) -> {
			if(str != null && end != null) {
				return cb.between(root.get("createdAt"), str, end);
			}
			else if(str != null) {
				return cb.greaterThanOrEqualTo(root.get("createdAt"), str);
			}
			else if(end != null) {
				return cb.lessThan(root.get("createdAt"), end);
			}
			return null;
		};
	}
	
	public static Specification<ConfirmationDocument> hasCreatedAtAfter(LocalDateTime aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("createdAt"), aft);
	}
	
	public static Specification<ConfirmationDocument> hasCreatedAtBefore(LocalDateTime bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("createdAt"), bef);
	}
	
	public static Specification<ConfirmationDocument> hasCreatedAt(LocalDateTime crd){
		return(root, query, cb) -> crd == null ? null : cb.equal(root.get("createdAt"), crd);
	}
	
	public static Specification<ConfirmationDocument> hasStatus(ConfirmationDocumentStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
	
	public static Specification<ConfirmationDocument> hasConfirmed(Boolean conf){
		return(root, query, cb) -> conf == null ? null : cb.equal(root.get("confirmed"), conf);
	}
}
