package com.jovan.erp_v1.repository.specification;


import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.model.Shipment;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.TrackingInfo;

import jakarta.persistence.criteria.Join;


public class TrackingInfoSpecifications {

    public static Specification<TrackingInfo> hasOriginStorageType(StorageType type) {
        return (root, query, criteriaBuilder) -> {
            if (type == null) return criteriaBuilder.conjunction();

            Join<TrackingInfo, Shipment> shipmentJoin = root.join("shipment");
            Join<Shipment, Storage> storageJoin = shipmentJoin.join("originStorage");

            return criteriaBuilder.equal(storageJoin.get("type"), type);
        };
    }

    public static Specification<TrackingInfo> hasOriginStorageStatus(StorageStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) return criteriaBuilder.conjunction();

            Join<TrackingInfo, Shipment> shipmentJoin = root.join("shipment");
            Join<Shipment, Storage> storageJoin = shipmentJoin.join("originStorage");

            return criteriaBuilder.equal(storageJoin.get("status"), status);
        };
    }
}
