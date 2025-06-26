package com.jovan.erp_v1.repository.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.ProductionOrderStatus;
import com.jovan.erp_v1.model.ProductionOrder;

public class ProductionOrderSpecification {

    public static Specification<ProductionOrder> hasProductName(String productName) {
        return (root, query, cb) -> {
            if (productName == null || productName.isBlank())
                return null;
            return cb.like(cb.lower(root.get("product").get("name")), "%" + productName.toLowerCase() + "%");
        };
    }

    public static Specification<ProductionOrder> hasWorkCenterName(String workCenterName) {
        return (root, query, cb) -> {
            if (workCenterName == null || workCenterName.isBlank())
                return null;
            return cb.like(cb.lower(root.get("workCenter").get("name")), "%" + workCenterName.toLowerCase() + "%");
        };
    }

    public static Specification<ProductionOrder> hasStatus(ProductionOrderStatus status) {
        return (root, query, cb) -> {
            if (status == null)
                return null;
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<ProductionOrder> hasStartDateBetween(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from != null && to != null) {
                return cb.between(root.get("startDate"), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("startDate"), from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(root.get("startDate"), to);
            }
            return null;
        };
    }
}
