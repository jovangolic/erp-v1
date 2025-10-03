package com.jovan.erp_v1.repository.specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.model.VehicleLocation;
import com.jovan.erp_v1.search_request.VehicleLocationSearchRequest;

import jakarta.persistence.criteria.Join;

public class VehicleLocationSpecification {
	
	public static Specification<VehicleLocation> fromRequest(VehicleLocationSearchRequest req) {
	    return Specification.where(hasId(req.id()))
	            .and(hasIdRange(req.idFrom(), req.idTo()))
	            .and(hasVehicleId(req.vehicleId()))
	            .and(hasVehicleRangeId(req.vehicleIdFrom(), req.vehicleIdTo()))
	            .and(hasLatitude(req.latitude()))
	            .and(hasLongitude(req.longitude()))
	            .and(hasRecordedAtBetween(req.recordedAtFrom(), req.recordedAtTo()));
	}
	
	public static Specification<VehicleLocation> hasId(Long id) {
		return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<VehicleLocation> hasIdRange(Long idFrom, Long idTo){
		return(root, query, cb) -> {
			if(idFrom == null || idTo == null) return null;
			return cb.between(root.get("id"), idFrom, idTo);
		};
	}
	
	public static Specification<VehicleLocation> hasVehicleId(Long vehicleId) {
		return (root, query, cb) -> vehicleId == null ? null : cb.equal(root.get("vehicle").get("id"), vehicleId);
	}
	
	public static Specification<VehicleLocation> hasVehicleRangeId(Long idFrom, Long idTo) {
		return (root, query, cb) -> {
			if (idFrom == null || idTo == null) return null;
			return cb.between(root.get("vehicle").get("id"), idFrom, idTo);
		};
	}
	
	//Kombinovani upit na osnovu vozila i lokacije
	public static Specification<Vehicle> hadLocationBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            Join<Vehicle, VehicleLocation> locations = root.join("vehicleLocations");
            if (from != null && to != null) {
                return cb.between(locations.get("recordedAt"), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(locations.get("recordedAt"), from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(locations.get("recordedAt"), to);
            }
            return null;
        };
    }
	
	public static Specification<VehicleLocation> hasLatitude(BigDecimal latitude) {
		return (root, query, cb) -> latitude == null ? null : cb.equal(root.get("latitude"), latitude);
	}
	
	public static Specification<VehicleLocation> hasLongitude(BigDecimal longitude) {
		return (root, query, cb) -> longitude == null ? null : cb.equal(root.get("longitude"), longitude);
	}
	
	public static Specification<VehicleLocation> hasRecordedAtBetween(LocalDateTime from, LocalDateTime to) {
		return (root, query, cb) -> {
			if (from != null && to != null) {
				return cb.between(root.get("recordedAt"), from, to);
			} else if (from != null) {
				return cb.greaterThanOrEqualTo(root.get("recordedAt"), from);
			} else if (to != null) {
				return cb.lessThanOrEqualTo(root.get("recordedAt"), to);
			}
			return null;
		};
	}
}
