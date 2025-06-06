package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.VehicleStatus;
import com.jovan.erp_v1.exception.VehicleErrorException;
import com.jovan.erp_v1.mapper.VehicleMapper;
import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.repository.VehicleRepository;
import com.jovan.erp_v1.request.VehicleRequest;
import com.jovan.erp_v1.response.VehicleResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService implements IVehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Transactional
    @Override
    public VehicleResponse create(VehicleRequest request) {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(request.model());
        vehicle.setRegistrationNumber(request.registrationNumber());
        vehicle.setStatus(request.status());
        return new VehicleResponse(vehicle);
    }

    @Transactional
    @Override
    public VehicleResponse update(Long id, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleErrorException("Vehicle not found " + id));
        vehicle.setModel(request.model());
        vehicle.setRegistrationNumber(request.registrationNumber());
        vehicle.setStatus(request.status());
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
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleErrorException("Vehicle not found with id: " + id));
        return new VehicleResponse(vehicle);
    }

    @Override
    public List<VehicleResponse> findAll() {
        return vehicleRepository.findAll().stream()
                .map(VehicleResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleResponse findByRegistrationNumber(String registrationNumber) {
        Vehicle vehicle = vehicleRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new VehicleErrorException("Vehicle with that registration number not found"));
        return new VehicleResponse(vehicle);
    }

    @Override
    public List<VehicleResponse> filterVehicles(String model, String status) {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Vehicle> filteredVehicles = vehicles.stream()
                .filter(v -> {
                    boolean modelMatches = (model == null || model.isEmpty()) ||
                            (v.getModel() != null && v.getModel().equalsIgnoreCase(model));
                    boolean statusMatches = (status == null || status.isEmpty()) ||
                            (v.getStatus() != null && v.getStatus().name().equalsIgnoreCase(status));
                    return modelMatches && statusMatches;
                })
                .collect(Collectors.toList());
        return vehicleMapper.toResponseList(filteredVehicles);
    }

    @Override
    public List<VehicleResponse> search(String keyword) {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Vehicle> result = vehicles.stream()
                .filter(v -> v.getModel() != null && v.getModel().toLowerCase().contains(keyword.toLowerCase()) ||
                        v.getRegistrationNumber() != null
                                && v.getRegistrationNumber().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        return vehicleMapper.toResponseList(result);
    }

    @Override
    public List<VehicleResponse> findByStatus(VehicleStatus status) {
        return vehicleRepository.findByStatus(status).stream()
                .map(VehicleResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleResponse> findByModelAndStatus(String model, VehicleStatus status) {
        return vehicleRepository.findByModelAndStatus(model, status).stream()
                .map(VehicleResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleResponse> findByModel(String model) {
        return vehicleRepository.findByModel(model).stream()
                .map(VehicleResponse::new)
                .collect(Collectors.toList());
    }
}
