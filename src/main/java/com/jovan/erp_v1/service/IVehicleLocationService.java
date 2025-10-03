package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;

import com.jovan.erp_v1.request.VehicleLocationRequest;
import com.jovan.erp_v1.response.VehicleLocationResponse;
import com.jovan.erp_v1.response.VehicleResponse;
import com.jovan.erp_v1.save_as.VehicleLocationSaveAsRequest;
import com.jovan.erp_v1.search_request.VehicleLocationSearchRequest;

public interface IVehicleLocationService {

	VehicleLocationResponse create(VehicleLocationRequest request);
	VehicleLocationResponse update(Long id, VehicleLocationRequest request);
	void delete(Long id);
	VehicleLocationResponse findOne(Long id);
	List<VehicleLocationResponse> findAll();
	List<VehicleLocationResponse> findLocationsByTimeRange(LocalDateTime from, LocalDateTime to);
    List<VehicleResponse> findVehiclesByLocationTimeRange(LocalDateTime from, LocalDateTime to);
    List<VehicleLocationResponse> findLocationsByRequest(VehicleLocationSearchRequest req);
    
    List<VehicleLocationResponse> generalSearch(VehicleLocationSearchRequest req);
    VehicleLocationResponse saveVehicleLocation(VehicleLocationRequest request);
    VehicleLocationResponse saveAs(VehicleLocationSaveAsRequest req);
    List<VehicleLocationResponse> saveAll(List<VehicleLocationRequest> requests);
}
