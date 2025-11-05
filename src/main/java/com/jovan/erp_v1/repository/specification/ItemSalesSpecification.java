package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.search_request.ItemSalesSearchRequest;

public class ItemSalesSpecification {
	
	public static Specification<ItemSales> fromRequest(ItemSalesSearchRequest req){
		return Specification.where(hasId(req.id()))
				
				.and(hasIdRange(req.idFrom(), req.idTo()));
	}
	
	public static Specification<ItemSales> hasId(Long id){
		return(root, query, cb) -> id == null ? null  :cb.equal(root.get("id"), id);
	}
	
	public static Specification<ItemSales> hasIdRange(Long from, Long to){
		return(root, query, cb) -> {
			if(from != null && to != null) {
				return cb.between(root.get("id"), from , to);
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

	public static Specification<ItemSales> hasGoodsId(Long goodsId) {
        return (root, query, cb) -> cb.equal(root.get("goods").get("id"), goodsId);
    }

    public static Specification<ItemSales> hasBuyerId(Long buyerId) {
        return (root, query, cb) -> cb.equal(root.get("sales").get("buyer").get("id"), buyerId);
    }

    public static Specification<ItemSales> createdAfter(LocalDateTime date) {
        return (root, query, cb) -> cb.greaterThan(root.get("sales").get("createdAt"), date);
    }

    public static Specification<ItemSales> quantityGreaterThan(BigDecimal quantity) {
        return (root, query, cb) -> cb.greaterThan(root.get("quantity"), quantity);
    }

    public static Specification<ItemSales> unitPriceBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> cb.between(root.get("unitPrice"), min, max);
    }

    public static Specification<ItemSales> hasSalesOrderStatus(OrderStatus status) {
        return (root, query, cb) -> cb.equal(root.get("salesOrder").get("status"), status);
    }
    
    public static Specification<ItemSales> createdBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.between(root.get("sales").get("createdAt"), start, end);
    }
    
    public static Specification<ItemSales> totalPriceBetween(BigDecimal min, BigDecimal max) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.between(root.get("sales").get("totalPrice"), min, max);
    }
}
