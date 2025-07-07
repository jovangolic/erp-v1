package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.model.Sales;

public class SalesSpecifications {

	public static Specification<Sales> hasBuyerId(Long buyerId) {
        return (root, query, cb) ->
            buyerId == null ? null : cb.equal(root.get("buyer").get("id"), buyerId);
    }

    public static Specification<Sales> hasCompanyName(String name) {
        return (root, query, cb) ->
            name == null ? null : cb.like(cb.lower(root.get("buyer").get("companyName")), "%" + name.toLowerCase() + "%");
    }
    
    public static Specification<Sales> hasPib(String pib){
    	return (root, query, cb) -> 
    		pib == null ? null : cb.like(cb.lower(root.get("buyer").get("pib")), "%"+ pib.toLowerCase() + "%");
    }
    
    public static Specification<Sales> hasEmail(String email){
    	return (root, query, cb) -> 
    		email == null ? null : cb.like(cb.lower(root.get("buyer").get("email")), "%"+ email.toLowerCase() + "%");
    }
    
    public static Specification<Sales> hasPhoneNumber(String phoneNumber){
    	return (root, query, cb) -> 
    	phoneNumber == null ? null : cb.like(cb.lower(root.get("buyer").get("phoneNumber")), "%"+ phoneNumber.toLowerCase() + "%");
    }
    
    public static Specification<Sales> hasAddress(String address){
    	return (root, query, cb) -> 
    		address == null ? null : cb.like(cb.lower(root.get("buyer").get("address")), "%"+ address.toLowerCase() + "%");
    }
    
    public static Specification<Sales> hasContactPerson(String contactPerson){
    	return (root, query, cb) -> 
    	contactPerson == null ? null : cb.like(cb.lower(root.get("buyer").get("contactPerson")), "%"+ contactPerson.toLowerCase() + "%");
    }

    public static Specification<Sales> minTotalPrice(BigDecimal min) {
        return (root, query, cb) ->
            min == null ? null : cb.greaterThanOrEqualTo(root.get("totalPrice"), min);
    }

    public static Specification<Sales> maxTotalPrice(BigDecimal max) {
        return (root, query, cb) ->
            max == null ? null : cb.lessThanOrEqualTo(root.get("totalPrice"), max);
    }
     
}
