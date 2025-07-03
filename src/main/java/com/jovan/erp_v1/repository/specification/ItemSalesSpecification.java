package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.model.ItemSales;

public class ItemSalesSpecification {

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
