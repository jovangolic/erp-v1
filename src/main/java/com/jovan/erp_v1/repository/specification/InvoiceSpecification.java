package com.jovan.erp_v1.repository.specification;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import com.jovan.erp_v1.enumeration.InvoiceStatus;
import com.jovan.erp_v1.enumeration.InvoiceTypeStatus;
import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.PaymentStatus;
import com.jovan.erp_v1.model.Invoice;
import com.jovan.erp_v1.search_request.InvoiceSearchRequest;
import com.jovan.erp_v1.statistics.invoice.InvoiceSpecificationRequest;
import com.jovan.erp_v1.statistics.invoice.InvoiceStatByBuyerRequest;

import jakarta.persistence.criteria.Predicate;

public class InvoiceSpecification {

	public static Specification<Invoice> fromRequest(InvoiceSearchRequest req){
		return Specification.where(hasId(req.id()))
				.and(hasIdRange(req.idFrom(), req.idTo()))
				.and(hasInvoiceNumber(req.invoiceNumber()))
				.and(hasIssueDate(req.issueDate()))
				.and(hasIssueDateBefore(req.issueDateBefore()))
				.and(hasIssueDateAfter(req.issueDateAfter()))
				.and(hasIssueDateRange(req.issueDateAfter(), req.issueDateBefore()))
				.and(hasDueDate(req.dueDate()))
				.and(hasDueDateBefore(req.dueDateBefore()))
				.and(hasDueDateAfter(req.dueDateAfter()))
				.and(hasDueDateRange(req.dueDateAfter(), req.dueDateBefore()))
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
				.and(hasSalesId(req.salesId()))
				.and(hasSalesIdRange(req.salesIdFrom(), req.salesIdTo()))
				.and(hasSalesBuyerId(req.salesBuyerId()))
				.and(hasSalesBuyerIdRange(req.salesBuyerIdFrom(), req.salesBuyerIdTo()))
				.and(hasCreatedAt(req.createdAt()))
				.and(hasCreatedAtBefore(req.createdAtBefore()))
				.and(hasCreatedAtAfter(req.createdAtAfter()))
				.and(hasCreatedAtRange(req.createdAtAfter(), req.createdAtBefore()))
				.and(hasSalesTotalPrice(req.totalPrice()))
				.and(hasSalesTotalPriceRange(req.totalPriceMin(), req.totalPriceMax()))
				.and(hasPaymentId(req.paymentId()))
				.and(hasPaymentIdRange(req.paymentIdFrom(), req.paymentIdTo()))
				.and(hasPaymentAmount(req.amount()))
				.and(hasPaymentAmountRange(req.amountMin(), req.amountMax()))
				.and(hasPaymentDate(req.paymentDate()))
				.and(hasPaymentDateBefore(req.paymentDateBefore()))
				.and(hasPaymentDateAfter(req.paymentDateAfter()))
				.and(hasPaymentDateRange(req.paymentDateAfter(), req.paymentDateBefore()))
				.and(hasPaymentMethod(req.method()))
				.and(hasPaymentStatus(req.paymentStatus()))
				.and(hasPaymentReferenceNumber(req.paymentReferenceNumber()))
				.and(hasPaymentBuyerId(req.paymentBuyerId()))
				.and(hasPaymentBuyerIdRange(req.paymentBuyerIdFrom(), req.paymentBuyerIdTo()))
				.and(hasPaymentSalesId(req.paymentSalesId()))
				.and(hasPaymentSalesIdRange(req.paymentSalesIdFrom(), req.paymentSalesIdTo()))
				.and(hasSalesOrderId(req.salesOrderId()))
				.and(hasSalesOrderIdRange(req.salesOrderIdFrom(), req.salesOrderIdTo()))
				.and(hasSalesOrderNumber(req.salesOrderNumber()))
				.and(hasOrderDate(req.orderDate()))
				.and(hasOrderDateBefore(req.orderDateBefore()))
				.and(hasOrderDateAfter(req.orderDateAfter()))
				.and(hasOrderDateRange(req.orderDateAfter(), req.orderDateBefore()))
				.and(hasSalesTotalAmount(req.salesTotalAmount()))
				.and(hasSalesTotalAmountRange(req.salesTotalAmountMin(), req.salesTotalAmountMax()))
				.and(hasCreatedById(req.createdById()))
				.and(hasCreatedByIdRange(req.createdByIdFrom(), req.createdByIdTo()))
				.and(hasFullName(req.firstName(), req.lastName()))
				.and(hasCreatedByEmail(req.createdByEmail()))
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
	
	public static Specification<Invoice> hasCreatedByEmail(String em){
		return(root ,query, cb) -> {
			if(em == null || em.isBlank()) return null;
			return cb.like(cb.lower(root.get("createdBy").get("email")), "%" + em.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Invoice> hasFullName(String first, String last){
		return(root, query, cb) ->{
			if((first == null || first.isBlank()) && (last == null || last.isBlank())) return null;
			Predicate firstName = null;
			Predicate lastName = null;
			if(first != null && !first.isBlank()) {
				firstName = cb.like(cb.lower(root.get("createdBy").get("firstName")), "%" + first.toLowerCase().trim() + "%");
			}
			if(last != null && !last.isBlank()) {
				lastName = cb.like(cb.lower(root.get("createdBy").get("lastName")), "%" + last.toLowerCase().trim() + "%");
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
	
	public static Specification<Invoice> hasCreatedByIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("createdBy").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("createdBy").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("createdBy").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasCreatedById(Long id){
		return(root, query, cb) -> id == null ? null  :cb.equal(root.get("createdBy").get("id"), id);
	}
	
	public static Specification<Invoice> hasSalesTotalAmountRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) ->{
			if(min != null && max != null) {
				return cb.between(root.get("salesOrder").get("totalAmount"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("salesOrder").get("totalAmount"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("salesOrder").get("totalAmount"), max);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasSalesTotalAmount(BigDecimal bd){
		return(root, query, cb) -> bd == null ? null : cb.equal(root.get("salesOrder").get("totalAmount"), bd);
	}
	
	public static Specification<Invoice> hasOrderDateRange(LocalDateTime st, LocalDateTime end){
		return(root, query, cb) -> {
			if(st != null && end != null ) {
				return cb.between(root.get("salesOrder").get("orderDate"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("salesOrder").get("orderDate"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("salesOrder").get("orderDate"), end);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasOrderDateAfter(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.greaterThan(root.get("salesOrder").get("orderDate"), ld);
	}
	
	public static Specification<Invoice> hasOrderDateBefore(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.lessThan(root.get("salesOrder").get("orderDate"), ld);
	}
	
	public static Specification<Invoice> hasOrderDate(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("salesOrder").get("orderDate"), ld);
	}
	
	public static Specification<Invoice> hasSalesOrderNumber(String num){
		return(root, query, cb) -> num == null ? null : cb.equal(root.get("salesOrder").get("orderNumber"), num);
	}
	
	public static Specification<Invoice> hasSalesOrderIdRange(Long from, Long to){
		return(root ,query, cb) ->{
			if(from != null && to != null) {
				return cb.between(root.get("salesOrder").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("salesOrder").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("salesOrder").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasSalesOrderId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("salesOrder").get("id"), id);
	}
	
	public static Specification<Invoice> hasPaymentSalesIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("payment").get("relatedSales").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("payment").get("relatedSales").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("payment").get("relatedSales").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasPaymentSalesId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("payment").get("relatedSales").get("id"), id);
	}
	
	public static Specification<Invoice> hasPaymentBuyerIdRange(Long from, Long to){
		return(root, query, cb) ->{
			if(from != null && to != null) {
				return cb.between(root.get("payment").get("buyer").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("payment").get("buyer").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("payment").get("buyer").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasPaymentBuyerId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("payment").get("buyer").get("id") ,id);
	}
	
	public static Specification<Invoice> hasPaymentReferenceNumber(String num){
		return(root ,query, cb) -> {
			if(num == null || num.isBlank()) return null;
			return cb.like(cb.lower(root.get("payment").get("referenceNumber")), "%" + num.toLowerCase().trim() + "%");
		};
	}
	
	public static Specification<Invoice> hasPaymentStatus(PaymentStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("payment").get("status"), status);
	}
	
	public static Specification<Invoice> hasPaymentMethod(PaymentMethod met){
		return(root, query, cb) -> met == null ? null : cb.equal(root.get("payment").get("method"), met);
	}
	
	public static Specification<Invoice> hasPaymentDateRange(LocalDateTime st, LocalDateTime end){
		return(root, query, cb) ->{
			if(st != null && end != null) {
				return cb.between(root.get("payment").get("paymentDate"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("payment").get("paymentDate"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("payment").get("paymentDate"), end);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasPaymentDateAfter(LocalDateTime aft){
		return(root, query, cb) -> aft == null ? null : cb.greaterThan(root.get("payment").get("paymentDate"), aft);
	}
	
	public static Specification<Invoice> hasPaymentDateBefore(LocalDateTime bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("payment").get("paymentDate"), bef);
	}
	
	public static Specification<Invoice> hasPaymentDate(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null  :cb.equal(root.get("payment").get("paymentDate"), ld);
	}
	
	public static Specification<Invoice> hasPaymentAmountRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("payment").get("amount"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("payment").get("amount"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("payment").get("amount"), max);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasPaymentAmount(BigDecimal bd){
		return(root, query, cb) -> bd == null ? null : cb.equal(root.get("payment").get("amount"), bd);
	}
	
	public static Specification<Invoice> hasPaymentIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("payment").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("payment").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("payment").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasPaymentId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("payment").get("id"), id);
	}
	
	public static Specification<Invoice> hasSalesTotalPriceRange(BigDecimal min, BigDecimal max){
		return(root, query, cb) -> {
			if(min != null && max != null) {
				return cb.between(root.get("relatedSales").get("totalPrice"), min, max);
			}
			else if(min != null) {
				return cb.greaterThanOrEqualTo(root.get("relatedSales").get("totalPrice"), min);
			}
			else if(max != null) {
				return cb.lessThanOrEqualTo(root.get("relatedSales").get("totalPrice"), max);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasSalesTotalPrice(BigDecimal tp){
		return(root, query,cb) -> tp == null ? null : cb.equal(root.get("relatedSales").get("totalPrice"), tp);
	}
	
	public static Specification<Invoice> hasCreatedAtRange(LocalDateTime st, LocalDateTime end){
		return(root, query, cb) -> {
			if(st != null && end != null) {
				return cb.between(root.get("relatedSales").get("createdAt"), st, end);
			}
			else if(st != null) {
				return cb.greaterThanOrEqualTo(root.get("relatedSales").get("createdAt"), st);
			}
			else if(end != null) {
				return cb.lessThanOrEqualTo(root.get("relatedSales").get("createdAt"), end);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasCreatedAtAfter(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.greaterThan(root.get("relatedSales").get("createdAt"), ld);
	}
	
	public static Specification<Invoice> hasCreatedAtBefore(LocalDateTime bef){
		return(root, query, cb) -> bef == null ? null : cb.lessThan(root.get("relatedSales").get("createdAt"), bef);
	}
	
	public static Specification<Invoice> hasCreatedAt(LocalDateTime ld){
		return(root, query, cb) -> ld == null ? null : cb.equal(root.get("relatedSales").get("createdAt"), ld);
	}
	
	public static Specification<Invoice> hasSalesBuyerIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("relatedSales").get("buyer").get("id"), from, to);	
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("relatedSales").get("buyer").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("relatedSales").get("buyer").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasSalesBuyerId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("relatedSales").get("buyer").get("id"), id);
	}
	
	public static Specification<Invoice> hasSalesIdRange(Long from, Long to){
		return(root , query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("relatedSales").get("id"), from, to);
			}
			else if(from != null) {
				return cb.greaterThanOrEqualTo(root.get("relatedSales").get("id"), from);
			}
			else if(to != null) {
				return cb.lessThanOrEqualTo(root.get("relatedSales").get("id"), to);
			}
			return null;
		};
	}
	
	public static Specification<Invoice> hasSalesId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("relatedSales").get("id"), id);
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
	
	public static Specification<Invoice> withFiltersByBuyer(InvoiceStatByBuyerRequest request){
		return(root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<Predicate>();
			if(request.buyerId() != null) {
				predicates.add(cb.equal(root.get("buyer").get("id"), request.buyerId()));
			}
			if(request.fromDate() != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("issueDate"), request.fromDate().atStartOfDay()));
			}
			if(request.toDate() != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("issueDate"), request.toDate().atTime(23,59,59)));
			}
			predicates.add(cb.isTrue(root.get("confirmed")));
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	public static Specification<Invoice> withDynamicFilters(InvoiceSpecificationRequest request) {
		return(root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			// Ako ima polje buyerId
			if(hasField(request, "buyerId")) {
				Long buyerId = getFieldValue(request, "buyerId");
				if(buyerId != null) {
					predicates.add(cb.equal(root.get("buyer").get("id"), buyerId));
				}
			}
			// Ako ima polje salesId
			if(hasField(request, "salesId")) {
				Long salesId = getFieldValue(request, "salesId");
				if(salesId != null) {
					predicates.add(cb.equal(root.get("relatedSales").get("id"), salesId));
				}
			}
			//Datum od
			if(request.fromDate() != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("issueDate"), request.fromDate().atStartOfDay()));
			}
			//Datum do
			if(request.toDate() != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("issueDate"), request.toDate().atTime(23,59,59)));
			}
			// Samo potvrdjene fakture
			predicates.add(cb.isTrue(root.get("confirmed")));
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	private static Boolean hasField(Object obj, String fieldName) {
		try {
			return obj.getClass().getDeclaredField(fieldName) != null;
		}
		catch(NoSuchFieldException e) {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T getFieldValue(Object obj, String fieldName) {
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) field.get(obj);
		}
		catch(Exception e) {
			return null;
		}
	}
}
