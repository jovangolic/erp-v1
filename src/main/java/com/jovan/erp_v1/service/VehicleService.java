package com.jovan.erp_v1.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.VehicleFuel;
import com.jovan.erp_v1.enumeration.VehicleStatus;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.exception.VehicleErrorException;
import com.jovan.erp_v1.mapper.VehicleMapper;
import com.jovan.erp_v1.model.Vehicle;
import com.jovan.erp_v1.repository.VehicleRepository;
import com.jovan.erp_v1.repository.specification.VehicleSpecification;
import com.jovan.erp_v1.request.VehicleRequest;
import com.jovan.erp_v1.response.VehicleResponse;
import com.jovan.erp_v1.search_request.VehicleSearchRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService implements IVehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Transactional
    @Override
    public VehicleResponse create(VehicleRequest request) {
    	validateString(request.registrationNumber());
        validateString(request.model());
        validateVehicleStatus(request.status());
        validateVehicleFuel(request.fuel());
        Vehicle vehicle = vehicleMapper.toEntity(request);
        Vehicle saved = vehicleRepository.save(vehicle);
        return new VehicleResponse(saved);
    }

    @Transactional
    @Override
    public VehicleResponse update(Long id, VehicleRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleErrorException("Vehicle not found " + id));
        validateString(request.registrationNumber());
        validateString(request.model());
        validateVehicleStatus(request.status());
        validateVehicleFuel(request.fuel());
        vehicleMapper.toEntityUpdate(vehicle, request);
        Vehicle v = vehicleRepository.save(vehicle);
        return new VehicleResponse(v);
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
    	List<Vehicle> items = vehicleRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("List of vehicles is empty");
    	}
        return items.stream()
                .map(VehicleResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleResponse findByRegistrationNumber(String registrationNumber) {
    	validateString(registrationNumber);
        Vehicle vehicle = vehicleRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new VehicleErrorException("Vehicle with that registration number not found"));
        return new VehicleResponse(vehicle);
    }

    @Override
    public List<VehicleResponse> filterVehicles(String model, String status) {
    	validateString(model);
    	validateString(status);
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
    	validateString(keyword);
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
    	validateVehicleStatus(status);
    	List<Vehicle> items = vehicleRepository.findByStatus(status);
    	if(items.isEmpty()) {
    		String msg = String.format("Vehicle status %s is not found", status);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(VehicleResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleResponse> findByModelAndStatus(String model, VehicleStatus status) {
    	validateString(model);
    	validateVehicleStatus(status);
    	List<Vehicle> items = vehicleRepository.findByModelAndStatus(model, status);
    	if(items.isEmpty()) {
    		String msg = String.format("Vehicle model %s and vehicle status %s is not found", model,status);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(VehicleResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleResponse> findByModel(String model) {
    	validateString(model);
    	List<Vehicle> items = vehicleRepository.findByModel(model);
    	if(items.isEmpty()) {
    		String msg = String.format("Vehicle model equal to %s is not found", model);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(VehicleResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleResponse> findByModelContainingIgnoreCase(String modelFragment) {
    	validateString(modelFragment);
    	List<Vehicle> items = vehicleRepository.findByModelContainingIgnoreCase(modelFragment);
    	if(items.isEmpty()) {
    		String msg = String.format("Vehicle containing model fragment %s is not found", modelFragment);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(VehicleResponse::new)
                .collect(Collectors.toList());
    }
    
    @Override
	public List<VehicleResponse> findByFuel(VehicleFuel fuel) {
		validateVehicleFuel(fuel);
		List<Vehicle> items = vehicleRepository.findByFuel(fuel);
		if(items.isEmpty()) {
			String msg = String.format("No Vehicle for fuel type %s, is found", fuel);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(vehicleMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<VehicleResponse> findByModelContainingIgnoreCaseAndFuel(String model, VehicleFuel fuel) {
		validateString(model);
		validateVehicleFuel(fuel);
		List<Vehicle> items = vehicleRepository.findByModelContainingIgnoreCaseAndFuel(model, fuel);
		if(items.isEmpty()) {
			String msg = String.format("No Vehicle for model %s and fuel type %s, is found", 
					model, fuel);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(vehicleMapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<VehicleResponse> generalSearch(VehicleSearchRequest request) {
		Specification<Vehicle> vehicles = VehicleSpecification.fromRequest(request);
		List<Vehicle> items = vehicleRepository.findAll(vehicles);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Vehicles found for given search criteria");
		}
		return items.stream().map(vehicleMapper::toResponse).collect(Collectors.toList());
	}
    
    private void validateString(String str) {
    	if(str == null || str.trim().isEmpty()) {
    		throw new ValidationException("String must not be null nor empty");
    	}
    }
    
    private void validateVehicleStatus(VehicleStatus status) {
    	Optional.ofNullable(status)
    		.orElseThrow(() -> new ValidationException("VehicleStatus status must not be null"));
    }
    
    private void validateVehicleFuel(VehicleFuel fuel) {
    	Optional.ofNullable(fuel)
    		.orElseThrow(() -> new ValidationException("VehicleFuel fuel must nor be null"));
    }


}
