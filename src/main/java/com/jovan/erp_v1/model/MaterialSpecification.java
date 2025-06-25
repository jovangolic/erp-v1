package com.jovan.erp_v1.model;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.UnitOfMeasure;

import java.math.BigDecimal;

public class MaterialSpecification {

    public static Specification<Material> hasName(String name) {
        return (root, query, cb) -> name == null ? null
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Material> hasCode(String code) {
        return (root, query, cb) -> code == null ? null : cb.equal(root.get("code"), code);
    }

    public static Specification<Material> hasUnit(UnitOfMeasure unit) {
        return (root, query, cb) -> unit == null ? null : cb.equal(root.get("unit"), unit);
    }

    public static Specification<Material> hasCurrentStock(BigDecimal stock) {
        return (root, query, cb) -> stock == null ? null : cb.equal(root.get("currentStock"), stock);
    }

    public static Specification<Material> hasStorageId(Long storageId) {
        return (root, query, cb) -> storageId == null ? null : cb.equal(root.get("storage").get("id"), storageId);
    }

    public static Specification<Material> hasReorderLevel(BigDecimal reorderLevel) {
        return (root, query, cb) -> reorderLevel == null ? null : cb.equal(root.get("reorderLevel"), reorderLevel);
    }
}