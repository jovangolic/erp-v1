package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.enumeration.DriverStatus;

import com.jovan.erp_v1.request.DriverRequest;
import com.jovan.erp_v1.response.DriverResponse;
import com.jovan.erp_v1.save_as.DriverSaveAsRequest;

public interface IDriverService {

    DriverResponse create(DriverRequest request);
    DriverResponse update(Long id, DriverRequest request);
    void delete(Long id);
    DriverResponse findOneById(Long id);
    List<DriverResponse> findAllDrivers();
    DriverResponse findByPhone(String phone);
    List<DriverResponse> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);
    List<DriverResponse> findByPhoneLikeIgnoreCase(String phone);
    List<DriverResponse> findByStatus(DriverStatus status);
    List<DriverResponse> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndStatus(String firstName, String lastName, DriverStatus status);
    
    DriverResponse trackDriver(Long id);
    List<DriverResponse> generalSearch( Long id,  Long idFrom,  Long idTo, String firstName, String lastName,
    		String phone, DriverStatus status, Boolean confirmed);
    Boolean existsByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);
    DriverResponse confirmDriver(Long id);
    DriverResponse closeDriver(Long id);
    DriverResponse cancelDriver(Long id);
    DriverResponse changeStatus(Long id, DriverStatus newStatus);
    
    DriverResponse saveDriver(DriverRequest request);
    DriverResponse saveAs(DriverSaveAsRequest request);
    List<DriverResponse> saveAll(List<DriverRequest> requests);
}
