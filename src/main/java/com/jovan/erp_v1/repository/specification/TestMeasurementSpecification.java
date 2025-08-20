package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.Inspection;
import com.jovan.erp_v1.model.Product;
import com.jovan.erp_v1.model.QualityStandard;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.Supply;
import com.jovan.erp_v1.model.TestMeasurement;
import com.jovan.erp_v1.model.User;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class TestMeasurementSpecification {

	public static Specification<TestMeasurement> hasInspectionId(Long inspectionId) {
        return (root, query, cb) -> 
            inspectionId == null ? null : cb.equal(root.get("inspection").get("id"), inspectionId);
    }

    public static Specification<TestMeasurement> productNameContains(String productName) {
        return (root, query, cb) -> 
            productName == null ? null : cb.like(cb.lower(root.get("inspection").get("product").get("name")),
                                                "%" + productName.toLowerCase() + "%");
    }

    public static Specification<TestMeasurement> hasQualityCheckStatus(QualityCheckStatus status) {
        return (root, query, cb) -> 
            status == null ? null : cb.equal(root.get("inspection").get("qualityCheck").get("status"), status);
    }

    public static Specification<TestMeasurement> measuredValueGreaterThan(BigDecimal value) {
        return (root, query, cb) -> 
            value == null ? null : cb.greaterThan(root.get("measuredValue"), value);
    }

    public static Specification<TestMeasurement> dateBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start != null && end != null) {
                return cb.between(root.get("inspection").get("date"), start, end);
            }
            return null;
        };
    }
    
    public static Specification<TestMeasurement> supplyUpdatedAfter(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) return null;
            return cb.greaterThan(
                    root.join("standard")
                        .join("product")
                        .join("supply")
                        .get("updates"),
                    date
            );
        };
    }

    public static Specification<TestMeasurement> inspectionResult(InspectionResult result) {
        return (root, query, cb) -> {
            if (result == null) return null;
            return cb.equal(
                    root.join("inspection").get("result"),
                    result
            );
        };
    }
    
 // --- Storage filters ---
    public static Specification<TestMeasurement> storageCapacityGreaterThan(BigDecimal capacity) {
        return (root, query, cb) -> {
            Join<TestMeasurement, Product> product = root.join("inspection").join("product");
            Join<Product, Storage> storage = product.join("storage", JoinType.LEFT);
            return cb.greaterThan(storage.get("capacity"), capacity);
        };
    }

    public static Specification<TestMeasurement> storageCapacityLessThan(BigDecimal capacity) {
        return (root, query, cb) -> {
            Join<TestMeasurement, Product> product = root.join("inspection").join("product");
            Join<Product, Storage> storage = product.join("storage", JoinType.LEFT);
            return cb.lessThan(storage.get("capacity"), capacity);
        };
    }

    public static Specification<TestMeasurement> storageNameContains(String name) {
        return (root, query, cb) -> {
            Join<TestMeasurement, Product> product = root.join("inspection").join("product");
            Join<Product, Storage> storage = product.join("storage", JoinType.LEFT);
            return cb.like(cb.lower(storage.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<TestMeasurement> storageLocationContains(String location) {
        return (root, query, cb) -> {
            Join<TestMeasurement, Product> product = root.join("inspection").join("product");
            Join<Product, Storage> storage = product.join("storage", JoinType.LEFT);
            return cb.like(cb.lower(storage.get("location")), "%" + location.toLowerCase() + "%");
        };
    }

    public static Specification<TestMeasurement> storageStatus(StorageStatus status) {
        return (root, query, cb) -> {
            Join<TestMeasurement, Product> product = root.join("inspection").join("product");
            Join<Product, Storage> storage = product.join("storage", JoinType.LEFT);
            return cb.equal(storage.get("status"), status);
        };
    }

    public static Specification<TestMeasurement> storageType(StorageType type) {
        return (root, query, cb) -> {
            Join<TestMeasurement, Product> product = root.join("inspection").join("product");
            Join<Product, Storage> storage = product.join("storage", JoinType.LEFT);
            return cb.equal(storage.get("type"), type);
        };
    }


    // --- Inspector filters ---
    public static Specification<TestMeasurement> inspectorFirstNameContains(String firstName) {
        return (root, query, cb) -> {
            Join<TestMeasurement, Inspection> inspection = root.join("inspection");
            Join<Inspection, User> inspector = inspection.join("inspector", JoinType.LEFT);
            return cb.like(cb.lower(inspector.get("firstName")), "%" + firstName.toLowerCase() + "%");
        };
    }

    public static Specification<TestMeasurement> inspectorLastNameContains(String lastName) {
        return (root, query, cb) -> {
            Join<TestMeasurement, Inspection> inspection = root.join("inspection");
            Join<Inspection, User> inspector = inspection.join("inspector", JoinType.LEFT);
            return cb.like(cb.lower(inspector.get("lastName")), "%" + lastName.toLowerCase() + "%");
        };
    }

    public static Specification<TestMeasurement> inspectorEmailContains(String email) {
        return (root, query, cb) -> {
            Join<TestMeasurement, Inspection> inspection = root.join("inspection");
            Join<Inspection, User> inspector = inspection.join("inspector", JoinType.LEFT);
            return cb.like(cb.lower(inspector.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

    public static Specification<TestMeasurement> inspectorPhoneContains(String phone) {
        return (root, query, cb) -> {
            Join<TestMeasurement, Inspection> inspection = root.join("inspection");
            Join<Inspection, User> inspector = inspection.join("inspector", JoinType.LEFT);
            return cb.like(cb.lower(inspector.get("phoneNumber")), "%" + phone.toLowerCase() + "%");
        };
    }
    
 // --- Storage filters (preko Standard - Product - Storage) ---
    public static Specification<TestMeasurement> standardStorageNameContains(String name) {
        return (root, query, cb) -> {
            Join<TestMeasurement, QualityStandard> standard = root.join("standard");
            Join<QualityStandard, Product> product = standard.join("product");
            Join<Product, Storage> storage = product.join("storage", JoinType.LEFT);
            return cb.like(cb.lower(storage.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<TestMeasurement> standardStorageLocationContains(String location) {
        return (root, query, cb) -> {
            Join<TestMeasurement, QualityStandard> standard = root.join("standard");
            Join<QualityStandard, Product> product = standard.join("product");
            Join<Product, Storage> storage = product.join("storage", JoinType.LEFT);
            return cb.like(cb.lower(storage.get("location")), "%" + location.toLowerCase() + "%");
        };
    }

    public static Specification<TestMeasurement> standardStorageCapacityEquals(BigDecimal capacity) {
        return (root, query, cb) -> {
            Join<TestMeasurement, QualityStandard> standard = root.join("standard");
            Join<QualityStandard, Product> product = standard.join("product");
            Join<Product, Storage> storage = product.join("storage", JoinType.LEFT);
            return cb.equal(storage.get("capacity"), capacity);
        };
    }

    public static Specification<TestMeasurement> standardStorageCapacityGreaterThan(BigDecimal capacity) {
        return (root, query, cb) -> {
            Join<TestMeasurement, QualityStandard> standard = root.join("standard");
            Join<QualityStandard, Product> product = standard.join("product");
            Join<Product, Storage> storage = product.join("storage", JoinType.LEFT);
            return cb.greaterThan(storage.get("capacity"), capacity);
        };
    }

    public static Specification<TestMeasurement> standardStorageCapacityLessThan(BigDecimal capacity) {
        return (root, query, cb) -> {
            Join<TestMeasurement, QualityStandard> standard = root.join("standard");
            Join<QualityStandard, Product> product = standard.join("product");
            Join<Product, Storage> storage = product.join("storage", JoinType.LEFT);
            return cb.lessThan(storage.get("capacity"), capacity);
        };
    }

    public static Specification<TestMeasurement> standardStorageType(StorageType type) {
        return (root, query, cb) -> {
            Join<TestMeasurement, QualityStandard> standard = root.join("standard");
            Join<QualityStandard, Product> product = standard.join("product");
            Join<Product, Storage> storage = product.join("storage", JoinType.LEFT);
            return cb.equal(storage.get("type"), type);
        };
    }

    public static Specification<TestMeasurement> standardStorageStatus(StorageStatus status) {
        return (root, query, cb) -> {
            Join<TestMeasurement, QualityStandard> standard = root.join("standard");
            Join<QualityStandard, Product> product = standard.join("product");
            Join<Product, Storage> storage = product.join("storage", JoinType.LEFT);
            return cb.equal(storage.get("status"), status);
        };
    }

    public static Specification<TestMeasurement> standardStorageTypeAndStatus(StorageType type, StorageStatus status) {
        return (root, query, cb) -> {
            Join<TestMeasurement, QualityStandard> standard = root.join("standard");
            Join<QualityStandard, Product> product = standard.join("product");
            Join<Product, Storage> storage = product.join("storage", JoinType.LEFT);
            return cb.and(
                    cb.equal(storage.get("type"), type),
                    cb.equal(storage.get("status"), status)
            );
        };
    }


    // --- Supply filters (preko Standard - Product - Supply) ---
    public static Specification<TestMeasurement> standardSupplyQuantityEquals(BigDecimal quantity) {
        return (root, query, cb) -> {
            Join<TestMeasurement, QualityStandard> standard = root.join("standard");
            Join<QualityStandard, Product> product = standard.join("product");
            Join<Product, Supply> supply = product.join("supply", JoinType.LEFT);
            return cb.equal(supply.get("quantity"), quantity);
        };
    }

    public static Specification<TestMeasurement> standardSupplyQuantityGreaterThan(BigDecimal quantity) {
        return (root, query, cb) -> {
            Join<TestMeasurement, QualityStandard> standard = root.join("standard");
            Join<QualityStandard, Product> product = standard.join("product");
            Join<Product, Supply> supply = product.join("supply", JoinType.LEFT);
            return cb.greaterThan(supply.get("quantity"), quantity);
        };
    }

    public static Specification<TestMeasurement> standardSupplyQuantityLessThan(BigDecimal quantity) {
        return (root, query, cb) -> {
            Join<TestMeasurement, QualityStandard> standard = root.join("standard");
            Join<QualityStandard, Product> product = standard.join("product");
            Join<Product, Supply> supply = product.join("supply", JoinType.LEFT);
            return cb.lessThan(supply.get("quantity"), quantity);
        };
    }

    public static Specification<TestMeasurement> standardSupplyUpdatesEquals(LocalDateTime updates) {
        return (root, query, cb) -> {
            Join<TestMeasurement, QualityStandard> standard = root.join("standard");
            Join<QualityStandard, Product> product = standard.join("product");
            Join<Product, Supply> supply = product.join("supply", JoinType.LEFT);
            return cb.equal(supply.get("updates"), updates);
        };
    }

    public static Specification<TestMeasurement> standardSupplyUpdatesAfter(LocalDateTime updates) {
        return (root, query, cb) -> {
            Join<TestMeasurement, QualityStandard> standard = root.join("standard");
            Join<QualityStandard, Product> product = standard.join("product");
            Join<Product, Supply> supply = product.join("supply", JoinType.LEFT);
            return cb.greaterThan(supply.get("updates"), updates);
        };
    }

    public static Specification<TestMeasurement> standardSupplyUpdatesBefore(LocalDateTime updates) {
        return (root, query, cb) -> {
            Join<TestMeasurement, QualityStandard> standard = root.join("standard");
            Join<QualityStandard, Product> product = standard.join("product");
            Join<Product, Supply> supply = product.join("supply", JoinType.LEFT);
            return cb.lessThan(supply.get("updates"), updates);
        };
    }

    public static Specification<TestMeasurement> standardSupplyUpdatesBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            Join<TestMeasurement, QualityStandard> standard = root.join("standard");
            Join<QualityStandard, Product> product = standard.join("product");
            Join<Product, Supply> supply = product.join("supply", JoinType.LEFT);
            return cb.between(supply.get("updates"), start, end);
        };
    }
    
    public static Specification<TestMeasurement> hasStorageNameLike(String storageName) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("standard")
                                   .get("product")
                                   .get("storage")
                                   .get("name")),
                        "%" + storageName.toLowerCase() + "%");
    }

    public static Specification<TestMeasurement> hasStorageLocationLike(String location) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("standard")
                                   .get("product")
                                   .get("storage")
                                   .get("location")),
                        "%" + location.toLowerCase() + "%");
    }

    public static Specification<TestMeasurement> hasStorageCapacityGreaterThan(BigDecimal capacity) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("standard")
                                            .get("product")
                                            .get("storage")
                                            .get("capacity"), capacity);
    }

    public static Specification<TestMeasurement> hasStorageCapacityLessThan(BigDecimal capacity) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("standard")
                                         .get("product")
                                         .get("storage")
                                         .get("capacity"), capacity);
    }

    public static Specification<TestMeasurement> hasStorageType(StorageType type) {
        return (root, query, cb) ->
                cb.equal(root.get("standard")
                             .get("product")
                             .get("storage")
                             .get("type"), type);
    }

    public static Specification<TestMeasurement> hasStorageStatus(StorageStatus status) {
        return (root, query, cb) ->
                cb.equal(root.get("standard")
                             .get("product")
                             .get("storage")
                             .get("status"), status);
    }

    // ===== Supply =====

    public static Specification<TestMeasurement> hasSupplyQuantityGreaterThan(BigDecimal quantity) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("standard")
                                            .get("product")
                                            .get("supply")
                                            .get("quantity"), quantity);
    }

    public static Specification<TestMeasurement> hasSupplyQuantityLessThan(BigDecimal quantity) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("standard")
                                         .get("product")
                                         .get("supply")
                                         .get("quantity"), quantity);
    }

    public static Specification<TestMeasurement> hasSupplyUpdatesAfter(LocalDateTime after) {
        return (root, query, cb) ->
                cb.greaterThan(root.get("standard")
                                   .get("product")
                                   .get("supply")
                                   .get("updates"), after);
    }

    public static Specification<TestMeasurement> hasSupplyUpdatesBefore(LocalDateTime before) {
        return (root, query, cb) ->
                cb.lessThan(root.get("standard")
                                .get("product")
                                .get("supply")
                                .get("updates"), before);
    }
}
