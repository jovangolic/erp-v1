package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.WorkCenter;

public class WorkCenterSpecification {

	public static Specification<WorkCenter> nameContains(String name){
		return (root, query, cb) -> name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
	}
	
	public static Specification<WorkCenter> locationContains(String location){
		return (root,query,cb) -> location == null ? null : cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
	}
	
	public static Specification<WorkCenter> capacityGreaterThan(BigDecimal capacity) {
        return (root, query, cb) -> capacity == null ? null : cb.greaterThan(root.get("capacity"), capacity);
    }

    public static Specification<WorkCenter> capacityLessThan(BigDecimal capacity) {
        return (root, query, cb) -> capacity == null ? null : cb.lessThan(root.get("capacity"), capacity);
    }

    public static Specification<WorkCenter> storageTypeEquals(StorageType type) {
        return (root, query, cb) -> type == null ? null : cb.equal(root.get("localStorage").get("type"), type);
    }

    public static Specification<WorkCenter> storageStatusEquals(StorageStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("localStorage").get("status"), status);
    }
}
