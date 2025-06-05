package com.jovan.erp_v1.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.VehicleErrorException;
import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.repository.VehicleRepository;
import com.jovan.erp_v1.request.VehicleRequest;
import com.jovan.erp_v1.response.VehicleResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService implements IVehicleService {

    private final VehicleRepository vehicleRepository;

    @Transactional
    @Override
    public VehicleResponse create(VehicleRequest request) {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(request.model());
        vehicle.setRegistrationNumber(request.registrationNumber());
        return new VehicleResponse(vehicle);
    }

    @Transactional
    @Override
    public VehicleResponse update(Long id, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleErrorException("Vehicle not found " + id));
        vehicle.setModel(request.model());
        vehicle.setRegistrationNumber(request.registrationNumber());
        return new VehicleResponse(vehicle);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new VehicleErrorException("Vehicle not found " + id);
        }
        vehicleRepository.deleteById(id);
    }

    @Override
    public VehicleResponse findById(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<VehicleResponse> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public VehicleResponse findByModel(String model) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public VehicleResponse findByDistinctRegistrationNumber(String registrationNumber) {
        // TODO Auto-generated method stub
        return null;
    }
}
