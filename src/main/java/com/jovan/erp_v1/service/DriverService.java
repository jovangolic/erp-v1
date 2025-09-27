package com.jovan.erp_v1.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.DriverStatus;
import com.jovan.erp_v1.exception.DriverErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.DriverMapper;
import com.jovan.erp_v1.model.Driver;
import com.jovan.erp_v1.repository.DriverRepository;
import com.jovan.erp_v1.request.DriverRequest;
import com.jovan.erp_v1.response.DriverResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.DriverSaveAsRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverService implements IDriverService {

    private final DriverRepository driversRepository;
    private final DriverMapper driverMapper;

    @Transactional
    @Override
    public DriverResponse create(DriverRequest request) {
    	validateString(request.firstName());
    	validateString(request.lastName());
    	validateString(request.phone());
    	validateDriverStatus(request.status());
        Driver driver = driverMapper.toEntity(request);
        Driver saved = driversRepository.save(driver);
        return new DriverResponse(saved);
    }

    @Transactional
    @Override
    public DriverResponse update(Long id, DriverRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        Driver driver = driversRepository.findById(id)
                .orElseThrow(() -> new DriverErrorException("Driver not found wtih id " + id));
        validateStringsNotEmpty("first-name, last-name, phone", request.firstName(),request.lastName(), request.phone());
    	validateDriverStatus(request.status());
    	driverMapper.toEntityUpdate(driver, request);
        Driver saved = driversRepository.save(driver);
        return new DriverResponse(saved);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!driversRepository.existsById(id)) {
            throw new DriverErrorException("Driver not found wtih id " + id);
        }
        driversRepository.deleteById(id);
    }

    @Override
    public DriverResponse findOneById(Long id) {
        Driver driver = driversRepository.findById(id)
                .orElseThrow(() -> new DriverErrorException("Driver not found wtih id " + id));
        return new DriverResponse(driver);
    }

    @Override
    public List<DriverResponse> findAllDrivers() {
    	List<Driver> items = driversRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("Driver list is empty");
    	}
        return items.stream()
                .map(DriverResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public DriverResponse findByPhone(String phone) {
    	validateString(phone);
        Driver driver = driversRepository.findByPhone(phone)
                .orElseThrow(() -> new DriverErrorException("Driver with phone not found" + phone));
        return new DriverResponse(driver);
    }
    
    @Override
	public List<DriverResponse> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName,
			String lastName) {
		validateString(firstName);
		validateString(lastName);
		List<Driver> items = driversRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);
		if(items.isEmpty()) {
			String msg = String.format("No Driver for first-name %s and last-name %s, found", 
					firstName,lastName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(driverMapper::toRespone).collect(Collectors.toList());
	}

	@Override
	public List<DriverResponse> findByPhoneLikeIgnoreCase(String phone) {
		validateString(phone);
		List<Driver> items = driversRepository.findByPhoneLikeIgnoreCase(phone);
		if(items.isEmpty()) {
			String msg = String.format("No Driver for given phone-number %s, is found", phone);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(driverMapper::toRespone).collect(Collectors.toList());
	}

	@Override
	public List<DriverResponse> generalSearch(Long id, Long idFrom, Long idTo, String firstName, String lastName,
			String phone, DriverStatus status, Boolean confirmed) {
		if (id != null) {
	        validateDriverId(id);
	        return driversRepository.findById(id)
	                .map(driverMapper::toRespone)
	                .map(Collections::singletonList)
	                .orElseThrow(() -> new NoDataFoundException("Driver not found with id " + id));
	    }
	    if (idFrom != null || idTo != null) {
	        if (idFrom == null || idTo == null) {
	            throw new ValidationException("Both idFrom and idTo must be provided for range search");
	        }
	        if (idFrom > idTo) {
	            throw new ValidationException("idFrom must not be greater than idTo");
	        }
	    }
	    if(firstName != null) validateString(firstName);
	    if(lastName != null) validateString(lastName);
	    if(phone != null) validateString(phone);
	    if(status != null) validateDriverStatus(status);
	    if(idFrom == null && idTo == null && (firstName == null || firstName.trim().isEmpty()) 
	    		&& (lastName == null || lastName.trim().isEmpty()) && (phone == null || phone.trim().isEmpty())
	    		&& (status == null && confirmed == null)) {
	    	return driversRepository.findAll().stream()
	    			.map(driverMapper::toRespone)
	    			.collect(Collectors.toList());
	    }
		return generalSearch(id, idFrom, idTo, firstName, lastName, phone,status, confirmed);
	}
	
	@Transactional
	@Override
	public DriverResponse changeStatus(Long id, DriverStatus newStatus) {
		validateDriverStatus(newStatus);
		Driver d = driversRepository.findById(id).orElseThrow(() -> new ValidationException("Driver ID must not be null"));
		if(d.getStatus() == DriverStatus.CLOSED) {
			throw new ValidationException("Closed defects cannot change status");
		}
		if(newStatus == DriverStatus.CONFIRMED) {
			if(d.getStatus() != DriverStatus.NEW) {
				throw new ValidationException("Only NEW defects can be confirmed");
			}
			d.setConfirmed(true);
		}
		d.setStatus(newStatus);
		return new DriverResponse(driversRepository.save(d));
	}
	
	@Transactional
	@Override
	public DriverResponse confirmDriver(Long id) {
		Driver d = driversRepository.findById(id).orElseThrow(() -> new ValidationException("Driver ID must not be null"));
		d.setConfirmed(true);
		d.setStatus(DriverStatus.CONFIRMED);
		driversRepository.save(d);
		return new DriverResponse(d);
	}

	@Transactional
	@Override
	public DriverResponse closeDriver(Long id) {
		Driver d = driversRepository.findById(id).orElseThrow(() -> new ValidationException("Driver ID must not be null"));
		if(d.getStatus() != DriverStatus.CONFIRMED) {
			throw new ValidationException("Only CONFIRMED defects can be closed");
		}
		d.setStatus(DriverStatus.CLOSED);
		return new DriverResponse(driversRepository.save(d));
	}

	@Transactional
	@Override
	public DriverResponse cancelDriver(Long id) {
		Driver d = driversRepository.findById(id).orElseThrow(() -> new ValidationException("Driver ID must not be null"));
		if(d.getStatus() != DriverStatus.NEW && d.getStatus() != DriverStatus.CONFIRMED) {
			throw new ValidationException("Only NEW or CONFIRMED defects can be cancelled");
		}
		d.setStatus(DriverStatus.CANCELLED);
		return new DriverResponse(driversRepository.save(d));
	}
	
	@Override
	public List<DriverResponse> findByStatus(DriverStatus status) {
		validateDriverStatus(status);
		List<Driver> items = driversRepository.findByStatus(status);
		if(items.isEmpty()) {
			String msg = String.format("No Driver for driver-status %s, is found", status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(driverMapper::toRespone).collect(Collectors.toList());
	}

	@Override
	public List<DriverResponse> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndStatus(
			String firstName, String lastName, DriverStatus status) {
		validateString(firstName);
		validateString(lastName);
		validateDriverStatus(status);
		List<Driver> items = driversRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndStatus(firstName, lastName, status);
		if(items.isEmpty()) {
			String msg = String.format("No Driver for first-name %s, last-name %s and status %s, is found", 
					firstName,lastName,status);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(driverMapper::toRespone).collect(Collectors.toList());
	}
	
	@Override
	public Boolean existsByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName,
			String lastName) {
		validateString(firstName);
		validateString(lastName);
		return driversRepository.existsByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);
	}
	
	@Transactional(readOnly = true)
	@Override
	public DriverResponse trackDriver(Long id) {
		Driver items = driversRepository.trackDriver(id).orElseThrow(() -> new ValidationException("Driver not found with id "+id));
		return new DriverResponse(items);
	}
	
	@Transactional
	@Override
	public DriverResponse saveDriver(DriverRequest request) {
		Driver d = Driver.builder()
				.id(request.id())
				.firstName(request.firstName())
				.lastName(request.lastName())
				.phone(request.phone())
				.status(request.status())
				.confirmed(request.confirmed())
				.build();
		Driver saved = driversRepository.save(d);
		return new DriverResponse(saved);
	}
	
	private final AbstractSaveAsService<Driver, DriverResponse> saveAsHelper = new AbstractSaveAsService<Driver, DriverResponse>() {
		
		@Override
		protected DriverResponse toResponse(Driver entity) {
			return new DriverResponse(entity);
		}
		
		@Override
		protected JpaRepository<Driver, Long> getRepository() {
			return driversRepository;
		}
		
		@Override
		protected Driver copyAndOverride(Driver source, Map<String, Object> overrides) {
			return Driver.builder()
					.firstName((String) overrides.getOrDefault("first-name", source.getFirstName()))
					.lastName((String) overrides.getOrDefault("last-name", source.getLastName()))
					.phone((String) overrides.getOrDefault("phone", source.getPhone()))
					.status(source.getStatus())
                    .confirmed(source.getConfirmed())
                    .build();
		}
	};
	
	private final AbstractSaveAllService<Driver, DriverResponse> saveAllHelper = new AbstractSaveAllService<Driver, DriverResponse>() {
		
		@Override
		protected Function<Driver, DriverResponse> toResponse() {
			return DriverResponse::new;
		}
		
		@Override
		protected JpaRepository<Driver, Long> getRepository() {
			return driversRepository;
		}
	};

	@Transactional
	@Override
	public DriverResponse saveAs(DriverSaveAsRequest request) {
		Map<String, Object> overrides = new HashMap<String, Object>();
		if(request.firstName() != null) overrides.put("first-name", request.firstName());
		if(request.lastName() != null) overrides.put("last-name", request.lastName());
		if(request.phone() != null) overrides.put("phone", request.phone());
		return saveAsHelper.saveAs(request.sourceId(), overrides);
	}

	@Transactional
	@Override
	public List<DriverResponse> saveAll(List<DriverRequest> requests) {
		List<Driver> items = requests.stream()
				.map(req -> Driver.builder()
						.id(req.id())
						.firstName(req.firstName())
						.lastName(req.lastName())
						.phone(req.phone())
						.status(req.status())
						.confirmed(req.confirmed())
						.build())
				.collect(Collectors.toList());
		return saveAllHelper.saveAll(items);
	}
    
	private void validateString(String str) {
		if(str == null || str.trim().isEmpty()) {
			throw new ValidationException("String must not be null nor empty");
		}
	}
	
	private Driver validateDriverId(Long id) {
		if(id == null) {
			throw new ValidationException("Driver id must not be null");
		}
		return driversRepository.findById(id).orElseThrow(() -> new ValidationException("No Driver found with id "+id));
	}

	private void validateDriverStatus(DriverStatus status) {
		Optional.ofNullable(status)
			.orElseThrow(() -> new ValidationException("DriverStatus status must not be null"));
	}

	private void validateStringsNotEmpty(String fieldName, String... values) {
	    for (String val : values) {
	        if (val == null || val.trim().isEmpty()) {
	            throw new ValidationException(fieldName + " must not be null or empty");
	        }
	    }
	}

}
