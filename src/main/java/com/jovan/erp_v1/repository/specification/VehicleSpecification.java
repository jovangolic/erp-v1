package com.jovan.erp_v1.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.jovan.erp_v1.enumeration.VehicleFuel;
import com.jovan.erp_v1.enumeration.VehicleStatus;
import com.jovan.erp_v1.enumeration.VehicleTypeStatus;
import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.search_request.VehicleSearchRequest;

/**
 *Specification Vehicle-Class for general-search, purposes
 */
public class VehicleSpecification {

	public static Specification<Vehicle> fromRequest(VehicleSearchRequest req){
		return Specification.where(hasId(req.id()))
			.and(hasIdRange(req.idFrom(), req.idTo()))
			.and(hasRegistrationNumber(req.registrationNumber()))
	        .and(hasModel(req.model()))
	        .and(hasStatus(req.status()))
	        .and(hasFuel(req.fuel()))
	        .and(hasTypeStatus(req.typeStatus()))
	        .and(hasConfirmed(req.confirmed()));
	}
	
	public static Specification<Vehicle> hasId(Long id){
		return(root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
	}
	
	public static Specification<Vehicle> hasIdRange(Long idFrom, Long idTo){
		return(root, query, cb) -> {
			if(idFrom == null || idTo == null) return null;
			return cb.between(root.get("id"), idFrom, idTo);
		};
	}
	
	public static Specification<Vehicle> hasRegistrationNumber(String registrationNumber){
		return(root, query, cb) -> registrationNumber == null ? null 
				: cb.like(cb.lower(root.get("registrationNumber")), "%" + registrationNumber.toLowerCase() +"%");
	}
	
	public static Specification<Vehicle> hasModel(String model){
		return(root, query, cb) -> model == null ? null :
			cb.like(cb.lower(root.get("model")),  "%" + model.toLowerCase() + "%");
	}
	
	public static Specification<Vehicle> hasStatus(VehicleStatus status){
		return(root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
	}
	
	public static Specification<Vehicle> hasFuel(VehicleFuel fuel){
		return(root, query, cb) -> fuel == null ? null : cb.equal(root.get("fuel"), fuel);
	}
	
	public static Specification<Vehicle> hasTypeStatus(VehicleTypeStatus typeStatus){
		return(root, query, cb) -> typeStatus == null ? null : cb.equal(root.get("typeStatus"), typeStatus);
	}
	
	public static Specification<Vehicle> hasConfirmed(Boolean confirmed){
		return(root, query, cb) -> confirmed == null ? null : cb.equal(root.get("confirmed"), confirmed);
	}
}
