package com.jovan.erp_v1.repository.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.DriverStatus;
import com.jovan.erp_v1.enumeration.TripStatus;
import com.jovan.erp_v1.enumeration.TripTypeStatus;
import com.jovan.erp_v1.model.Trip;
import com.jovan.erp_v1.request.TripSearchRequest;

/**
 *Specification Trip-Class for general-search, purposes
 */
public class TripSpecification {

	public static Specification<Trip> fromRequest(TripSearchRequest req) {
        return Specification.where(hasId(req.id()))
                .and(hasIdRange(req.idFrom(), req.idTo()))
                .and(hasStartLocation(req.startLocation()))
                .and(hasEndLocation(req.endLocation()))
                .and(hasStartTime(req.startTime()))
                .and(hasEndTime(req.endTime()))
                .and(hasTimeRange(req.startTime(), req.endTime()))
                .and(hasStartTimeAfter(req.startTimeAfter()))
                .and(hasStartTimeBefore(req.startTimeBefore()))
                .and(hasEndTimeAfter(req.endTimeAfter()))
                .and(hasEndTimeBefore(req.endTimeBefore()))
                .and(hasTripStatus(req.tripStatus()))
                .and(hasTypeStatus(req.typeStatus()))
                .and(hasDriverId(req.driverId()))
                .and(hasDriverIdRange(req.driverIdFrom(), req.driverIdTo()))
                .and(hasDriverFirstName(req.firstName()))
                .and(hasDriverLastName(req.lastName()))
                .and(hasDriverPhone(req.phone()))
                .and(hasDriverStatus(req.driverStatus()))
                .and(hasConfirmed(req.confirmed()));
    }
	
	public static Specification<Trip> hasId(Long id) {
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<Trip> hasIdRange(Long idFrom, Long idTo) {
        return (root, query, cb) -> {
            if (idFrom == null || idTo == null) return null;
            return cb.between(root.get("id"), idFrom, idTo);
        };
    }
    
    public static Specification<Trip> hasTimeRange(LocalDateTime min, LocalDateTime max){
    	return(root, query, cb) -> {
    		if(min != null && max != null) {
    			return cb.between(root.get("startTime"), min, max);
    		}
    		else if(min != null) {
    			return cb.greaterThanOrEqualTo(root.get("startTime"), min);
    		}
    		else if(max != null) {
    			return cb.greaterThanOrEqualTo(root.get("startTime"), max);
    		}
    		return null;
    	};
    }

    public static Specification<Trip> hasStartLocation(String startLocation) {
        return (root, query, cb) -> startLocation == null ? null :
                cb.like(cb.lower(root.get("startLocation")), "%" + startLocation.toLowerCase() + "%");
    }

    public static Specification<Trip> hasEndLocation(String endLocation) {
        return (root, query, cb) -> endLocation == null ? null :
                cb.like(cb.lower(root.get("endLocation")), "%" + endLocation.toLowerCase() + "%");
    }

    public static Specification<Trip> hasStartTime(LocalDateTime startTime) {
        return (root, query, cb) -> startTime == null ? null : cb.equal(root.get("startTime"), startTime);
    }

    public static Specification<Trip> hasEndTime(LocalDateTime endTime) {
        return (root, query, cb) -> endTime == null ? null : cb.equal(root.get("endTime"), endTime);
    }

    public static Specification<Trip> hasStartTimeAfter(LocalDateTime startTimeAfter) {
        return (root, query, cb) -> startTimeAfter == null ? null : cb.greaterThan(root.get("startTime"), startTimeAfter);
    }

    public static Specification<Trip> hasStartTimeBefore(LocalDateTime startTimeBefore) {
        return (root, query, cb) -> startTimeBefore == null ? null : cb.lessThan(root.get("startTime"), startTimeBefore);
    }

    public static Specification<Trip> hasEndTimeAfter(LocalDateTime endTimeAfter) {
        return (root, query, cb) -> endTimeAfter == null ? null : cb.greaterThan(root.get("endTime"), endTimeAfter);
    }

    public static Specification<Trip> hasEndTimeBefore(LocalDateTime endTimeBefore) {
        return (root, query, cb) -> endTimeBefore == null ? null : cb.lessThan(root.get("endTime"), endTimeBefore);
    }

    public static Specification<Trip> hasTripStatus(TripStatus tripStatus) {
        return (root, query, cb) -> tripStatus == null ? null : cb.equal(root.get("status"), tripStatus);
    }

    public static Specification<Trip> hasTypeStatus(TripTypeStatus typeStatus) {
        return (root, query, cb) -> typeStatus == null ? null : cb.equal(root.get("typeStatus"), typeStatus);
    }

    public static Specification<Trip> hasDriverId(Long driverId) {
        return (root, query, cb) -> driverId == null ? null : cb.equal(root.get("driver").get("id"), driverId);
    }

    public static Specification<Trip> hasDriverIdRange(Long driverIdFrom, Long driverIdTo) {
        return (root, query, cb) -> {
            if (driverIdFrom == null || driverIdTo == null) return null;
            return cb.between(root.get("driver").get("id"), driverIdFrom, driverIdTo);
        };
    }

    public static Specification<Trip> hasDriverFirstName(String firstName) {
        return (root, query, cb) -> firstName == null ? null :
                cb.like(cb.lower(root.get("driver").get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<Trip> hasDriverLastName(String lastName) {
        return (root, query, cb) -> lastName == null ? null :
                cb.like(cb.lower(root.get("driver").get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<Trip> hasDriverPhone(String phone) {
        return (root, query, cb) -> phone == null ? null :
                cb.like(cb.lower(root.get("driver").get("phone")), "%" + phone.toLowerCase() + "%");
    }

    public static Specification<Trip> hasDriverStatus(DriverStatus driverStatus) {
        return (root, query, cb) -> driverStatus == null ? null :
                cb.equal(root.get("driver").get("status"), driverStatus);
    }

    public static Specification<Trip> hasConfirmed(Boolean confirmed) {
        return (root, query, cb) -> confirmed == null ? null :
                cb.equal(root.get("driver").get("confirmed"), confirmed);
    }
}
