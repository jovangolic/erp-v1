package com.jovan.erp_v1.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.Storage;

public class StorageSpecifications {

	public static Specification<Storage> hasType(StorageType type){
		return (root, query, criteriaBuilder) -> {
			if(type == null) return criteriaBuilder.conjunction();
			return criteriaBuilder.equal(root.get("type"), type);
		};
	}
	
	public static Specification<Storage> hasStatus(StorageStatus status){
		return (root,query,criteriaBuilder) -> {
			if(status == null) return criteriaBuilder.conjunction();
			return criteriaBuilder.equal(root.get("status"), status);
		};
	}
}
