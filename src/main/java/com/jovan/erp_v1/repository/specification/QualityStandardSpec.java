package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.model.QualityStandard;

public class QualityStandardSpec {

	// Filter po supply ID iz proizvoda
    public static Specification<QualityStandard> hasSupplyId(Long supplyId) {
        return (root, query, cb) -> {
            if (supplyId == null) return cb.conjunction();
            return cb.equal(root.get("product").get("supply").get("id"), supplyId);
        };
    }

    // Filter po kapacitetu skladista proizvoda
    public static Specification<QualityStandard> hasProductStorageCapacityBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return cb.conjunction();
            if (min != null && max != null)
                return cb.between(root.get("product").get("storage").get("capacity"), min, max);
            if (min != null)
                return cb.greaterThanOrEqualTo(root.get("product").get("storage").get("capacity"), min);
            return cb.lessThanOrEqualTo(root.get("product").get("storage").get("capacity"), max);
        };
    }

    // Filter po polici i redu
    public static Specification<QualityStandard> hasShelfRowAndCol(Integer row, Integer col) {
        return (root, query, cb) -> {
            if (row == null && col == null) return cb.conjunction();
            if (row != null && col != null)
                return cb.and(
                        cb.equal(root.get("product").get("shelf").get("rowCount"), row),
                        cb.equal(root.get("product").get("shelf").get("cols"), col)
                );
            if (row != null)
                return cb.equal(root.get("product").get("shelf").get("rowCount"), row);
            return cb.equal(root.get("product").get("shelf").get("cols"), col);
        };
    }

    // Filter po kapacitetu supply-a 
    public static Specification<QualityStandard> hasSupplyCapacityBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return cb.conjunction();
            if (min != null && max != null)
                return cb.between(root.get("product").get("supply").get("quantity"), min, max);
            if (min != null)
                return cb.greaterThanOrEqualTo(root.get("product").get("supply").get("quantity"), min);
            return cb.lessThanOrEqualTo(root.get("product").get("supply").get("quantity"), max);
        };
    }
}
