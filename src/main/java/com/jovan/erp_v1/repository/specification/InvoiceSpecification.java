package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.InvoiceStatus;
import com.jovan.erp_v1.enumeration.InvoiceTypeStatus;
import com.jovan.erp_v1.model.Invoice;
import com.jovan.erp_v1.search_request.InvoiceSearchRequest;

public class InvoiceSpecification {

	public static Specification<Invoice> fromRequest(InvoiceSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasInvoiceNumber(req.invoiceNumber()))
				.and(hasIssueDate(req.issueDate()))
				.and(hasIssueDateBefore(req.issueDateBefore()))
				.and(hasIssueDateAfter(req.issueDateAfter()))
				.and(hasIssueDateRange(req.issueDateBefore(), req.issueDateAfter()))
				.and(hasDueDate(req.dueDate()))
				.and(hasDueDateBefore(req.dueDateBefore()))
				.and(hasDueDateAfter(req.dueDateAfter()))
				.and(hasDueDateRange(req.dueDateBefore(), req.dueDateAfter()))
				.and(hasInvoiceStatus(req.status()))
				.and(hasTotalAmount(req.totalAmount()))
				.and(hasTotalAmountRange(req.totalAmountMin(), req.totalAmountMax()))
				.and(hasBuyerId(req.buyerId()))
				.and(hasBuyerIdRange(req.buyerIdFrom(), req.buyerIdTo()))
				.and(hasCompanyName(req.companyName()))
				.and(hasPib(req.pib()))
				.and(hasAddress(req.address()))
				.and(hasContactPerson(req.contactPerson()))
				.and(hasBuyerEmail(req.email()))
				.and(hasPhoneNumber(req.phoneNumber()))
				
				.and(hasNote(req.note()))
				.and(hasInvoiceTypeStatus(req.typeStatus()))
				.and(hasConfirmed(req.confirmed()));
	}
	
	public static Specification<Invoice> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<Invoice> hasIdRange(Long from, Long to){
		return(root ,query, cb) -> {
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
	
	public static Specification<Invoice> hasPhoneNumber(String num){
		return(root, query, cb) -> {
			if(num == null || num.isBlank()) return null;
			return cb.like(cb.lower(root.get("buyer").get("phoneNumber")), "%" + num.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Invoice> hasBuyerEmail(String em){
		return(root, query, cb) -> {
			if(em == null || em.isBlank()) return null;
			return cb.like(cb.lower(root.get("buyer").get("email")), "%" + em.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Invoice> hasContactPerson(String cp){
		return(root, query, cb) -> {
			if(cp == null || cp.isBlank()) return null;
			return cb.like(cb.lower(root.get("buyer").get("contactPerson")), "%" + cp.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Invoice> hasAddress(String adr){
		return(root, query, cb) -> {
			if(adr == null || adr.isBlank()) return null;
			return cb.like(cb.lower(root.get("buyer").get("address")), "%" + adr.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Invoice> hasPib(String pib){
		return(root, query, cb) -> {
			if(pib == null || pib.isBlank()) return null;
			return cb.like(cb.lower(root.get("buyer").get("pib")), "%" + pib.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Invoice> hasCompanyName(String name){
		return(root, query, cb) ->{
			if(name == null || name.isBlank()) return null;
			return cb.like(cb.lower(root.get("buyer").get("companyName")), "%" + name.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Invoice> hasBuyerIdRange(Long from, Long to){
		return(root, query,cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("buyer").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("buyer").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("buyer").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasBuyerId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("buyer").get("id"), id);
	}
	
	public static Specification<Invoice> hasTotalAmountRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("totalAmount"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("totalAmount"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("totalAmount"), max);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasTotalAmount(BigDecimal am){
		return(root, query, cb) -> am == null ? null : cb.equal(root.get("totalAmount"), am);
	}
	
	public static Specification<Invoice> hasInvoiceStatus(InvoiceStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status); 
	}
	
	public static Specification<Invoice> hasDueDateRange(LocalDateTime st, LocalDateTime end){
		return(root, query, cb) -> {
			if(st != null && end != null) {
				return cb.between(root.get("dueDate"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("dueDate"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("dueDate"), end);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasDueDateAfter(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.greaterThan(root.get("dueDate"), ld);
	}
	
	public static Specification<Invoice> hasDueDateBefore(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.lessThan(root.get("dueDate"), ld);
	}
	
	public static Specification<Invoice> hasDueDate(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("dueDate"), ld);
	}
	
	public static Specification<Invoice> hasIssueDateRange(LocalDateTime st, LocalDateTime end){
		return(root, query, cb) ->{
			if(st != null && end != null) {
				return cb.between(root.get("issueDate"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("issueDate"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("issueDate"), end);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasIssueDateAfter(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.greaterThan(root.get("issueDate"), ld);
	}
	
	public static Specification<Invoice> hasIssueDateBefore(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.lessThan(root.get("issueDate"), ld);
	}
	
	public static Specification<Invoice> hasIssueDate(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("issueDate"), ld);
	}
	
	public static Specification<Invoice> hasInvoiceNumber(String num){
		return(root, query, cb) -> {
			if(num == null || num.isBlank()) return null;
			return cb.like(cb.lower(root.get("invoiceNumber")), "%" + num.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Invoice> hasConfirmed(Boolean conf){
		return(root, query, cb) -> conf == null ? null : cb.equal(root.get("confirmed"), conf);
	}
	
	public static Specification<Invoice> hasInvoiceTypeStatus(InvoiceTypeStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("typeStatus"), status);
	}
	
	public static Specification<Invoice> hasNote(String note){
		return(root ,query, cb) -> {
			if(note == null || note.isBlank()) return null;
			return cb.like(cb.lower(root.get("note")), "%" + note.toLowerCase().trim() + "%");
		};
	}
}
