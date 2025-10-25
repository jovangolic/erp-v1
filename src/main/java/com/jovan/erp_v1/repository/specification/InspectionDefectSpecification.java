package com.jovan.erp_v1.repository.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.InspectionDefectStatus;
import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.enumeration.SeverityLevel;
import com.jovan.erp_v1.model.InspectionDefect;
import com.jovan.erp_v1.search_request.InspectionDefectSearchRequest;

import jakarta.persistence.criteria.Predicate;

public class InspectionDefectSpecification {

	public static Specification<InspectionDefect> formRequest(InspectionDefectSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasQuantityAffected(req.quantityAffected()))
				.and(hasQuantityAffectedRange(req.quantityAffectedMin(), req.quantityAffectedMax()))
				.and(hasInspectionId(req.inspectionId()))
				.and(hasInspectionIdRange(req.inspectionIdFrom(), req.inspectionIdTo()))
				.and(hasInspectionCode(req.inspectionCode()))
				.and(hasInspectionType(req.type()))
				.and(hasInspectionDate(req.inspectionDate()))
				.and(hasInspectionDateBefore(req.inspectionDateBefore()))
				.and(hasInspectionDateAfter(req.inspectionDateAfter()))
				.and(hasInspectionDateRange(req.inspectionDateBefore(),req.inspectionDateAfter()))
				.and(hasBatchId(req.batchId()))
				.and(hasBatchIdRange(req.batchIdFrom(), req.batchIdTo()))
				.and(hasProductId(req.productId()))
				.and(hasProductIdRange(req.productIdFrom(), req.productIdTo()))
				.and(hasInspectorId(req.inspectorId()))
				.and(hasInspectorIdRange(req.inspectorIdFrom(), req.inspectorIdTo()))
				.and(hasInspectorFullName(req.firstName(), req.lastName()))
				.and(hasQuantityInspected(req.quantityInspected()))
				.and(hasQuantityInspectedRange(req.quantityInspectedMin(), req.quantityInspectedMax()))
				.and(hasQuantityAccepted(req.quantityAccepted()))
				.and(hasQuantityAcceptedRange(req.quantityAcceptedMin(), req.quantityAcceptedMax()))
				.and(hasQuantityRejected(req.quantityRejected()))
				.and(hasQuantityRejectedRange(req.quantityRejectedMin(), req.quantityRejectedMax()))
				.and(hasInspectionNotes(req.notes()))
				.and(hasInspectionResult(req.result()))
				.and(hasQualityCheckId(req.qualityCheckId()))
				.and(hasQualityCheckIdRange(req.qualityCheckIdFrom(), req.qualityCheckIdTo()))
				.and(hasDefectId(req.defectId()))
				.and(hasDefectIdRange(req.defectIdFrom(), req.defectIdTo()))
				.and(hasDefectCode(req.defectCode()))
				.and(hasDefectName(req.defectName()))
				.and(hasDefectDescription(req.defectDescription()))
				.and(hasDefectSeverityLevel(req.severity()))
				.and(hasDefectCreatedDate(req.defectCreatedDate()))
				.and(hasDefectCreatedDateBefore(req.defectCreatedDateBefore()))
				.and(hasDefectCreatedDateAfter(req.defectCreatedDateAfter()))
				.and(hasDefectCreatedDateRange(req.defectCreatedDateBefore(), req.defectCreatedDateAfter()))
				.and(hasConfirmed(req.confirmed()))
				.and(hasInspectionDefectStatus(req.status()));
	}
	
	public static Specification<InspectionDefect> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<InspectionDefect> hasIdRange(Long from, Long to){
		return(root, query, cb) ->{
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
	
	public static Specification<InspectionDefect> hasDefectCreatedDateRange(LocalDateTime st, LocalDateTime end){
		return(root, query, cb) -> {
			if(st != null && end != null) {
				return cb.between(root.get("defect").get("createdDate"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("defect").get("createdDate"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("defect").get("createdDate"), end);
			}
			return null;
		};
	}
	
	public static Specification<InspectionDefect> hasDefectCreatedDateAfter(LocalDateTime aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("defect").get("createdDate"), aft);
	}
	
	public static Specification<InspectionDefect> hasDefectCreatedDateBefore(LocalDateTime bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("defect").get("createdDate"), bef);
	}
	
	public static Specification<InspectionDefect> hasDefectCreatedDate(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("defect").get("createdDate"), ld);
	}
	
	public static Specification<InspectionDefect> hasDefectSeverityLevel(SeverityLevel level){
		return(root, query, cb) -> level == null ? null : cb.equal(root.get("defect").get("severity"), level);
	}
	
	public static Specification<InspectionDefect> hasDefectDescription(String dsc){
		return(root, query, cb) -> {
			if(dsc == null || dsc.isBlank()) return null;
			return cb.like(cb.lower(root.get("defect").get("description")), "%" + dsc.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<InspectionDefect> hasDefectName(String name){
		return(root, query, cb) -> {
			if(name == null || name.isBlank()) return null;
			return cb.like(cb.lower(root.get("defect").get("name")), "%" + name.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<InspectionDefect> hasDefectCode(String code){
		return(root, query, cb) -> {
			if(code == null || code.isBlank()) return null;
			return cb.like(cb.lower(root.get("defect").get("code")), "%" + code.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<InspectionDefect> hasDefectIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("defect").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("defect").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("defect").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<InspectionDefect> hasDefectId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("defect").get("id"), id);
	}
	
	public static Specification<InspectionDefect> hasQualityCheckIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("inspection").get("qualityCheck").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("inspection").get("qualityCheck").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("inspection").get("qualityCheck").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<InspectionDefect> hasQualityCheckId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("inspection").get("qualityCheck").get("id"), id);
	}
	
	public static Specification<InspectionDefect> hasInspectionResult(InspectionResult rez){
		return(root, query, cb) -> rez == null ? null : cb.equal(root.get("inspection").get("result"), rez);
	}
	
	public static Specification<InspectionDefect> hasInspectionNotes(String notes){
		return(root, query, cb) -> {
			if(notes == null || notes.isBlank()) return null;
			return cb.like(cb.lower(root.get("inspection").get("notes")), "%" + notes.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<InspectionDefect> hasQuantityRejectedRange(Integer min, Integer max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("inspection").get("quantityRejected"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("inspection").get("quantityRejected"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("inspection").get("quantityRejected"), max);
			}
			return null;
		};
	}
	
	public static Specification<InspectionDefect> hasQuantityRejected(Integer q){
		return(root, query, cb) -> q == null ? null : cb.equal(root.get("inspection").get("quantityRejected"), q);
	}
	
	public static Specification<InspectionDefect> hasQuantityAcceptedRange(Integer min, Integer max){
		return(root, query, cb) -> {
			if(min != null  && max != null) {
				return cb.between(root.get("inspection").get("quantityAccepted"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("inspection").get("quantityAccepted"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("inspection").get("quantityAccepted"), min);
			}
			return null;
		};
	}
	
	public static Specification<InspectionDefect> hasQuantityAccepted(Integer q){
		return(root, query, cb) -> q == null ? null : cb.equal(root.get("inspection").get("quantityAccepted"), q);
	}
	
	public static Specification<InspectionDefect> hasQuantityInspectedRange(Integer min, Integer max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("inspection").get("quantityInspected"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("inspection").get("quantityInspected"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("inspection").get("quantityInspected"), max);
			}
			return null;
		};
	}
	
	public static Specification<InspectionDefect> hasQuantityInspected(Integer q){
		return(root, query, cb) -> q == null ? null : cb.equal(root.get("inspection").get("quantityInspected"), q);
	}
	
	public static Specification<InspectionDefect> hasInspectorFullName(String first, String last){
		return(root, query, cb) -> {
			if((first == null || first.isBlank()) && (last == null || last.isBlank())) return null;
			Predicate firstName = null;
			Predicate lastName = null;
			if(first != null && !first.isBlank()) {
				firstName = cb.and(firstName, cb.like(cb.lower(root.get("inspection").get("inspector").get("firstName")), "%" + first.toLowerCase().trim() + "%"));
			}
			if(last != null && !last.isBlank()) {
				lastName = cb.and(lastName, cb.like(cb.lower(root.get("inspection").get("inspector").get("lastName")), "%" + last.toLowerCase().trim() + "%"));
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
	
	public static Specification<InspectionDefect> hasInspectorIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("inspection").get("inspector").get("id"), from , to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("inspection").get("inspector").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("inspection").get("inspector").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<InspectionDefect> hasInspectorId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("inspection").get("inspector").get("id"), id);
	}
	
	public static Specification<InspectionDefect> hasProductIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("inspection").get("product").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("inspection").get("product").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("inspection").get("product").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<InspectionDefect> hasProductId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("inspection").get("product").get("id"), id);
	}
	
	public static Specification<InspectionDefect> hasBatchIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("inspection").get("batch").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("inspection").get("batch").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("inspection").get("batch").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<InspectionDefect> hasBatchId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("inspection").get("batch").get("id"), id);
	}
	
	public static Specification<InspectionDefect> hasInspectionDateRange(LocalDateTime st, LocalDateTime end){
		return(root, query, cb) -> {
			if(st != null && end != null) {
				return cb.between(root.get("inspection").get("inspectionDate"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("inspection").get("inspectionDate"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("inspection").get("inspectionDate"), end);
			}
			return null;
		};
	}
	
	public static Specification<InspectionDefect> hasInspectionDateAfter(LocalDateTime aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("inspection").get("inspectionDate"), aft);
	}
	
	public static Specification<InspectionDefect> hasInspectionDateBefore(LocalDateTime bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("inspection").get("inspectionDate"), bef);
	}
	
	public static Specification<InspectionDefect> hasInspectionDate(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("inspection").get("inspectionDate"), ld);
	}
	
	public static Specification<InspectionDefect> hasInspectionType(InspectionType type){
		return(root, query, cb) -> type == null ? null : cb.equal(root.get("inspection").get("type"), type);
	}
	
	public static Specification<InspectionDefect> hasInspectionCode(String code){
		return(root, query, cb) -> {
			if(code == null || code.isBlank()) return null;
			return cb.like(cb.lower(root.get("inspection").get("code")), "%" + code.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<InspectionDefect> hasInspectionIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("inspection").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("inspection").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("inspection").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<InspectionDefect> hasInspectionId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("inspection").get("id"), id);
	}
	
	public static Specification<InspectionDefect> hasQuantityAffectedRange(Integer min, Integer max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("quantityAffected"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("quantityAffected"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("quantityAffected"), max);
			}
			return null;
		};
	}
	
	public static Specification<InspectionDefect> hasQuantityAffected(Integer qa){
		return(root, query, cb) -> qa == null ? null : cb.equal(root.get("quantityAffected"), qa);
	}
	
	public static Specification<InspectionDefect> hasConfirmed(Boolean c){
		return(root, query, cb) -> c == null ? null : cb.equal(root.get("confirmed"), c);
	}
	
	public static Specification<InspectionDefect> hasInspectionDefectStatus(InspectionDefectStatus status){
		return(root, query, cb) -> status == null ?  null : cb.equal(root.get("status"), status);
	}
}
