package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.model.Inspection;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class InspectionSpecifications {

	public static Specification<Inspection> hasStorageNameLike(String storageName) {
        return (root, query, cb) -> {
            if (storageName == null || storageName.isBlank()) return null;
            Join<Object, Object> storage = root.join("product").join("storage", JoinType.INNER);
            return cb.like(cb.lower(storage.get("name")), "%" + storageName.toLowerCase() + "%");
        };
    }

    public static Specification<Inspection> hasStorageLocationLike(String storageLocation) {
        return (root, query, cb) -> {
            if (storageLocation == null || storageLocation.isBlank()) return null;
            Join<Object, Object> storage = root.join("product").join("storage", JoinType.INNER);
            return cb.like(cb.lower(storage.get("location")), "%" + storageLocation.toLowerCase() + "%");
        };
    }

    public static Specification<Inspection> hasStorageCapacityEqual(BigDecimal capacity) {
        return (root, query, cb) -> {
            if (capacity == null) return null;
            Join<Object, Object> storage = root.join("product").join("storage", JoinType.INNER);
            return cb.equal(storage.get("capacity"), capacity);
        };
    }

    public static Specification<Inspection> hasStorageCapacityGreaterThan(BigDecimal capacity) {
        return (root, query, cb) -> {
            if (capacity == null) return null;
            Join<Object, Object> storage = root.join("product").join("storage", JoinType.INNER);
            return cb.greaterThanOrEqualTo(storage.get("capacity"), capacity);
        };
    }

    public static Specification<Inspection> hasStorageCapacityLessThan(BigDecimal capacity) {
        return (root, query, cb) -> {
            if (capacity == null) return null;
            Join<Object, Object> storage = root.join("product").join("storage", JoinType.INNER);
            return cb.lessThanOrEqualTo(storage.get("capacity"), capacity);
        };
    }

    public static Specification<Inspection> hasStorageCapacityBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min == null || max == null) return null;
            Join<Object, Object> storage = root.join("product").join("storage", JoinType.INNER);
            return cb.between(storage.get("capacity"), min, max);
        };
    }
}
