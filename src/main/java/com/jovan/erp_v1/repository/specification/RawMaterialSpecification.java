package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.model.RawMaterial;

public class RawMaterialSpecification {

	public static Specification<RawMaterial> hasShelfId(Long shelfId) {
        return (root, query, cb) -> shelfId == null ? null : cb.equal(root.get("shelf").get("id"), shelfId);
    }

    public static Specification<RawMaterial> quantityGreaterThan(BigDecimal quantity) {
        return (root, query, cb) -> quantity == null ? null : cb.greaterThan(root.get("currentQuantity"), quantity);
    }

    public static Specification<RawMaterial> quantityLessThan(BigDecimal quantity) {
        return (root, query, cb) -> quantity == null ? null : cb.lessThan(root.get("currentQuantity"), quantity);
    }

    public static Specification<RawMaterial> productQuantityGreaterThan(BigDecimal quantity) {
        return (root, query, cb) -> quantity == null ? null : cb.greaterThan(root.get("product").get("currentQuantity"), quantity);
    }

    public static Specification<RawMaterial> productIdEquals(Long productId) {
        return (root, query, cb) -> productId == null ? null : cb.equal(root.get("product").get("id"), productId);
    }
}
