package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.enumeration.VehicleStatus;
import com.jovan.erp_v1.request.VehicleRequest;
import com.jovan.erp_v1.response.VehicleResponse;

public interface IVehicleService {

    VehicleResponse create(VehicleRequest request);

    VehicleResponse update(Long id, VehicleRequest request);

    void delete(Long id);

    VehicleResponse findById(Long id);

    List<VehicleResponse> findAll();

    VehicleResponse findBy_Model(String model);

    VehicleResponse findByRegistrationNumber(String registrationNumber);

    List<VehicleResponse> filterVehicles(String model, String status);

    List<VehicleResponse> search(String keyword);

    List<VehicleResponse> findByStatus(VehicleStatus status);

    List<VehicleResponse> findByModelAndStatus(String model, VehicleStatus status);
}
